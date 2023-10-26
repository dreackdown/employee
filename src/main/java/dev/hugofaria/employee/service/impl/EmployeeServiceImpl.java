package dev.hugofaria.employee.service.impl;

import dev.hugofaria.employee.controller.EmployeeController;
import dev.hugofaria.employee.dto.v1.EmployeeDto;
import dev.hugofaria.employee.entity.Employee;
import dev.hugofaria.employee.exception.RequiredObjectIsNullException;
import dev.hugofaria.employee.exception.ResourceNotFoundException;
import dev.hugofaria.employee.mapper.DozerMapper;
import dev.hugofaria.employee.repository.EmployeeRepository;
import dev.hugofaria.employee.service.EmployeeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    final EmployeeRepository repository;

    final
    PagedResourcesAssembler<EmployeeDto> assembler;

    private final Logger logger = Logger.getLogger(EmployeeServiceImpl.class.getName());

    public EmployeeServiceImpl(EmployeeRepository repository, PagedResourcesAssembler<EmployeeDto> assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    @Override
    public List<EmployeeDto> populateEmployees() {
        logger.info("populate employee with external service...");

//        ObjectMapper objectMapper = new ObjectMapper();
//        String url = "https://randomuser.me/api/?results=10&inc=name";
//        WebClient webClient = WebClient.create();
//
//        String employeeJson = webClient.get()
//                .uri(url)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        try {
//            JsonNode jsonNode = objectMapper.readTree(employeeJson);
//            JsonNode resultsNode = jsonNode.get("results");
//
//            List<EmployeeDto> employees = new ArrayList<>();
//
//            for (JsonNode result : resultsNode) {
//                JsonNode nameNode = result.get("name");
//                String firstName = nameNode.get("first").asText();
//                String lastName = nameNode.get("last").asText();
//
//                EmployeeDto employeeDTO = new EmployeeDto(firstName, lastName);
//                employees.add(employeeDTO);
//            }
//
//            return employees;
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
        return Collections.emptyList();
    }

    public PagedModel<EntityModel<EmployeeDto>> findAllEmployees(Pageable pageable) {
        logger.info("finding all employee...");

        var employeePage = repository.findAll(pageable);

        var employeeDtoPage = employeePage.map(p -> DozerMapper.parseObject(p, EmployeeDto.class));
        employeeDtoPage.map(
                p -> p.add(
                        linkTo(methodOn(EmployeeController.class)
                                .findEmployeeById(p.getEmployeeId())).withSelfRel()));

        Link link = linkTo(
                methodOn(EmployeeController.class)
                        .findAllEmployees(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(employeeDtoPage, link);
    }

    public PagedModel<EntityModel<EmployeeDto>> findEmployeeByName(String firstname, Pageable pageable) {

        logger.info("finding all employee...");

        var employeePage = repository.findEmployeeByName(firstname, pageable);

        var employeeDtoPage = employeePage.map(p -> DozerMapper.parseObject(p, EmployeeDto.class));
        employeeDtoPage.map(
                p -> p.add(
                        linkTo(methodOn(EmployeeController.class)
                                .findEmployeeById(p.getEmployeeId())).withSelfRel()));

        Link link = linkTo(
                methodOn(EmployeeController.class)
                        .findAllEmployees(pageable.getPageNumber(),
                                pageable.getPageSize(),
                                "asc")).withSelfRel();

        return assembler.toModel(employeeDtoPage, link);
    }

    public EmployeeDto findEmployeeById(Long id) {
        logger.info("finding one employee...");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, EmployeeDto.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(id)).withSelfRel());
        return dto;
    }

    public EmployeeDto createEmployee(EmployeeDto employee) {
        if (employee == null) throw new RequiredObjectIsNullException();
        logger.info("creating employee...");

        var entity = DozerMapper.parseObject(employee, Employee.class);
        var dto = DozerMapper.parseObject(repository.save(entity), EmployeeDto.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(dto.getEmployeeId())).withSelfRel());
        return dto;
    }

    public EmployeeDto updateEmployee(EmployeeDto employee) {
        if (employee == null) throw new RequiredObjectIsNullException();
        logger.info("updating employee...");

        var entity = repository.findById(employee.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(employee.getFirstName());
        entity.setLastName(employee.getLastName());
        entity.setRole(employee.getRole());

        var updatedEmployee = DozerMapper.parseObject(repository.save(entity), EmployeeDto.class);
        updatedEmployee.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(updatedEmployee.getEmployeeId())).withSelfRel());
        return updatedEmployee;
    }

    @Transactional
    public EmployeeDto disableEmployee(Long id) {

        logger.info("disabling one employee...");

        repository.disableEmployee(id);

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto = DozerMapper.parseObject(entity, EmployeeDto.class);
        dto.add(linkTo(methodOn(EmployeeController.class).findEmployeeById(id)).withSelfRel());
        return dto;
    }

    public void deleteEmployee(Long id) {
        logger.info("deleting employee...");
        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}