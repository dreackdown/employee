package dev.hugofaria.employee.integrationtests.repository;

import dev.hugofaria.employee.entity.Employee;
import dev.hugofaria.employee.integrationtests.testcontainers.AbstractIntegrationTest;
import dev.hugofaria.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.ASC;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(OrderAnnotation.class)
public class EmployeeRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    public EmployeeRepository repository;

    private static Employee employee;

    @BeforeAll
    public static void setup() {
        employee = new Employee();
    }

    @Test
    @Order(1)
    public void testFindByName() {

        Pageable pageable = PageRequest.of(0, 6, Sort.by(ASC, "firstName"));
        employee = repository.findEmployeeByName("pap", pageable).getContent().get(0);

        assertNotNull(employee.getEmployeeId());
        assertNotNull(employee.getFirstName());
        assertNotNull(employee.getLastName());
        assertNotNull(employee.getRole());

        assertTrue(employee.getEnabled());

        assertEquals(798, employee.getEmployeeId());

        assertEquals("Papagena", employee.getFirstName());
        assertEquals("Dun", employee.getLastName());
        assertEquals("Estimator", employee.getRole());
    }

    @Test
    @Order(2)
    public void testDisableEmployee() {

        repository.disableEmployee(employee.getEmployeeId());

        Pageable pageable = PageRequest.of(0, 6, Sort.by(ASC, "firstName"));
        employee = repository.findEmployeeByName("pap", pageable).getContent().get(0);

        assertNotNull(employee.getEmployeeId());
        assertNotNull(employee.getFirstName());
        assertNotNull(employee.getLastName());
        assertNotNull(employee.getRole());

        assertFalse(employee.getEnabled());

        assertEquals(798, employee.getEmployeeId());

        assertEquals("Papagena", employee.getFirstName());
        assertEquals("Dun", employee.getLastName());
        assertEquals("Estimator", employee.getRole());
    }
}