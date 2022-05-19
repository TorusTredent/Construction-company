package by.constructioncompany.dto.employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UpdateEmployeeDto {

    private int id;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String speciality;
}
