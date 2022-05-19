package by.constructioncompany.entity.person.employee;

import by.constructioncompany.entity.person.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends Person {

    private String speciality;
    private EmployeeRole employeeRole;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employee employee = (Employee) o;
        return Objects.equals(speciality, employee.speciality) && employeeRole == employee.employeeRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speciality, employeeRole);
    }
}
