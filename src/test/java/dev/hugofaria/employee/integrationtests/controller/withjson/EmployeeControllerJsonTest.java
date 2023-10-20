package dev.hugofaria.employee.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hugofaria.employee.config.TestConfigs;
import dev.hugofaria.employee.integrationtests.dto.EmployeeDTO;
import dev.hugofaria.employee.integrationtests.testcontainers.AbstractIntegrationTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class EmployeeControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;

    private static ObjectMapper objectMapper;

    private static EmployeeDTO employee;

    @BeforeAll
    public static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        employee = new EmployeeDTO();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockEmployee();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/employee/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(employee)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        EmployeeDTO persistedEmployee = objectMapper.readValue(content, EmployeeDTO.class);
        employee = persistedEmployee;

        assertNotNull(persistedEmployee);

        assertNotNull(persistedEmployee.getEmployeeId());
        assertNotNull(persistedEmployee.getFirstName());
        assertNotNull(persistedEmployee.getLastName());
        assertNotNull(persistedEmployee.getRole());

        assertTrue(persistedEmployee.getEmployeeId() > 0);

        assertEquals("Richard", persistedEmployee.getFirstName());
        assertEquals("Stallman", persistedEmployee.getLastName());
        assertEquals("Manager", persistedEmployee.getRole());
    }

    @Test
    @Order(2)
    public void testCreateWithWrongOrigin() {
        mockEmployee();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/employee/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(employee)
                .when()
                .post()
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonProcessingException {
        mockEmployee();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                .setBasePath("/api/employee/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", employee.getEmployeeId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        EmployeeDTO persistedEmployee = objectMapper.readValue(content, EmployeeDTO.class);
        employee = persistedEmployee;

        assertNotNull(persistedEmployee);

        assertNotNull(persistedEmployee.getEmployeeId());
        assertNotNull(persistedEmployee.getFirstName());
        assertNotNull(persistedEmployee.getLastName());
        assertNotNull(persistedEmployee.getRole());

        assertTrue(persistedEmployee.getEmployeeId() > 0);

        assertEquals("Richard", persistedEmployee.getFirstName());
        assertEquals("Stallman", persistedEmployee.getLastName());
        assertEquals("Manager", persistedEmployee.getRole());
    }

    @Test
    @Order(4)
    public void testFindByIdWithWrongOrigin() {
        mockEmployee();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
                .setBasePath("/api/employee/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", employee.getEmployeeId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();


        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    private void mockEmployee() {
        employee.setFirstName("Richard");
        employee.setLastName("Stallman");
        employee.setRole("Manager");
    }
}