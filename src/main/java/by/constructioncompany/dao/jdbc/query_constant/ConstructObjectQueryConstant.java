package by.constructioncompany.dao.jdbc.query_constant;

public class ConstructObjectQueryConstant {

    public static final String INSERT_QUERY = "INSERT INTO construct_objects (square, type) VALUES (?, ?)";
    public static final String FIND_ALL = "SELECT * FROM construct_objects";

    public static final String UPDATE_QUERY = "UPDATE construct_objects SET square =?, type =? WHERE id =?";
    public static final String FIND_BY_TYPE_AND_SQUARE = "SELECT * FROM construct_objects WHERE type =? AND square =?";

    public static final String FIND_BY_ID = "SELECT * FROM construct_objects WHERE id =?";

    public static final String DELETE_QUERY = "DELETE FROM construct_objects WHERE id=?";

    public static final String DELETE_BY_TYPE_AND_SQUARE = "DELETE FROM construct_objects WHERE type =? AND square =?";
}
