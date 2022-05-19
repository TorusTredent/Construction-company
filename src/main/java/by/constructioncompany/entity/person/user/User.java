package by.constructioncompany.entity.person.user;

import by.constructioncompany.entity.person.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Person {

    private int discount = 0;
    private UserRole userRole = UserRole.USER;
}
