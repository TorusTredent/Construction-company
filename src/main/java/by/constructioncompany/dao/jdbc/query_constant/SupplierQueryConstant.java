package by.constructioncompany.dao.jdbc.query_constant;

public class SupplierQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO suppliers (name, rating) " +
            "VALUES(?, ?)";
    public static final String EXIST_BY_NAME = "SELECT * FROM suppliers WHERE name =?";

    public static final String EXIST_SUPPLIER_IN_STOCK = "SELECT * FROM stock WHERE supplier_id =?";

    public static final String FIND_ALL = "SELECT * FROM suppliers";
    public static final String FIND_BY_NAME = "SELECT * FROM suppliers WHERE name =?";

    public static final String UPDATE_QUERY = "UPDATE suppliers SET name =?, rating =? WHERE id =?";

    public static final String DELETE_QUERY = "DELETE FROM suppliers WHERE id=?";

    public static final String FIND_BY_ID = "SELECT * FROM suppliers WHERE id =?";

    public static final String PEREKREST = "SELECT suppliers.name,\n" +
            "       SUM(case\n" +
            "               when stock.purchase_date > '2019-01-01' and stock.purchase_date < '2019-12-31'\n" +
            "                   then stock.count_of_materials\n" +
            "               else 0 end) as \"2019\",\n" +
            "       SUM(case\n" +
            "               when stock.purchase_date > '2020-01-01' and stock.purchase_date < '2020-12-31'\n" +
            "                   then stock.count_of_materials\n" +
            "               else 0 end) as \"2020\",\n" +
            "       SUM(case\n" +
            "               when stock.purchase_date > '2021-01-01' and stock.purchase_date < '2021-12-31'\n" +
            "                   then stock.count_of_materials\n" +
            "               else 0 end) as \"2021\",\n" +
            "       SUM(case\n" +
            "               when stock.purchase_date > '2022-01-01' and stock.purchase_date < '2022-12-31'\n" +
            "                   then stock.count_of_materials\n" +
            "               else 0 end) as \"2022\"\n" +
            "FROM stock\n" +
            "         LEFT JOIN suppliers on suppliers.id = stock.supplier_id\n" +
            "GROUP by suppliers.name\n" +
            "ORDER BY suppliers.name";

}
