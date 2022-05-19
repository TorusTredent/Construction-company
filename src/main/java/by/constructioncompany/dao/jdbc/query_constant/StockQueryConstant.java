package by.constructioncompany.dao.jdbc.query_constant;

public class StockQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO stock (name, unit_price, count_of_materials, purchase_date, supplier_id) " +
            "VALUES(?, ?, ?, ?, ?)";
    public static final String UPDATE_QUERY = "UPDATE stock SET name =?, unit_price =?, count_of_materials =? WHERE id =?";

    public static final String DELETE_QUERY = "DELETE FROM stock WHERE id =?";
    public static final String DELETE_BY_SUPPLIER_QUERY = "DELETE FROM stock WHERE supplier_id =?";

    public static final String EXIST_BY_NAME_AND_DATE = "SELECT * FROM stock WHERE name =? AND purchase_date =?";
    public static final String FIND_BY_ID = "SELECT *\n" +
            "FROM stock\n" +
            "         LEFT JOIN suppliers AS sup ON sup.id = stock.supplier_id\n" +
            "WHERE stock.id =?\n";
    public static final String FIND_ALL = "SELECT * FROM stock LEFT JOIN suppliers sup on sup.id = supplier_id";
    public static final String FIND_BY_NAME_AND_DATE = "SELECT * FROM stock LEFT JOIN suppliers sup ON " +
            "sup.id = stock.supplier_id WHERE stock.name =? AND stock.purchase_date =?";

    public static final String FIND_BY_ORDER_ID = "SELECT *\n" +
            "FROM orders_stocks\n" +
            "         LEFT JOIN stock s on orders_stocks.stock_id = s.id\n" +
            "WHERE order_id = ?";

    public static final String FIND_ALL_BY_ORDER_STATUS = "SELECT * FROM stock LEFT JOIN" +
            " suppliers sup on sup.id = supplier_id WHERE order_status =?";


    public static final String UNION = "SELECT orders.address\n" +
            "FROM orders\n" +
            "UNION\n" +
            "SELECT users.email\n" +
            "FROM users";

    public static final String ITOGOVY = "SELECT stock.id,\n" +
            "       stock.name,\n" +
            "       suppliers.name, suppliers.rating,\n" +
            "       SUM(stock.unit_price * stock.count_of_materials) AS cost\n" +
            "FROM stock\n" +
            "LEFT JOIN suppliers ON stock.supplier_id = suppliers.id\n" +
            "WHERE stock.purchase_date >= ? AND stock.purchase_date < ?\n" +
            "GROUP BY stock.name, stock.id, suppliers.name, suppliers.rating";
    public static final String FIND_ALL_BY_SUPPLIER_ID = "SELECT * FROM stock LEFT JOIN suppliers sup on sup.id = supplier_id WHERE supplier_id =?";


    public static final String UPDATE_MATERIAL_COUNTER_IN_ORDER = "UPDATE orders_stocks SET count =? WHERE" +
            " stock_id =? AND order_id =?";

    public static final String FIND_COUNT_BY_MATERIAL_ID_AND_ORDER_ID = "SELECT count FROM orders_stocks WHERE" +
            " stock_id =? AND order_id =?";

    public static final String DELETE_MATERIAL_FROM_ORDER = "DELETE FROM orders_stocks WHERE stock_id =? AND order_id =?";

    public static final String INSERT_MATERIAL_FOR_ORDER = "INSERT INTO orders_stocks (order_id, stock_id, count) " +
            "VALUES (?, ?, ?)";

    public static final String EXIST_MATERIAL_IN_ORDER = "SELECT * FROM orders_stocks WHERE order_id =? AND stock_id =?";

    public static final String EXIST_BY_NAME = "SELECT * FROM stock WHERE name =?";
}
