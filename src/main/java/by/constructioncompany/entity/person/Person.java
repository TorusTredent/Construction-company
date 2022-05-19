package by.constructioncompany.entity.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {

    private int id;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
