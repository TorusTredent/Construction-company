package by.constructioncompany.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String email;
}
