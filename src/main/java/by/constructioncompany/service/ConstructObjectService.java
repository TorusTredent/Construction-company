package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.construct_object.JdbcConstructObjectDao;
import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.ConstructObjectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConstructObjectService {

    @Autowired
    private JdbcConstructObjectDao constructObjectDao;

    public List<ConstructObject> findAll() {
        return (List<ConstructObject>) constructObjectDao.findAll().orElse(null);
    }

    public ConstructObject findBySquareAndName(double square, ConstructObjectType type) {
        return constructObjectDao.findBySquareAndType(type, square).orElse(null);
    }

    public ConstructObject findByTypeAndSquare(String typeAndSquare) {
        String[] parts = typeAndSquare.trim().split("-");
        String name = parts[0];
        String square = parts[1];
        ConstructObjectType equalType = ConstructObjectType.findEqualType(name);
        return findBySquareAndName(Double.parseDouble(square), equalType);
    }

    public boolean save (ConstructObject constructObject) {
        return constructObjectDao.save(constructObject);
    }

    public boolean update (ConstructObject constructObject, int id) {
        return constructObjectDao.update(constructObject, id);
    }

    public ConstructObject findById (int id) {
        return (ConstructObject) constructObjectDao.findById(id).orElse(null);
    }

    public boolean existBySquareAndType(double square, ConstructObjectType type) {
        return constructObjectDao.existBySquareAndType(square, type);
    }

    public boolean deleteBySquareAndType (double square, ConstructObjectType type) {
        return constructObjectDao.deleteBySquareAndType(square, type);
    }

    public boolean deleteById (int id) {
        return constructObjectDao.delete(id);
    }
}
