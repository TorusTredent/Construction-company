package by.constructioncompany.dao;

import java.util.List;
import java.util.Optional;

public interface BrigadeDao {
    boolean addEmployee (int brigadeId, int employeeId);
    boolean deleteEmployee (int brigadeId, int employeeId);

    Optional<List<Integer>> findAllBrigadesNumbers();
}
