package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.brigade.JdbcBrigadeDao;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.brigade.BrigadeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrigadeService {

    @Autowired
    private JdbcBrigadeDao brigadeDao;

    public boolean save(Brigade brigade) {
        return brigadeDao.save(brigade);
    }

    public boolean update(Object brigade, int id) {
        return brigadeDao.update(brigade, id);
    }

    public boolean addEmployee(int brigadeId, int employeeId) {
        return brigadeDao.addEmployee(brigadeId, employeeId);
    }


    public boolean delete(int id) {
        return brigadeDao.delete(id);
    }


    public Brigade findById(int id) {
        Brigade brigade = brigadeDao.findById(id).orElse(null);
        checkBrigadeStatus(brigade);
        return brigade;
    }

    public List<Brigade> findAll() {
        List<Brigade> brigades = (List<Brigade>) brigadeDao.findAll().orElse(null);
        for (Brigade brigade : brigades) {
            checkBrigadeStatus(brigade);
        }
        return brigades;
    }

    public Brigade findBrigadeWithEmployeeByBrigadeId(int brigadeId) {
        Brigade brigade = brigadeDao.findBrigadeWithEmployeeByBrigadeId(brigadeId).orElse(null);
        checkBrigadeStatus(brigade);
        return brigade;
    }

    public boolean deleteEmployeeById(int brigadeId, int id) {
        return brigadeDao.deleteEmployeeById(brigadeId, id);
    }

    public List<Integer> getFreeBrigadeIds(List<Brigade> brigades) {
        List<Integer> freeBrigades = new ArrayList<>();
        for (Brigade brigade : brigades) {
            checkBrigadeStatus(brigade);
            if (brigadeDao.getBrigadeOrderCounterByBrigadeId(brigade.getId()) < 2) {
                freeBrigades.add(brigade.getId());
            }
        }
        return freeBrigades;
    }

    private void checkBrigadeStatus(Brigade brigade) {
        if (brigadeDao.getBrigadeOrderCounterByBrigadeId(brigade.getId()) < 2) {
            if (brigade.getBrigadeStatus().equals(BrigadeStatus.USED)) {
                brigade.setBrigadeStatus(BrigadeStatus.FREE);
                brigadeDao.update(brigade, brigade.getId());
            }
        }
    }
}
