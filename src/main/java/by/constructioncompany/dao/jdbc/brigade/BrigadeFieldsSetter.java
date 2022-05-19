package by.constructioncompany.dao.jdbc.brigade;

import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.brigade.BrigadeStatus;
import by.constructioncompany.entity.person.employee.Employee;
import by.constructioncompany.entity.person.employee.EmployeeRole;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BrigadeFieldsSetter {

    private static final String ID = "id";
    private static final String SALARY = "salary";
    private static final String BRIGADE_STATUS = "brigade_status";



    public void setBrigadeFieldsForPrep(PreparedStatement prep, Brigade brigade) throws SQLException {
        prep.setDouble(1, brigade.getSalary());
        prep.setString(2, String.valueOf(brigade.getBrigadeStatus()));
    }

    public void setIdsForQuery(PreparedStatement prep, int brigadeId, int employeeId) throws SQLException {
        prep.setInt(1, brigadeId);
        prep.setInt(2, employeeId);
    }

    public void setBrigadeFields(ResultSet rs, Brigade brigade) throws SQLException {
        brigade.setId(rs.getInt(ID));
        brigade.setSalary(rs.getDouble(SALARY));
        brigade.setBrigadeStatus(BrigadeStatus.valueOf(rs.getString(BRIGADE_STATUS)));
    }
}
