package by.constructioncompany.dto.employee;

import by.constructioncompany.entity.person.employee.EmployeeRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveEmployeeDto {

    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String speciality;
    private EmployeeRole employeeRole;
}
