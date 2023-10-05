package dev.hugofaria.employeeapi.service.impl;

import dev.hugofaria.employeeapi.controller.EmployeeController;
import dev.hugofaria.employeeapi.dto.EmployeeDTO;
import dev.hugofaria.employeeapi.entity.Employee;
import dev.hugofaria.employeeapi.exception.RequiredObjectIsNullException;
import dev.hugofaria.employeeapi.exception.ResourceNotFoundException;
import dev.hugofaria.employeeapi.mapper.DozerMapper;
import dev.hugofaria.employeeapi.repository.EmployeeRepository;
import dev.hugofaria.employeeapi.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    final EmployeeRepository employeeRepository;

    private final Logger logger = Logger.getLogger(EmployeeServiceImpl.class.getName());

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.employeeRepository = repository;
    }

    @Override
    public List<EmployeeDTO> populateEmployees() {
        logger.info("populate employee with external service...");

        String url = "https://randomuser.me/api/?results=10&inc=name";

        WebClient webClient = WebClient.create();

        String employeeJson = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(employeeJson);

        return Collections.emptyList();
    }

    public List<EmployeeDTO> findAllEmployees() {
        logger.info("finding all employee...");

        var employees = DozerMapper.parseListObjects(employeeRepository.findAll(), EmployeeDTO.class);
        employees.forEach(p -> p.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(p.getEmployeeId())).withSelfRel()));
        return employees;
    }

    public EmployeeDTO findEmployeeById(Long id) {
        logger.info("finding one employee...!");

        var entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, EmployeeDTO.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(id)).withSelfRel());
        return dto;
    }

    public EmployeeDTO createEmployee(EmployeeDTO employee) {
        if (employee == null) throw new RequiredObjectIsNullException();
        logger.info("creating employee...");

        var entity = DozerMapper.parseObject(employee, Employee.class);
        var dto = DozerMapper.parseObject(employeeRepository.save(entity), EmployeeDTO.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(dto.getEmployeeId())).withSelfRel());
        return dto;
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employee) {
        if (employee == null) throw new RequiredObjectIsNullException();
        logger.info("updating employee...");

        var entity = employeeRepository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setRole(employee.getRole());

        var updatedEmployee = DozerMapper.parseObject(employeeRepository.save(entity), EmployeeDTO.class);
        updatedEmployee.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(updatedEmployee.getEmployeeId())).withSelfRel());
        return updatedEmployee;
    }

    public void deleteEmployee(Long id) {
        logger.info("deleting employee...");
        var entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        employeeRepository.delete(entity);
    }
}