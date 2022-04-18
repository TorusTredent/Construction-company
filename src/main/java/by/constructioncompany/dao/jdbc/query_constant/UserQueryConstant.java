package by.constructioncompany.dao.jdbc.query_constant;

public class UserQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO users (password, first_name," +
            " last_name, phone_number, email, discount) VALUES(?, ?, ?, ?, ?, ?)";
    public static final String EXIST_BY_EMAIL_QUERY = "SELECT email FROM users WHERE email=?";
    public static final String EXIST_BY_PHONE_NUMBER_QUERY = "SELECT phone_number FROM users WHERE phone_number=?";
    public static final String FIND_USERS_QUERY = "SELECT * FROM users";
    public static final String DELETE_QUERY = "DELETE FROM users WHERE email=?";
    public static final String UPDATE_QUERY = "UPDATE users SET password =?, first_name =?," +
            " last_name =?, phoneNumber =?, email =?, discount =? WHERE email =?";
    public static final String FIND_BY_EMAIL = "SELECT * FROM users WHERE email =?";
}
