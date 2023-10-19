package dev.hugofaria.employeeapi.service;

import dev.hugofaria.employeeapi.dto.v1.EmployeeDto;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDto> populateEmployees();
}