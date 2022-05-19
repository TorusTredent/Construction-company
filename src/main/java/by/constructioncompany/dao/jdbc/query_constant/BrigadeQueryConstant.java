package by.constructioncompany.dao.jdbc.query_constant;

public class BrigadeQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO brigades (salary, brigade_status) VALUES(?, ?)";
    public static final String UPDATE_QUERY = "UPDATE brigades SET salary =?, brigade_status =? WHERE id =?";
    public static final String DELETE_QUERY = "DELETE FROM brigades WHERE id =?";
    public static final String DELETE_EMPLOYEE_BY_ID = "DELETE FROM brigades_employees WHERE employee_id =? AND brigade_id =?";

    public static final String FIND_BY_ID = "SELECT * FROM brigades WHERE id =?";
    public static final String FIND_ALL = "SELECT * FROM brigades";
    public static final String INSERT_EMPLOYEE = "INSERT INTO brigades_employees (brigade_id, employee_id) VALUES(?, ?)";
    public static final String DELETE_EMPLOYEE = "DELETE FROM brigades_employees WHERE brigade_id =? AND employee_id =?";
    public static final String FIND_BRIGADE_WITH_EMPLOYEE_BY_BRIGADE_ID =
            "SELECT *\n" +
                    "FROM brigades\n" +
                    "         LEFT JOIN brigades_employees ON brigades.id = brigades_employees.brigade_id\n" +
                    "         LEFT JOIN employees ON brigades_employees.employee_id = employees.id\n" +
                    "WHERE brigades.id =?\n";

    public static final String FIND_ALL_BRIGADES_NUMBERS = "SELECT id FROM brigades";
    public static final String GET_COUNT_OF_BRIGADE_BY_ID = "SELECT COUNT(*) AS brigade_count FROM orders WHERE brigade_id =?";


}
