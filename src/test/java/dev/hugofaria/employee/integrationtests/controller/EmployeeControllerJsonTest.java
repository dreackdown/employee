package dev.hugofaria.employee.integrationtests.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hugofaria.employee.config.TestConfigs;
import dev.hugofaria.employee.integrationtests.dto.AccountCredentialsDto;
import dev.hugofaria.employee.integrationtests.dto.EmployeeDTO;
import dev.hugofaria.employee.integrationtests.dto.TokenDto;
import dev.hugofaria.employee.integrationtests.dto.wrappers.WrapperEmployeeDto;
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
        assertTrue(persistedEmployee.getEnabled());

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
        assertTrue(persistedEmployee.getEnabled());

        assertEquals(employee.getEmployeeId(), persistedEmployee.getEmployeeId());

        assertEquals("Nelson", persistedEmployee.getFirstName());
        assertEquals("Piquet Souto Maior", persistedEmployee.getLastName());
        assertEquals("Developer", persistedEmployee.getRole());
    }

    @Test
    @Order(3)
    public void testDisableEmployeeById() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", employee.getEmployeeId())
                .when()
                .patch("{id}")
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
        assertFalse(persistedEmployee.getEnabled());

        assertEquals(employee.getEmployeeId(), persistedEmployee.getEmployeeId());

        assertEquals("Nelson", persistedEmployee.getFirstName());
        assertEquals("Piquet Souto Maior", persistedEmployee.getLastName());
        assertEquals("Developer", persistedEmployee.getRole());
    }

    @Test
    @Order(4)
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

        EmployeeDTO persistedEmployee = objectMapper.readValue(content, EmployeeDTO.class);
        employee = persistedEmployee;

        assertNotNull(persistedEmployee);

        assertNotNull(persistedEmployee.getEmployeeId());
        assertNotNull(persistedEmployee.getFirstName());
        assertNotNull(persistedEmployee.getLastName());
        assertNotNull(persistedEmployee.getRole());
        assertFalse(persistedEmployee.getEnabled());

        assertEquals(employee.getEmployeeId(), persistedEmployee.getEmployeeId());

        assertEquals("Nelson", persistedEmployee.getFirstName());
        assertEquals("Piquet Souto Maior", persistedEmployee.getLastName());
        assertEquals("Developer", persistedEmployee.getRole());
    }

    @Test
    @Order(5)
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
    @Order(6)
    public void testFindAll() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperEmployeeDto wrapper = objectMapper.readValue(content, WrapperEmployeeDto.class);
        var employees = wrapper.getEmbedded().getEmployees();

        EmployeeDTO foundEmployeeOne = employees.get(0);

        assertNotNull(foundEmployeeOne.getEmployeeId());
        assertNotNull(foundEmployeeOne.getFirstName());
        assertNotNull(foundEmployeeOne.getLastName());
        assertNotNull(foundEmployeeOne.getRole());

        assertEquals(282, foundEmployeeOne.getEmployeeId());

        assertEquals("Allina", foundEmployeeOne.getFirstName());
        assertEquals("Fonteyne", foundEmployeeOne.getLastName());
        assertEquals("Construction Worker", foundEmployeeOne.getRole());

        EmployeeDTO foundEmployeeSix = employees.get(5);

        assertNotNull(foundEmployeeSix.getEmployeeId());
        assertNotNull(foundEmployeeSix.getFirstName());
        assertNotNull(foundEmployeeSix.getLastName());
        assertNotNull(foundEmployeeSix.getRole());

        assertEquals(420, foundEmployeeSix.getEmployeeId());

        assertEquals("Amara", foundEmployeeSix.getFirstName());
        assertEquals("Bruckent", foundEmployeeSix.getLastName());
        assertEquals("Construction Foreman", foundEmployeeSix.getRole());
    }

    @Test
    @Order(7)
    public void testFindByName() throws JsonProcessingException {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("firstName", "cal")
                .queryParams("page", 0, "size", 6, "direction", "asc")
                .when()
                .get("findPersonByName/{firstName}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperEmployeeDto wrapper = objectMapper.readValue(content, WrapperEmployeeDto.class);
        var people = wrapper.getEmbedded().getEmployees();

        EmployeeDTO foundPersonOne = people.get(0);

        assertNotNull(foundPersonOne.getEmployeeId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getRole());

        assertTrue(foundPersonOne.getEnabled());

        assertEquals(22, foundPersonOne.getEmployeeId());

        assertEquals("Caleb", foundPersonOne.getFirstName());
        assertEquals("Holdworth", foundPersonOne.getLastName());
        assertEquals("Electrician", foundPersonOne.getRole());
    }

    @Test
    @Order(8)
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

    @Test
    @Order(9)
    public void testHATEOAS() {

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .accept(TestConfigs.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when()
                .get()
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/employee/v1/227\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/employee/v1/282\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/employee/v1/198\"}}}"));

        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/employee/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/employee/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/employee/v1?page=3&size=10&direction=asc\"}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/employee/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/employee/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));

        assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1007,\"totalPages\":101,\"number\":3}}"));
    }

    private void mockEmployee() {
        employee.setFirstName("Nelson");
        employee.setLastName("Piquet");
        employee.setRole("Developer");
        employee.setEnabled(true);
    }
}