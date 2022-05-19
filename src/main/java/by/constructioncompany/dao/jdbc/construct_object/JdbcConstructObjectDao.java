package by.constructioncompany.dao.jdbc.construct_object;

import by.constructioncompany.dao.ConstructionFirmDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.query_constant.ConstructObjectQueryConstant;
import by.constructioncompany.dao.jdbc.query_constant.UserQueryConstant;
import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.ConstructObjectType;
import by.constructioncompany.entity.person.user.User;
import lombok.extern.slf4j.Slf4j;
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
public class JdbcConstructObjectDao implements ConstructionFirmDao {


    @Override
    public boolean save(Object object) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.INSERT_QUERY)) {
            ConstructObject constructObject = (ConstructObject) object;
            prep.setDouble(1, constructObject.getSquare());
            prep.setString(2, String.valueOf(constructObject.getConstructObjectType()));
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object object, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.UPDATE_QUERY)) {
            prep.setDouble(1, ((ConstructObject) object).getSquare());
            prep.setString(2, String.valueOf(((ConstructObject) object).getConstructObjectType()));
            prep.setInt(3, ((ConstructObject) object).getId());
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
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.DELETE_QUERY)) {
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
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getConstructObject(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findAll() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.FIND_ALL)) {
            ResultSet rs = prep.executeQuery();
            return getAll(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<ConstructObject> findBySquareAndType(ConstructObjectType type, double square) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.FIND_BY_TYPE_AND_SQUARE)) {
            prep.setString(1,  String.valueOf(type));
            prep.setDouble(2, square);
            ResultSet rs = prep.executeQuery();
            return getConstructObject(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean existBySquareAndType(double square, ConstructObjectType type) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.FIND_BY_TYPE_AND_SQUARE)) {
            prep.setString(1, String.valueOf(type));
            prep.setDouble(2, square);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean deleteBySquareAndType(double square, ConstructObjectType type) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(ConstructObjectQueryConstant.DELETE_BY_TYPE_AND_SQUARE)) {
            prep.setString(1, String.valueOf(type));
            prep.setDouble(2, square);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    private Optional<List<?>> getAll(ResultSet rs) throws SQLException {
        List<ConstructObject> constructObjects = new ArrayList<>();
        while (rs.next()) {
            ConstructObject constructObject = new ConstructObject();
            constructObject.setId(rs.getInt(1));
            constructObject.setSquare(rs.getDouble(2));
            constructObject.setConstructObjectType(ConstructObjectType.valueOf(rs.getString(3)));
            constructObjects.add(constructObject);
        }
        return Optional.of(constructObjects);
    }

    private Optional<ConstructObject> getConstructObject(ResultSet rs) throws SQLException {
        ConstructObject constructObject = new ConstructObject();
        while (rs.next()) {
            constructObject.setId(rs.getInt(1));
            constructObject.setSquare(rs.getDouble(2));
            constructObject.setConstructObjectType(ConstructObjectType.valueOf(rs.getString(3)));
        }
        return Optional.of(constructObject);
    }

}
