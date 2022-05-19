package by.constructioncompany.dao;

import java.util.List;
import java.util.Optional;

public interface ConstructionFirmDao {

    boolean save (Object object);
    boolean update (Object object, int id);

    boolean delete (int id);

    Optional<?> findById (int id);

    Optional<List<?>> findAll ();
}
