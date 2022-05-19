package by.constructioncompany.dao.jdbc.user;

import by.constructioncompany.dao.PersonDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.query_constant.UserQueryConstant;
import by.constructioncompany.entity.person.user.User;
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
public class JdbcUserDao implements PersonDao {

    @Autowired
    private UserFieldsSetter userFieldsSetter;


    @Override
    public boolean save(Object user) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.INSERT_QUERY)) {
            userFieldsSetter.setUserFieldsForPrep(prep, (User) user);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object user, String email) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.UPDATE_QUERY)) {
            userFieldsSetter.setUserFieldsForPrep(prep, (User) user);
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

    public boolean deleteById(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.DELETE_BY_ID_QUERY)) {
            prep.setInt(1, id);
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
            return getUser(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findById (int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getUser(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findPersons() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(UserQueryConstant.FIND_USERS_QUERY)) {
            ResultSet rs = prep.executeQuery();
            return getUsers(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    private Optional<List<?>> getUsers(ResultSet rs) throws SQLException {
        List<User> users = new ArrayList<>();
        while(rs.next()) {
            User user = new User();
            userFieldsSetter.setUserFields(rs, user);
            users.add(user);
        }
        return Optional.ofNullable(users);
    }
    private Optional<User> getUser(ResultSet rs) throws SQLException {
        User user = new User();
        while (rs.next()) {
            userFieldsSetter.setUserFields(rs, user);
        }
        return Optional.ofNullable(user);
    }

}
