package by.constructioncompany.dao.jdbc.employee;

import by.constructioncompany.dao.PersonDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.query_constant.EmployeeQueryConstant;
import by.constructioncompany.entity.person.employee.Employee;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class JdbcEmployeeDao implements PersonDao {


    @Autowired
    private ModelMapper mapper;

    @Autowired
    private EmployeeFieldsSetter employeeFieldsSetter;

    @Override
    public boolean save(Object employee) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.INSERT_QUERY)) {
            employeeFieldsSetter.setEmployeeFieldsForPrep(prep, (Employee) employee);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object employee, String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.UPDATE_QUERY)) {
            employeeFieldsSetter.setEmployeeFieldsForPrep(prep, (Employee) employee);
            prep.setString(8, email);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.DELETE_QUERY)) {
            prep.setString(1, email);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existByEmail(String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.EXIST_BY_EMAIL_QUERY)) {
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean existByPhoneNumber(String phoneNumber) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.EXIST_BY_PHONE_NUMBER_QUERY)) {
            prep.setString(1, phoneNumber);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Employee> findByEmail(String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.FIND_BY_EMAIL)) {
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            return getEmployee(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<?> findById(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getEmployee(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findPersons() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.FIND_EMPLOYEES_QUERY)) {
            ResultSet rs = prep.executeQuery();
            return getEmployees(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Integer>> findAllEmployeeIdsInBrigades() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.FIND_ALL_EMPLOYEES_ID_IN_BRIGADE)) {
            ResultSet rs = prep.executeQuery();
            return getAllEmployeeIds(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean deleteById (int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(EmployeeQueryConstant.DELETE_BY_ID_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Optional<List<Integer>> getAllEmployeeIds(ResultSet rs) throws SQLException {
        List<Integer> ids = new ArrayList<>();
        while (rs.next()) {
            ids.add(rs.getInt(1));
        }
        return Optional.of(ids);
    }
    private Optional<List<?>> getEmployees(ResultSet rs) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        while(rs.next()) {
            Employee employee = new Employee();
            employeeFieldsSetter.setEmployeeFields(rs, employee);
            employees.add(employee);
        }
        return Optional.ofNullable(employees);
    }
    private Optional<Employee> getEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        while (rs.next()) {
            employeeFieldsSetter.setEmployeeFields(rs, employee);
        }
        return Optional.ofNullable(employee);
    }
}
