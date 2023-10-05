package dev.hugofaria.employeeapi.unittests.mapper.mocks;

import dev.hugofaria.employeeapi.dto.EmployeeDTO;
import dev.hugofaria.employeeapi.entity.Employee;

import java.util.ArrayList;
import java.util.List;

public class MockEmployee {

    public Employee mockEntity() {
        return mockEntity(0);
    }

    public EmployeeDTO mockDto() {
        return mockDto(0);
    }

    public List<Employee> mockEntityList() {
        List<Employee> employees = new ArrayList<Employee>();
        for (int i = 0; i < 14; i++) {
            employees.add(mockEntity(i));
        }
        return employees;
    }

    public List<EmployeeDTO> mockDTOList() {
        List<EmployeeDTO> employees = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            employees.add(mockDto(i));
        }
        return employees;
    }

    public Employee mockEntity(Integer number) {
        Employee employee = new Employee();
        employee.setEmployeeId(number.longValue());
        employee.setFirstName("First Name Test" + number);
        employee.setLastName("Last Name Test" + number);
        employee.setRole("Role Test" + number);
        return employee;
    }

    public EmployeeDTO mockDto(Integer number) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(number.longValue());
        employee.setFirstName("First Name Test" + number);
        employee.setLastName("Last Name Test" + number);
        employee.setRole("Role Test" + number);
        return employee;
    }
}