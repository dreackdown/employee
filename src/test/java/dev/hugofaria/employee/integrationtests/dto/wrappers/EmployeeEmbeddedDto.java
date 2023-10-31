package dev.hugofaria.employee.integrationtests.dto.wrappers;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.hugofaria.employee.integrationtests.dto.EmployeeDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeEmbeddedDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("employeeDtoList")
    private List<EmployeeDTO> employees;
}