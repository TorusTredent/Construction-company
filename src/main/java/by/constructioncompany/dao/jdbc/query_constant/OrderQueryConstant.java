package by.constructioncompany.dao.jdbc.query_constant;

public class OrderQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO orders (address, beginning_of_work, end_of_work, brigade_id," +
            " user_id, order_status, construct_object_id) VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_QUERY = "UPDATE orders SET address =?, beginning_of_work =?, " +
            "end_of_work =?, brigade_id =?, user_id =?, order_status =?, construct_object_id =? WHERE id =?";

    public static final String DELETE_ORDERS_STOCKS_QUERY = "DELETE FROM orders_stocks WHERE order_id =?";

    public static final String DELETE_QUERY = "DELETE FROM orders WHERE id =?";

    public static final String DELETE_BY_USER_ID = "DELETE FROM orders WHERE user_id =?";


    public static final String FIND_ALL_BY_ORDER_STATUS = "SELECT * FROM orders WHERE order_status =?";

    public static final String FIND_ALL_BY_DATES = "SELECT *\n" +
            "FROM orders\n" +
            "WHERE orders.beginning_of_work >= ? AND orders.end_of_work < ?";

    public static final String FIND_ALL = "SELECT * FROM orders";

    public static final String FIND_BY_ID = "SELECT *\n" +
            "FROM orders\n" +
            "         INNER JOIN users us on us.id = orders.user_id\n" +
            "         INNER JOIN construct_objects co ON orders.construct_object_id = co.id\n" +
            "         LEFT JOIN brigades ON orders.brigade_id = brigades.id\n" +
            "         LEFT JOIN brigades_employees br ON brigades.id = br.brigade_id\n" +
            "         LEFT JOIN employees ON br.employee_id = employees.id\n" +
            "WHERE orders.id = ?";

    public static final String FIND_ALL_BY_USER_ID = "SELECT * FROM orders WHERE user_id =?";
    public static final String FIND_ALL_BY_BRIGADE_ID = "SELECT *\n" +
            "FROM orders\n" +
            "         INNER JOIN users us on us.id = orders.user_id\n" +
            "         INNER JOIN construct_objects co ON orders.construct_object_id = co.id\n" +
            "         LEFT JOIN brigades ON orders.brigade_id = brigades.id\n" +
            "         LEFT JOIN brigades_employees br ON brigades.id = br.brigade_id\n" +
            "         LEFT JOIN employees ON br.employee_id = employees.id\n" +
            "WHERE brigades.id = ?";




    public static final String GET_COUNT_OF_ORDERS_WITH_IN_PROGRESS_STATUS = "SELECT COUNT(*) AS order_count FROM orders GROUP BY orders.order_status HAVING  order_status = 'IN_PROGRESS'";

    public static final String SET_ORDER_STATUS = "UPDATE orders SET order_status =? WHERE id =?";


}
