package by.constructioncompany.dao.jdbc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
@Slf4j
public class JdbcPostgresSQLConnection {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/construction-company";
    private static final String USER = "postgres";
    private static final String PASS = "root";


    public static Connection connect() {

        Connection connection = null;

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(
                    DB_URL,
                    USER,
                    PASS
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        
        return connection;
    }
}
