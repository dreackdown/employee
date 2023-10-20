package dev.hugofaria.employee.unittests.mapper.mocks;

import dev.hugofaria.employee.dto.v1.EmployeeDto;
import dev.hugofaria.employee.entity.Employee;

import java.util.ArrayList;
import java.util.List;

public class MockEmployee {

    public Employee mockEntity() {
        return mockEntity(0);
    }

    public EmployeeDto mockDto() {
        return mockDto(0);
    }

    public List<Employee> mockEntityList() {
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            employees.add(mockEntity(i));
        }
        return employees;
    }

    public List<EmployeeDto> mockDTOList() {
        List<EmployeeDto> employees = new ArrayList<>();
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

    public EmployeeDto mockDto(Integer number) {
        EmployeeDto employee = new EmployeeDto();
        employee.setEmployeeId(number.longValue());
        employee.setFirstName("First Name Test" + number);
        employee.setLastName("Last Name Test" + number);
        employee.setRole("Role Test" + number);
        return employee;
    }
}