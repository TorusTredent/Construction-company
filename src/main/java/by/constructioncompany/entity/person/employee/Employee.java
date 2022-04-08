package by.constructioncompany.entity.person.employee;

import by.constructioncompany.entity.person.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.codec.DataBufferEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {

    private LocalDate dateOfBirth;
    private String speciality;
    private EmployeeRole employeeRole;
}
