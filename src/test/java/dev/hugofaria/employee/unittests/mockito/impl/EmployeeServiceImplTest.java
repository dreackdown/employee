package dev.hugofaria.employee.unittests.mockito.impl;

import dev.hugofaria.employee.dto.v1.EmployeeDto;
import dev.hugofaria.employee.entity.Employee;
import dev.hugofaria.employee.exception.RequiredObjectIsNullException;
import dev.hugofaria.employee.repository.EmployeeRepository;
import dev.hugofaria.employee.service.impl.EmployeeServiceImpl;
import dev.hugofaria.employee.unittests.mapper.mocks.MockEmployee;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(Lifecycle.PER_CLASS)
class EmployeeServiceImplTest {

    MockEmployee input;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @BeforeAll
    void setUp() {
        input = new MockEmployee();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findEmployeeById() {
        Employee entity = input.mockEntity(1);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));

        var result = employeeService.findEmployeeById(1L);

        assertNotNull(result);
        assertNotNull(result.getEmployeeId());
        assertNotNull(result.getLinks());

        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Role Test1", result.getRole());
    }

//    @Test
//    void findAllEmployees() {
//        List<Employee> list = input.mockEntityList();
//
//        when(employeeRepository.findAll()).thenReturn(list);
//
//        var employees = employeeService.findAllEmployees(pageable);
//
//        assertNotNull(employees);
//        assertEquals(14, employees.size());
//
//        var personOne = employees.get(1);
//
//        assertNotNull(personOne);
//        assertNotNull(personOne.getEmployeeId());
//        assertNotNull(personOne.getLinks());
//
//        assertEquals("First Name Test1", personOne.getFirstName());
//        assertEquals("Last Name Test1", personOne.getLastName());
//        assertEquals("Role Test1", personOne.getRole());
//
//        var personFour = employees.get(4);
//
//        assertNotNull(personFour);
//        assertNotNull(personFour.getEmployeeId());
//        assertNotNull(personFour.getLinks());
//
//        assertEquals("First Name Test4", personFour.getFirstName());
//        assertEquals("Last Name Test4", personFour.getLastName());
//        assertEquals("Role Test4", personFour.getRole());
//
//        var personSeven = employees.get(7);
//
//        assertNotNull(personSeven);
//        assertNotNull(personSeven.getEmployeeId());
//        assertNotNull(personSeven.getLinks());
//
//        assertEquals("First Name Test7", personSeven.getFirstName());
//        assertEquals("Last Name Test7", personSeven.getLastName());
//        assertEquals("Role Test7", personSeven.getRole());
//    }

//    @Test
//    void createEmployee() {
//        Employee entity = input.mockEntity(1);
//        entity.setEmployeeId(1L);
//
//        Employee persisted = entity;
//        persisted.setEmployeeId(1L);
//
//        EmployeeDto dto = input.mockDto(1);
//        dto.setEmployeeId(1L);
//
//        when(employeeRepository.save(entity)).thenReturn(persisted);
//
//        var result = employeeService.createEmployee(dto);
//
//        assertNotNull(result);
//        assertNotNull(result.getEmployeeId());
//        assertNotNull(result.getLinks());
//
//
//        assertEquals("First Name Test1", result.getFirstName());
//        assertEquals("Last Name Test1", result.getLastName());
//        assertEquals("Role Test1", result.getRole());
//    }

    @Test
    void createWithNullEmployee() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            employeeService.createEmployee(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void updateEmployee() {
        Employee entity = input.mockEntity(1);

        Employee persisted = entity;
        persisted.setEmployeeId(1L);

        EmployeeDto dto = input.mockDto(1);
        dto.setEmployeeId(1L);


        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(employeeRepository.save(entity)).thenReturn(persisted);

        var result = employeeService.updateEmployee(dto);

        assertNotNull(result);
        assertNotNull(result.getEmployeeId());
        assertNotNull(result.getLinks());

        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Role Test1", result.getRole());
    }

    @Test
    void testUpdateWithNullEmployee() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class, () -> {
            employeeService.updateEmployee(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteEmployee() {
        Employee entity = input.mockEntity(1);
        entity.setEmployeeId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(entity));

        employeeService.deleteEmployee(1L);
    }
}