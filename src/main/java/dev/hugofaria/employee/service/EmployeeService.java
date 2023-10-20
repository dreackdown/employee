package dev.hugofaria.employee.service;

import dev.hugofaria.employee.dto.v1.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> populateEmployees();
}