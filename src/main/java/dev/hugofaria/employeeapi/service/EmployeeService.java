package dev.hugofaria.employeeapi.service;

import dev.hugofaria.employeeapi.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> populateEmployees();
}