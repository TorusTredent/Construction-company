package by.constructioncompany.dao.jdbc.supplier;

import by.constructioncompany.dao.ConstructionFirmDao;
import by.constructioncompany.dao.SupplierDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.query_constant.SupplierQueryConstant;
import by.constructioncompany.dto.iosu.PerekrestDto;
import by.constructioncompany.entity.stock.Supplier;
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
public class JdbcSupplierDao implements SupplierDao, ConstructionFirmDao {

    @Override
    public boolean save(Object object) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.INSERT_QUERY)) {
            Supplier supplier = (Supplier) object;
            prep.setString(1, supplier.getName());
            prep.setDouble(2, supplier.getRating());
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object object, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.UPDATE_QUERY)) {
            prep.setString(1, ((Supplier) object).getName());
            prep.setDouble(2, ((Supplier) object).getRating());
            prep.setInt(3, ((Supplier) object).getId());
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
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.DELETE_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean existSupplierInStock(int id ) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.EXIST_SUPPLIER_IN_STOCK)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<?> findById(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getSupplier(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findAll() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.FIND_ALL)) {
            ResultSet rs = prep.executeQuery();
            return getSuppliers(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean existByName(String name) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.EXIST_BY_NAME)) {
            prep.setString(1, name);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Optional<Supplier> findByName (String name) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.FIND_BY_NAME)) {
            prep.setString(1, name);
            ResultSet rs = prep.executeQuery();
            return getSupplier(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<PerekrestDto>> perekrest() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(SupplierQueryConstant.PEREKREST)) {
            ResultSet rs = prep.executeQuery();
            return getPerekrest(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    private Optional<List<PerekrestDto>> getPerekrest(ResultSet rs) throws SQLException {
        List<PerekrestDto> perekrestDtos = new ArrayList<>();
        while (rs.next()) {
            perekrestDtos.add(new PerekrestDto(rs.getString(1), rs.getInt(2),
                    rs.getInt(3), rs.getInt(4), rs.getInt(5)));
        }
        return Optional.of(perekrestDtos);
    }

    private Optional<Supplier> getSupplier (ResultSet rs) throws SQLException {
        Supplier supplier = new Supplier();
        while (rs.next()) {
            supplier.setId(rs.getInt(1));
            supplier.setName(rs.getString(2));
            supplier.setRating(rs.getDouble(3));
        }
        return Optional.of(supplier);
    }

    private Optional<List<?>> getSuppliers (ResultSet rs) throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        while (rs.next()) {
            suppliers.add(new Supplier(rs.getInt(1), rs.getString(2), rs.getDouble(3)));
        }
        return Optional.of(suppliers);
    }
}
