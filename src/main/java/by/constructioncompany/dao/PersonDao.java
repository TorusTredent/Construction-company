package by.constructioncompany.dao;

import java.util.List;
import java.util.Optional;

public interface PersonDao {

    boolean save (Object person);
    boolean update (Object person, String email);

    boolean delete (String email);
    boolean existByEmail (String email);

    boolean existByPhoneNumber (String phoneNumber);

    Optional<?> findByEmail (String email);

    Optional<?> findById(int id);

    Optional<List<?>> findPersons ();
}
