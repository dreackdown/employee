package dev.hugofaria.employee.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hugofaria.employee.config.TestConfigs;
import dev.hugofaria.employee.integrationtests.dto.AccountCredentialsDto;
import dev.hugofaria.employee.integrationtests.dto.EmployeeDTO;
import dev.hugofaria.employee.integrationtests.dto.TokenDto;
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

import java.util.List;

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
    @Order(0)
    public void authorization() {

        AccountCredentialsDto user = new AccountCredentialsDto("hugo", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(user)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenDto.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/employee/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    public void testCreate() throws JsonProcessingException {
        mockEmployee();

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

        assertEquals("Nelson", persistedEmployee.getFirstName());
        assertEquals("Piquet", persistedEmployee.getLastName());
        assertEquals("Developer", persistedEmployee.getRole());
    }

    @Test
    @Order(2)
    public void testUpdate() throws JsonProcessingException {
        employee.setLastName("Piquet Souto Maior");

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

        assertEquals(employee.getEmployeeId(), persistedEmployee.getEmployeeId());

        assertEquals("Nelson", persistedEmployee.getFirstName());
        assertEquals("Piquet Souto Maior", persistedEmployee.getLastName());
        assertEquals("Developer", persistedEmployee.getRole());
    }

    @Test
    @Order(3)
    public void testFindById() throws JsonProcessingException {
        mockEmployee();

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

        EmployeeDTO persistedPerson = objectMapper.readValue(content, EmployeeDTO.class);
        employee = persistedPerson;

        assertNotNull(persistedPerson);

        assertNotNull(persistedPerson.getEmployeeId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getRole());

        assertEquals(employee.getEmployeeId(), persistedPerson.getEmployeeId());

        assertEquals("Nelson", persistedPerson.getFirstName());
        assertEquals("Piquet Souto Maior", persistedPerson.getLastName());
        assertEquals("Developer", persistedPerson.getRole());
    }

    @Test
    @Order(4)
    public void testDelete() {

        given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", employee.getEmployeeId())
                .when()
                .delete("{id}")
                .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    public void testFindAll() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        List<EmployeeDTO> employees = objectMapper.readValue(content, new TypeReference<>() {
        });

        EmployeeDTO foundEmployeeOne = employees.get(0);

        assertNotNull(foundEmployeeOne.getEmployeeId());
        assertNotNull(foundEmployeeOne.getFirstName());
        assertNotNull(foundEmployeeOne.getLastName());
        assertNotNull(foundEmployeeOne.getRole());

        assertEquals(1, foundEmployeeOne.getEmployeeId());

        assertEquals("Milja", foundEmployeeOne.getFirstName());
        assertEquals("Ollila", foundEmployeeOne.getLastName());
        assertEquals("Mystic", foundEmployeeOne.getRole());

        EmployeeDTO foundEmployeeSix = employees.get(5);

        assertNotNull(foundEmployeeSix.getEmployeeId());
        assertNotNull(foundEmployeeSix.getFirstName());
        assertNotNull(foundEmployeeSix.getLastName());
        assertNotNull(foundEmployeeSix.getRole());

        assertEquals(9, foundEmployeeSix.getEmployeeId());

        assertEquals("Slaviša", foundEmployeeSix.getFirstName());
        assertEquals("Heiko", foundEmployeeSix.getLastName());
        assertEquals("Archivist", foundEmployeeSix.getRole());
    }


    @Test
    @Order(6)
    public void testFindAllWithoutToken() {

        RequestSpecification specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/employee/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .when()
                .get()
                .then()
                .statusCode(403);
    }

    private void mockEmployee() {
        employee.setFirstName("Nelson");
        employee.setLastName("Piquet");
        employee.setRole("Developer");
    }
}