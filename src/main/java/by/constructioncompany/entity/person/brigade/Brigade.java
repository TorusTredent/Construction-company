package by.constructioncompany.entity.person.brigade;

import by.constructioncompany.entity.person.employee.Employee;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brigade {

    private int id;
    private double salary;
    private List<Employee> employees;
    private BrigadeStatus brigadeStatus;


}
