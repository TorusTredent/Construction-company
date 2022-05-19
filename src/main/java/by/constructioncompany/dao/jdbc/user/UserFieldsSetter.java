package by.constructioncompany.dao.jdbc.user;

import by.constructioncompany.entity.person.user.User;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserFieldsSetter {

    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String PHONE_NUMBER = "phone_number";
    private static final String DISCOUNT = "discount";


    public void setUserFieldsForPrep(PreparedStatement prep, User user) throws SQLException {
        prep.setString(1, user.getPassword());
        prep.setString(2, user.getFirstName());
        prep.setString(3, user.getLastName());
        prep.setString(4, user.getPhoneNumber());
        prep.setString(5, user.getEmail());
        prep.setInt(6, user.getDiscount());
    }

    public void setUserFields(ResultSet rs, User user) throws SQLException {
        user.setId(rs.getInt(ID));
        user.setEmail(rs.getString(EMAIL));
        user.setPassword(rs.getString(PASSWORD));
        user.setFirstName(rs.getString(FIRST_NAME));
        user.setLastName(rs.getString(LAST_NAME));
        user.setPhoneNumber(rs.getString(PHONE_NUMBER));
        user.setDiscount(rs.getInt(DISCOUNT));
    }
}
