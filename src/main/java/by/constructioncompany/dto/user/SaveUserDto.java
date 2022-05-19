package by.constructioncompany.dto.user;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveUserDto {

    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private String email;
}
