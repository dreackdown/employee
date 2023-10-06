package dev.hugofaria.employeeapi.dto;

import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;

@Data
public class EmployeeDTO extends RepresentationModel<EmployeeDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String role;

}