package dev.hugofaria.employeeapi.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;


public class EmployeeDTO extends RepresentationModel<EmployeeDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String role;

    public EmployeeDTO(){    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}