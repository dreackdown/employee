package dev.hugofaria.employee.integrationtests.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EmployeeDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean enabled;
}