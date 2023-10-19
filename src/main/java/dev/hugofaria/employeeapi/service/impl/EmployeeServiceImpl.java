package dev.hugofaria.employeeapi.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hugofaria.employeeapi.controller.EmployeeController;
import dev.hugofaria.employeeapi.dto.v1.EmployeeDto;
import dev.hugofaria.employeeapi.entity.Employee;
import dev.hugofaria.employeeapi.exception.RequiredObjectIsNullException;
import dev.hugofaria.employeeapi.exception.ResourceNotFoundException;
import dev.hugofaria.employeeapi.mapper.DozerMapper;
import dev.hugofaria.employeeapi.repository.EmployeeRepository;
import dev.hugofaria.employeeapi.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.ArrayList;
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
    public List<EmployeeDto> populateEmployees() {
        logger.info("populate employee with external service...");

        ObjectMapper objectMapper = new ObjectMapper();
        String url = "https://randomuser.me/api/?results=10&inc=name";
        WebClient webClient = WebClient.create();

        String employeeJson = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            JsonNode jsonNode = objectMapper.readTree(employeeJson);
            JsonNode resultsNode = jsonNode.get("results");

            List<EmployeeDto> employees = new ArrayList<>();

            for (JsonNode result : resultsNode) {
                JsonNode nameNode = result.get("name");
                String firstName = nameNode.get("first").asText();
                String lastName = nameNode.get("last").asText();

                EmployeeDto employeeDTO = new EmployeeDto(firstName, lastName);
                employees.add(employeeDTO);
            }

            return employees;

        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<EmployeeDto> findAllEmployees() {
        logger.info("finding all employee...");

        var employees = DozerMapper.parseListObjects(employeeRepository.findAll(), EmployeeDto.class);
        employees.forEach(p -> p.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(p.getEmployeeId())).withSelfRel()));
        return employees;
    }

    public EmployeeDto findEmployeeById(Long id) {
        logger.info("finding one employee...!");

        var entity = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, EmployeeDto.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(id)).withSelfRel());
        return dto;
    }

    public EmployeeDto createEmployee(EmployeeDto employee) {
        if (employee == null) throw new RequiredObjectIsNullException();
        logger.info("creating employee...");

        var entity = DozerMapper.parseObject(employee, Employee.class);
        var dto = DozerMapper.parseObject(employeeRepository.save(entity), EmployeeDto.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(dto.getEmployeeId())).withSelfRel());
        return dto;
    }

    public EmployeeDto updateEmployee(EmployeeDto employee) {
        if (employee == null) throw new RequiredObjectIsNullException();
        logger.info("updating employee...");

        var entity = employeeRepository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setRole(employee.getRole());

        var updatedEmployee = DozerMapper.parseObject(employeeRepository.save(entity), EmployeeDto.class);
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