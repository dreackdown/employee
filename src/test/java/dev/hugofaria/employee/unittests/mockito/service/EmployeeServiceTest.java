package dev.hugofaria.employee.unittests.mockito.service;

import dev.hugofaria.employee.dto.v1.EmployeeDto;
import dev.hugofaria.employee.entity.Employee;
import dev.hugofaria.employee.exception.RequiredObjectIsNullException;
import dev.hugofaria.employee.repository.EmployeeRepository;
import dev.hugofaria.employee.service.EmployeeService;
import dev.hugofaria.employee.unittests.mapper.mocks.MockEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    MockEmployee input;

    @InjectMocks
    private EmployeeService service;

    @Mock
    EmployeeRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockEmployee();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Employee entity = input.mockEntity(1);
        entity.setEmployeeId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findEmployeeById(1L);
        assertNotNull(result);
        assertNotNull(result.getEmployeeId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/employee/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Role Test1", result.getRole());
    }

    @Test
    void testCreate() {
        Employee entity = input.mockEntity(1);
        entity.setEmployeeId(1L);

        Employee persisted = entity;
        persisted.setEmployeeId(1L);

        EmployeeDto vo = input.mockDto(1);
        vo.setEmployeeId(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.findEmployeeById(1L);
        assertNotNull(result);
        assertNotNull(result.getEmployeeId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/employee/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Role Test1", result.getRole());
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.createEmployee(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testUpdate() {
        Employee entity = input.mockEntity(1);

        Employee persisted = entity;
        persisted.setEmployeeId(1L);

        EmployeeDto vo = input.mockDto(1);
        vo.setEmployeeId(1L);


        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.updateEmployee(vo);

        assertNotNull(result);
        assertNotNull(result.getEmployeeId());
        assertNotNull(result.getLinks());

        assertTrue(result.toString().contains("links: [</api/employee/v1/1>;rel=\"self\"]"));
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Role Test1", result.getRole());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.updateEmployee(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testDelete() {
        Employee entity = input.mockEntity(1);
        entity.setEmployeeId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.deleteEmployee(1L);
    }
}