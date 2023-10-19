package dev.hugofaria.employeeapi.unittests.mapper;

import dev.hugofaria.employeeapi.dto.v1.EmployeeDto;
import dev.hugofaria.employeeapi.entity.Employee;
import dev.hugofaria.employeeapi.mapper.DozerMapper;
import dev.hugofaria.employeeapi.unittests.mapper.mocks.MockEmployee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DozerConverterTest {

    MockEmployee inputObject;

    @BeforeEach
    public void setUp() {
        inputObject = new MockEmployee();
    }

    @Test
    public void parseEntityToDTOTest() {
        EmployeeDto output = DozerMapper.parseObject(inputObject.mockEntity(), EmployeeDto.class);
        assertEquals(Long.valueOf(0L), output.getEmployeeId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Role Test0", output.getRole());
    }

    @Test
    public void parseEntityListToDTOListTest() {
        List<EmployeeDto> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), EmployeeDto.class);
        EmployeeDto outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getEmployeeId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Role Test0", outputZero.getRole());

        EmployeeDto outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getEmployeeId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Role Test7", outputSeven.getRole());

        EmployeeDto outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getEmployeeId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Role Test12", outputTwelve.getRole());
    }

    @Test
    public void parseDTOToEntityTest() {
        Employee output = DozerMapper.parseObject(inputObject.mockDto(), Employee.class);
        assertEquals(Long.valueOf(0L), output.getEmployeeId());
        assertEquals("First Name Test0", output.getFirstName());
        assertEquals("Last Name Test0", output.getLastName());
        assertEquals("Role Test0", output.getRole());
    }

    @Test
    public void parserDTOListToEntityListTest() {
        List<Employee> outputList = DozerMapper.parseListObjects(inputObject.mockDTOList(), Employee.class);
        Employee outputZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), outputZero.getEmployeeId());
        assertEquals("First Name Test0", outputZero.getFirstName());
        assertEquals("Last Name Test0", outputZero.getLastName());
        assertEquals("Role Test0", outputZero.getRole());

        Employee outputSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), outputSeven.getEmployeeId());
        assertEquals("First Name Test7", outputSeven.getFirstName());
        assertEquals("Last Name Test7", outputSeven.getLastName());
        assertEquals("Role Test7", outputSeven.getRole());

        Employee outputTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), outputTwelve.getEmployeeId());
        assertEquals("First Name Test12", outputTwelve.getFirstName());
        assertEquals("Last Name Test12", outputTwelve.getLastName());
        assertEquals("Role Test12", outputTwelve.getRole());
    }
}