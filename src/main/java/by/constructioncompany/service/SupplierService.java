package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.supplier.JdbcSupplierDao;
import by.constructioncompany.dto.iosu.PerekrestDto;
import by.constructioncompany.entity.stock.Supplier;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {

    @Autowired
    private JdbcSupplierDao supplierDao;

    public boolean save (Supplier supplier) {
        return supplierDao.save(supplier);
    }

    public boolean update (Supplier supplier, int id) {
        return  supplierDao.update(supplier, id);
    }
    public boolean existByName(String name) {
        return supplierDao.existByName(name);
    }

    public boolean delete (int id) {
        if (supplierDao.existSupplierInStock(id)) {
            return false;
        } else {
            return supplierDao.delete(id);
        }
    }
    public Supplier findByName(String name) {
        return supplierDao.findByName(name).orElse(null);
    }

    public List<Supplier> findAll () {
        return (List<Supplier>) supplierDao.findAll().orElse(null);
    }

    public Supplier findById (int id) {
        return (Supplier) supplierDao.findById(id).orElse(null);
    }

    public List<PerekrestDto> perekrest() {
        return supplierDao.perekrest().orElse(null);
    }
}
