package by.constructioncompany.dao;

import by.constructioncompany.entity.person.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    boolean save (User user);
    boolean update (User user, String email);

    boolean delete (String email);
    boolean existByEmail (String email);

    boolean existByPhoneNumber (String phoneNumber);

    Optional<User> findByEmail (String email);

    Optional<List<User>> findUsers ();


}
