package by.constructioncompany.dao.jdbc;

import by.constructioncompany.dao.UserDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.query_constant.UserQueryConstant;
import by.constructioncompany.entity.person.user.User;
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
public class JdbcUserDao implements UserDao {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String DISCOUNT = "discount";


    @Autowired
    private ModelMapper mapper;
    @Override
    public boolean save(User user) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.INSERT_QUERY)) {
            setUserFieldsForPrep(prep, user);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(User user, String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.UPDATE_QUERY)) {
            setUserFieldsForPrep(prep, user);
            prep.setString(7, email);
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
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.DELETE_QUERY)) {
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
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.EXIST_BY_EMAIL_QUERY)) {
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
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.EXIST_BY_PHONE_NUMBER_QUERY)) {
            prep.setString(1, phoneNumber);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.FIND_BY_EMAIL)) {
            prep.setString(1, email);
            ResultSet rs = prep.executeQuery();
            return getByEmail(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> findUsers() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.FIND_USERS_QUERY)) {
            ResultSet rs = prep.executeQuery();
            return getUsers(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    private Optional<List<User>> getUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while(rs.next()) {
            User user = new User();
            setUserFields(rs, user);
            users.add(user);
        }
        return Optional.ofNullable(users);
    }
    private Optional<User> getByEmail(ResultSet rs) throws SQLException {
        User user = new User();
        while (rs.next()) {
           setUserFields(rs, user);
        }
        return Optional.ofNullable(user);
    }
    private void setUserFieldsForPrep(PreparedStatement prep, User user) throws SQLException {
        prep.setString(1, user.getPassword());
        prep.setString(2, user.getFirstName());
        prep.setString(3, user.getLastName());
        prep.setString(4, user.getPhoneNumber());
        prep.setString(5, user.getEmail());
        prep.setInt(6, user.getDiscount());
    }

    private void setUserFields(ResultSet rs, User user) throws SQLException {
        user.setId(rs.getInt(ID));
        user.setEmail(rs.getString(EMAIL));
        user.setPassword(rs.getString(PASSWORD));
        user.setFirstName(rs.getString(FIRST_NAME));
        user.setLastName(rs.getString(LAST_NAME));
        user.setPhoneNumber(rs.getString(PHONE_NUMBER));
        user.setDiscount(rs.getInt(DISCOUNT));
    }
}
