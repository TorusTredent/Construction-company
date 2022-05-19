package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.stock.JdbcStockDao;
import by.constructioncompany.dto.iosu.ItogovyDto;
import by.constructioncompany.entity.stock.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

@Service
public class StockService {


    @Autowired
    JdbcStockDao stockDao;

    public boolean save(Material material) {
        if (existByNameAndDate(material.getName(), material.getPurchaseDate())) {
            Material materialDb = stockDao.findByNameAndDate(material.getName(), material.getPurchaseDate()).orElse(null);
            materialDb.setCountOfMaterials(materialDb.getCountOfMaterials() + material.getCountOfMaterials());
            return update(materialDb, materialDb.getId());
        } else {
            return stockDao.save(material);
        }
    }

    public boolean update(Material material, int id) {
        return stockDao.update(material, id);
    }

    public boolean delete(int id) {
        return stockDao.delete(id);
    }

    public Material findById(int id) {
        return (Material) stockDao.findById(id).orElse(null);
    }


    public List<Material> findAll() {
        return (List<Material>) stockDao.findAll().orElse(null);
    }

    public boolean existByNameAndDate(String name, LocalDate date) {
        return stockDao.existByNameAndDate(name, date);
    }

    public List<Material> findAllByOrderId(int id) {
        return stockDao.findAllByOrderId(id).orElse(null);
    }

    public List<String> union () {
        return stockDao.union();
    }

    public List<ItogovyDto> itogovy(LocalDate selectedDate) {
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }
        int year = selectedDate.getYear();
        Month month = selectedDate.getMonth();
        LocalDate beginDate = YearMonth.of(year, month).atDay(1);
        LocalDate endDate = YearMonth.of(year, month).atEndOfMonth();
        return stockDao.itogovy(beginDate, endDate);
    }

    public boolean updateMaterialCounterInOrder(int count, int materialId, int orderId) {
        return stockDao.updateMaterialCounterInOrder(count, materialId, orderId);
    }

    public boolean changeMaterialCount(Material material, int orderId, int count) {
        int oldCount = stockDao.findCountByMaterialIdAndOrderId(material.getId(), orderId);
        if (oldCount < count) {
            int newCount = material.getCountOfMaterials() - (count - oldCount);
            if (newCount >= 0) {
                material.setCountOfMaterials(newCount);
                update(material, material.getId());
                updateMaterialCounterInOrder(count, material.getId(), orderId);
                return true;
            }
        } else {
            int newCount = oldCount - count;
            material.setCountOfMaterials(material.getCountOfMaterials() + newCount);
            update(material, material.getId());
            updateMaterialCounterInOrder(count, material.getId(), orderId);
            return true;
        }

        return false;
    }

    public boolean changeMaterialCount(Material material, int count) {
        material.setCountOfMaterials(count);
        return stockDao.update(material, material.getId());
    }

    public boolean changeStockMaterialCount(Material material, int orderId, int count) {
        int newStockCount = material.getCountOfMaterials() - count;
        if (stockDao.existMaterialInOrder(orderId, material.getId())) {
            int newCount = stockDao.findCountByMaterialIdAndOrderId(material.getId(), orderId) + count;
            updateMaterialCounterInOrder(newCount, material.getId(), orderId);
        } else {
            stockDao.insertMaterialForOrder(orderId, material.getId(), count);
        }
        material.setCountOfMaterials(newStockCount);
        update(material, material.getId());
        return true;
    }

    public boolean deleteMaterialFromOrder(Material material, int orderId, int count) {
        if (stockDao.deleteMaterialFromOrder(material.getId(), orderId)) {
            material.setCountOfMaterials(material.getCountOfMaterials() + count);
            update(material, material.getId());
            return true;
        }
        return false;
    }

    public boolean existByName(String name) {
        return stockDao.existByName(name);
    }

    public Material findByNameAndDate(String name, LocalDate date) {
        return stockDao.findByNameAndDate(name, date).orElse(null);
    }

    public List<Material> findBySupplierId(int id) {
        return (List<Material>) stockDao.findBySupplierId(id).orElse(null);
    }
}
