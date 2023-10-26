package dev.hugofaria.employee.dto.v1;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@Setter
@Getter
public class EmployeeDto extends RepresentationModel<EmployeeDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String role;
    private Boolean enabled;
//
//    public EmployeeDto(@JsonProperty("first") String firstName, @JsonProperty("last") String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
}