package by.constructioncompany.dao.jdbc.employee;

import by.constructioncompany.entity.person.employee.Employee;
import by.constructioncompany.entity.person.employee.EmployeeRole;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class EmployeeFieldsSetter {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String SPECIALITY = "speciality";
    private static final String EMPLOYEE_ROLE = "employee_role";


    public void setEmployeeFieldsForPrep(PreparedStatement prep, Employee employee) throws SQLException {
        prep.setString(1, employee.getPassword());
        prep.setString(2, employee.getFirstName());
        prep.setString(3, employee.getLastName());
        prep.setString(4, employee.getPhoneNumber());
        prep.setString(5, employee.getEmail());
        prep.setString(6, employee.getSpeciality());
        prep.setString(7, String.valueOf(employee.getEmployeeRole()));
    }

    public void setEmployeeFields(ResultSet rs, Employee employee) throws SQLException {
        employee.setId(rs.getInt(ID));
        employee.setEmail(rs.getString(EMAIL));
        employee.setPassword(rs.getString(PASSWORD));
        employee.setFirstName(rs.getString(FIRST_NAME));
        employee.setLastName(rs.getString(LAST_NAME));
        employee.setPhoneNumber(rs.getString(PHONE_NUMBER));
        employee.setSpeciality(rs.getString(SPECIALITY));
        employee.setEmployeeRole(EmployeeRole.valueOf(rs.getString(EMPLOYEE_ROLE)));
    }
}
