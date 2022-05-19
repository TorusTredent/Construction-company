package by.constructioncompany.dao.jdbc.order;

import by.constructioncompany.dao.ConstructionFirmDao;
import by.constructioncompany.dao.OrderDao;
import by.constructioncompany.dao.jdbc.brigade.BrigadeFieldsSetter;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.construct_object.ConstructObjectFieldsSetter;
import by.constructioncompany.dao.jdbc.employee.EmployeeFieldsSetter;
import by.constructioncompany.dao.jdbc.query_constant.OrderQueryConstant;
import by.constructioncompany.dao.jdbc.user.UserFieldsSetter;
import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.ConstructObjectType;
import by.constructioncompany.entity.order.Order;
import by.constructioncompany.entity.order.OrderStatus;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.brigade.BrigadeStatus;
import by.constructioncompany.entity.person.employee.Employee;
import by.constructioncompany.entity.person.employee.EmployeeRole;
import by.constructioncompany.entity.person.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class JdbcOrderDao implements ConstructionFirmDao, OrderDao {

    @Autowired
    private BrigadeFieldsSetter brigadeFieldsSetter;

    @Autowired
    private UserFieldsSetter userFieldsSetter;

    @Autowired
    private EmployeeFieldsSetter employeeFieldsSetter;
    @Autowired
    private OrderFieldsSetter orderFieldsSetter;

    @Autowired
    private ConstructObjectFieldsSetter constructObjectFieldsSetter;

    @Override
    public boolean save(Object order) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.INSERT_QUERY)) {
            Order newOrder = (Order) order;
            prep.setString(1, newOrder.getAddress());
            prep.setDate(2, Date.valueOf(newOrder.getBeginningOfWork()));
            prep.setDate(3, Date.valueOf(newOrder.getEndOfWork()));
            prep.setInt(4, newOrder.getBrigade().getId());
            prep.setInt(5, newOrder.getUser().getId());
            prep.setString(6, newOrder.getOrderStatus().toString());
            prep.setInt(7, newOrder.getConstructObject().getId());
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object order, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.UPDATE_QUERY)) {
            orderFieldsSetter.setOrderFieldsForSaveAUpdate(prep, (Order) order);
            prep.setInt(8, id);
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
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.DELETE_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean deleteByUserId(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.DELETE_BY_USER_ID)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean deleteOrderStock(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.DELETE_ORDERS_STOCKS_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<?> findById(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getOrder(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findAll() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.FIND_ALL)) {
            ResultSet rs = prep.executeQuery();
            return getAll(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getOrderCounterWithInProgressStatus() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.GET_COUNT_OF_ORDERS_WITH_IN_PROGRESS_STATUS)) {
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt("order_count"));
            }
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean setOrderStatus(OrderStatus orderStatus, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.SET_ORDER_STATUS)) {
            prep.setString(1, orderStatus.toString());
            prep.setInt(2, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return false;
    }

    public Optional<List<?>> findAllByOrderStatus(OrderStatus orderStatus) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.FIND_ALL_BY_ORDER_STATUS)) {
            prep.setString(1, String.valueOf(orderStatus));
            ResultSet rs = prep.executeQuery();
            return getAll(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<?>> findAllByUserId(int userId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.FIND_ALL_BY_USER_ID)) {
            prep.setInt(1,userId);
            ResultSet rs = prep.executeQuery();
            return getAll(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<?>> findAllByDates(LocalDate begin, LocalDate end) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.FIND_ALL_BY_DATES)) {
            prep.setDate(1, Date.valueOf(begin));
            prep.setDate(2, Date.valueOf(end));
            ResultSet rs = prep.executeQuery();
            return getAll(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<?>> findAllByBrigadeId(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(OrderQueryConstant.FIND_ALL_BY_BRIGADE_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getAll(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    private Optional<List<?>> getAll(ResultSet rs) throws SQLException {
        List<Order> orders = new ArrayList<>();
        while (rs.next()) {
            Order order = new Order();
            Brigade brigade = new Brigade();
            User user = new User();
            ConstructObject constructObject = new ConstructObject();
            order.setId(rs.getInt(1));
            order.setAddress(rs.getString(2));
            order.setBeginningOfWork(LocalDate.parse(rs.getString(3)));
            order.setEndOfWork(LocalDate.parse(rs.getString(4)));
            brigade.setId(rs.getInt(5));
            user.setId(rs.getInt(6));
            order.setOrderStatus(OrderStatus.valueOf(rs.getString(7)));
            constructObject.setId(rs.getInt(8));
            order.setBrigade(brigade);
            order.setUser(user);
            order.setConstructObject(constructObject);
            orders.add(order);
        }
        return Optional.of(orders);
    }

    private Optional<Order> getOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        while (rs.next()) {
            Brigade brigade = new Brigade();
            User user = new User();
            ConstructObject constructObject = new ConstructObject();
            orderFieldsSetter.setAllOrderFields(rs, order);
            setUserFields(rs, user);
            setBrigadeFields(rs, brigade);
            setConstrObjectFields(rs, constructObject);
            List<Employee> employees = getEmployees(rs).orElse(null);
            brigade.setEmployees(employees);
            order.setBrigade(brigade);
            order.setUser(user);
            order.setConstructObject(constructObject);
        }
        return Optional.of(order);
    }

    private Optional<List<Employee>> getEmployees(ResultSet rs) throws SQLException {
        List<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            Employee employee = new Employee();
            setEmployeeFields(rs, employee);
            employees.add(employee);
        }
        return Optional.of(employees);
    }


    public void setUserFields(ResultSet rs, User user) throws SQLException {
        user.setId(rs.getInt(9));
        user.setPassword(rs.getString(10));
        user.setFirstName(rs.getString(11));
        user.setLastName(rs.getString(12));
        user.setPhoneNumber(rs.getString(13));
        user.setEmail(rs.getString(14));
        user.setDiscount(rs.getInt(15));
    }

    public void setConstrObjectFields(ResultSet rs, ConstructObject constructObject) throws SQLException {
        constructObject.setId(rs.getInt(16));
        constructObject.setSquare(rs.getInt(17));
        constructObject.setConstructObjectType(ConstructObjectType.valueOf(rs.getString(18)));
    }

    public void setBrigadeFields(ResultSet rs, Brigade brigade) throws SQLException {
        brigade.setId(rs.getInt(19));
        brigade.setSalary(rs.getDouble(20));
        brigade.setBrigadeStatus(BrigadeStatus.valueOf(rs.getString(21)));
    }

    public void setEmployeeFields(ResultSet rs, Employee employee) throws SQLException {
        employee.setId(rs.getInt(24));
        employee.setPassword(rs.getString(25));
        employee.setFirstName(rs.getString(26));
        employee.setLastName(rs.getString(27));
        employee.setPhoneNumber(rs.getString(28));
        employee.setEmail(rs.getString(29));
        employee.setSpeciality(rs.getString(30));
        employee.setEmployeeRole(EmployeeRole.valueOf(rs.getString(31)));
    }


}
