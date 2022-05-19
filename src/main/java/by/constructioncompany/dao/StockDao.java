package by.constructioncompany.dao;

import by.constructioncompany.entity.stock.Material;

import java.time.LocalDate;
import java.util.Optional;

public interface StockDao {

    boolean existByNameAndDate (String name, LocalDate date);
    Optional<Material> findByNameAndDate (String name, LocalDate date);

}
