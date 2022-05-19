package by.constructioncompany.dao.jdbc.brigade;

import by.constructioncompany.dao.BrigadeDao;
import by.constructioncompany.dao.ConstructionFirmDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.employee.EmployeeFieldsSetter;
import by.constructioncompany.dao.jdbc.query_constant.BrigadeQueryConstant;
import by.constructioncompany.dao.jdbc.query_constant.OrderQueryConstant;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.employee.Employee;
import lombok.extern.slf4j.Slf4j;
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
public class JdbcBrigadeDao implements ConstructionFirmDao, BrigadeDao {

    @Autowired
    private BrigadeFieldsSetter brigadeFieldsSetter;

    @Autowired
    private EmployeeFieldsSetter employeeFieldsSetter;

    @Override
    public boolean save(Object brigade) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.INSERT_QUERY)) {
            brigadeFieldsSetter.setBrigadeFieldsForPrep(prep, (Brigade) brigade);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object brigade, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.UPDATE_QUERY)) {
            brigadeFieldsSetter.setBrigadeFieldsForPrep(prep, (Brigade) brigade);
            prep.setInt(3, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.DELETE_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Brigade> findById(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getById(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findAll() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.FIND_ALL)) {
            ResultSet rs = prep.executeQuery();
            return getBrigadesWithoutEmployee(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean addEmployee(int brigadeId, int employeeId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.INSERT_EMPLOYEE)) {
            prep.setInt(1, brigadeId);
            prep.setInt(2, employeeId);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteEmployee(int brigadeId, int employeeId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.DELETE_EMPLOYEE)) {
            brigadeFieldsSetter.setIdsForQuery(prep, brigadeId, employeeId);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<List<Integer>> findAllBrigadesNumbers() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.FIND_ALL_BRIGADES_NUMBERS)) {
            ResultSet rs = prep.executeQuery();
            List<Integer> numbers = new ArrayList<>();
            while (rs.next()) {
                numbers.add(rs.getInt(1));
            }
            return Optional.of(numbers);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public int getBrigadeOrderCounterByBrigadeId(int brigadeId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.GET_COUNT_OF_BRIGADE_BY_ID)) {
            prep.setInt(1, brigadeId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt("brigade_count");
            }
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return 0;
    }

    public Optional<Brigade> findBrigadeWithEmployeeByBrigadeId(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.FIND_BRIGADE_WITH_EMPLOYEE_BY_BRIGADE_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getBrigade(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean deleteEmployeeById(int brigadeId, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(BrigadeQueryConstant.DELETE_EMPLOYEE_BY_ID)) {
            prep.setInt(1, id);
            prep.setInt(2, brigadeId);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Optional<List<?>> getBrigadesWithoutEmployee(ResultSet rs) throws SQLException {
        List<Brigade> brigades = new ArrayList<>();
        while (rs.next()) {
            Brigade brigade = new Brigade();
            brigadeFieldsSetter.setBrigadeFields(rs, brigade);
            brigades.add(brigade);
        }
        return Optional.ofNullable(brigades);
    }

    private Optional<Brigade> getBrigade(ResultSet rs) throws SQLException {
        Brigade brigade = new Brigade();
        while (rs.next()) {
            brigadeFieldsSetter.setBrigadeFields(rs, brigade);
            List<Employee> employees = getEmployees(rs).orElse(null);
            brigade.setEmployees(employees);
        }
        return Optional.of(brigade);
    }
    private Optional<List<?>> getBrigades(ResultSet rs) throws SQLException {
        List<Brigade> brigades = new ArrayList<>();
        while (rs.next()) {
            Brigade brigade = new Brigade();
            brigadeFieldsSetter.setBrigadeFields(rs, brigade);
            List<Employee> employees = getEmployees(rs).orElse(null);
            brigade.setEmployees(employees);
            brigades.add(brigade);
        }
        return Optional.ofNullable(brigades);
    }


    private Optional<Brigade> getById(ResultSet rs) throws SQLException {
        Brigade brigade = new Brigade();
        while (rs.next()) {
            brigadeFieldsSetter.setBrigadeFields(rs, brigade);
        }
        return Optional.ofNullable(brigade);
    }

    private Optional<List<Employee>> getEmployees(ResultSet rs) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        setEmployeeFields(rs, employee);
        employees.add(employee);
        while (rs.next()) {
            employee = new Employee();
            setEmployeeFields(rs, employee);
            employees.add(employee);
        }
        return Optional.ofNullable(employees);
    }

    public void setEmployeeFields(ResultSet rs, Employee employee) throws SQLException {
        employee.setId(rs.getInt(6));
        employee.setPassword(rs.getString(7));
        employee.setFirstName(rs.getString(8));
        employee.setLastName(rs.getString(9));
        employee.setPhoneNumber(rs.getString(10));
        employee.setEmail(rs.getString(11));
        employee.setSpeciality(rs.getString(12));
    }

}
