package dev.hugofaria.employeeapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class EmployeeDTO extends RepresentationModel<EmployeeDTO> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String role;

    public EmployeeDTO(@JsonProperty("first") String firstName, @JsonProperty("last") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}