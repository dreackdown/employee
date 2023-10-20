package dev.hugofaria.employee.dto.v1;

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
public class EmployeeDto extends RepresentationModel<EmployeeDto> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long employeeId;
    private String firstName;
    private String lastName;
    private String role;
//
//    public EmployeeDto(@JsonProperty("first") String firstName, @JsonProperty("last") String lastName) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//    }
}