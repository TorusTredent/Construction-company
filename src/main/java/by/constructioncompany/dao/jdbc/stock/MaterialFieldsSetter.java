package by.constructioncompany.dao.jdbc.stock;

import by.constructioncompany.entity.stock.Material;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class MaterialFieldsSetter {

    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String UNIT_PRICE = "unit_price";
    private static final String COUNT_OF_MATERIALS = "count_of_materials";
    private static final String PURCHASE_DATE = "purchase_date";
    private static final String SUPPLIER_ID = "sup.id";
    private static final String SUPPLIER_NAME = "sup.name";
    private static final String SUPPLIER_RATING = "sup.rating";


    public void setMaterialFieldsForUpdate(PreparedStatement prep, Material material) throws SQLException {
        prep.setString(1, material.getName());
        prep.setDouble(2, material.getUnitPrice());
        prep.setInt(3, material.getCountOfMaterials());
    }
    public void setMaterialFieldsForSave(PreparedStatement prep, Material material) throws SQLException {
        prep.setString(1, material.getName());
        prep.setDouble(2, material.getUnitPrice());
        prep.setInt(3, material.getCountOfMaterials());
        prep.setDate(4, Date.valueOf(material.getPurchaseDate()));
        prep.setInt(5, material.getSupplier().getId());
    }

    public void setMaterialFields(ResultSet rs, Material material) throws SQLException {
        material.setId(rs.getInt(1));
        material.setName(rs.getString(2));
        material.setUnitPrice(rs.getDouble(3));
        material.setCountOfMaterials(rs.getInt(4));
        material.setPurchaseDate(rs.getObject(5, LocalDate.class));
        material.getSupplier().setId(rs.getInt(7));
        material.getSupplier().setName(rs.getString(8));
        material.getSupplier().setRating(rs.getDouble(9));
    }
}
