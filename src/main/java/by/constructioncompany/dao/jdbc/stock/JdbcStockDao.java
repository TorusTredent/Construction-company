package by.constructioncompany.dao.jdbc.stock;

import by.constructioncompany.dao.ConstructionFirmDao;
import by.constructioncompany.dao.StockDao;
import by.constructioncompany.dao.jdbc.configuration.JdbcPostgresSQLConnection;
import by.constructioncompany.dao.jdbc.query_constant.StockQueryConstant;
import by.constructioncompany.dto.iosu.ItogovyDto;
import by.constructioncompany.entity.stock.Material;
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
public class JdbcStockDao implements ConstructionFirmDao, StockDao {

    @Autowired
    private MaterialFieldsSetter materialFieldsSetter;


    @Override
    public boolean save(Object stock) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.INSERT_QUERY)) {
            materialFieldsSetter.setMaterialFieldsForSave(prep, (Material) stock);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Object stock, int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.UPDATE_QUERY)) {
            materialFieldsSetter.setMaterialFieldsForUpdate(prep, (Material) stock);
            prep.setInt(4, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.DELETE_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteBySupplierId(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.DELETE_BY_SUPPLIER_QUERY)) {
            prep.setInt(1, id);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }


    @Override
    public Optional<?> findById(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.FIND_BY_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getMaterial(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public Optional<List<?>> findAll() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.FIND_ALL)) {
            ResultSet rs = prep.executeQuery();
            return getMaterials(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public boolean existByNameAndDate(String name, LocalDate date) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.EXIST_BY_NAME_AND_DATE)) {
            prep.setString(1, name);
            prep.setDate(2, Date.valueOf(date));
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Material> findByNameAndDate(String name, LocalDate date) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.FIND_BY_NAME_AND_DATE)) {
            prep.setString(1, name);
            prep.setDate(2, Date.valueOf(date));
            ResultSet rs = prep.executeQuery();
            return getMaterial(rs);
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<?>> findBySupplierId(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.FIND_ALL_BY_SUPPLIER_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getMaterials(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<List<Material>> findAllByOrderId(int id) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.FIND_BY_ORDER_ID)) {
            prep.setInt(1, id);
            ResultSet rs = prep.executeQuery();
            return getMaterialsByOrderId(rs);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return Optional.empty();
    }


    public List<String> union() {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.UNION)) {
            ResultSet rs = prep.executeQuery();
            List<String> union = new ArrayList<>();
            while (rs.next()) {
                union.add(rs.getString(1));
            }
            return union;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public List<ItogovyDto> itogovy(LocalDate beginDate, LocalDate endDate) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.ITOGOVY)) {
            prep.setDate(1, Date.valueOf(beginDate));
            prep.setDate(2, Date.valueOf(endDate));
            ResultSet rs = prep.executeQuery();
            List<ItogovyDto> itogovy = new ArrayList<>();
            while (rs.next()) {
                itogovy.add(new ItogovyDto(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getDouble(4), rs.getDouble(5)));
            }
            return itogovy;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean updateMaterialCounterInOrder(int count, int materialId, int orderId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.UPDATE_MATERIAL_COUNTER_IN_ORDER)) {
            prep.setInt(1, count);
            prep.setInt(2, materialId);
            prep.setInt(3, orderId);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteMaterialFromOrder(int materialId, int orderId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.DELETE_MATERIAL_FROM_ORDER)) {
            prep.setInt(1, materialId);
            prep.setInt(2, orderId);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int findCountByMaterialIdAndOrderId(int materialId, int orderId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.FIND_COUNT_BY_MATERIAL_ID_AND_ORDER_ID)) {
            prep.setInt(1, materialId);
            prep.setInt(2, orderId);
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException throwables) {
            log.warn(throwables.toString());
            throwables.printStackTrace();
        }
        return 0;
    }

    public boolean insertMaterialForOrder(int orderId, int materialId, int count) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.INSERT_MATERIAL_FOR_ORDER)) {
            prep.setInt(1, orderId);
            prep.setInt(2, materialId);
            prep.setInt(3, count);
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean existMaterialInOrder(int orderId, int materialId) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.EXIST_MATERIAL_IN_ORDER)) {
            prep.setInt(1, orderId);
            prep.setInt(2, materialId);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean existByName(String name) {
        try (Connection con = JdbcPostgresSQLConnection.connect();
             PreparedStatement prep = con.prepareStatement(StockQueryConstant.EXIST_BY_NAME)) {
            prep.setString(1, name);
            ResultSet rs = prep.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private Optional<List<Material>> getMaterialsByOrderId(ResultSet rs) throws SQLException {
        List<Material> materials = new ArrayList<>();
        while (rs.next()) {
            Material material = new Material();
            material.setCountOfMaterials(rs.getInt(3));
            material.setId(rs.getInt(4));
            material.setName(rs.getString(5));
            material.setUnitPrice(rs.getDouble(6));
            material.setPurchaseDate(LocalDate.parse(rs.getString(8)));
            material.getSupplier().setId(rs.getInt(9));
            materials.add(material);
        }
        return Optional.ofNullable(materials);
    }

    private Optional<List<?>> getMaterials(ResultSet rs) throws SQLException {
        List<Material> materials = new ArrayList<>();
        while (rs.next()) {
            Material material = new Material();
            materialFieldsSetter.setMaterialFields(rs, material);
            materials.add(material);
        }
        return Optional.ofNullable(materials);
    }

    private Optional<Material> getMaterial(ResultSet rs) throws SQLException {
        Material material = new Material();
        while (rs.next()) {
            materialFieldsSetter.setMaterialFields(rs, material);
        }
        return Optional.of(material);
    }

}
