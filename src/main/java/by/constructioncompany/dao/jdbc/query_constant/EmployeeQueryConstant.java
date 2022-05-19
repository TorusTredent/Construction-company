package by.constructioncompany.dao.jdbc.query_constant;

import javax.servlet.http.PushBuilder;

public class EmployeeQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO employees (password, first_name," +
            " last_name, phone_number, email, speciality, employee_role) " +
            " VALUES(?, ?, ?, ?, ?, ?, ?)";
    public static final String UPDATE_QUERY = "UPDATE employees SET password =?, first_name =?," +
            " last_name =?, phone_number =?, email =?, speciality =?, employee_role =? WHERE email =?";
    public static final String DELETE_QUERY = "DELETE FROM employees WHERE email=?";

    public static final String FIND_ALL_EMPLOYEES_ID_IN_BRIGADE = "SELECT employee_id FROM brigades_employees";
    public static final String DELETE_BY_ID_QUERY = "DELETE FROM employees WHERE id=?";

    public static final String FIND_BY_EMAIL = "SELECT * FROM employees WHERE email =?";

    public static final String FIND_BY_ID = "SELECT * FROM employees WHERE id =?";
    public static final String FIND_EMPLOYEES_QUERY = "SELECT * FROM employees";
    public static final String EXIST_BY_EMAIL_QUERY = "SELECT email FROM employees WHERE email=?";
    public static final String EXIST_BY_PHONE_NUMBER_QUERY = "SELECT phone_number FROM employees WHERE phone_number=?";
}
