package by.constructioncompany.dto.iosu;

import by.constructioncompany.entity.stock.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItogovyDto {

    private int id;
    private String stockName;
    private String supplierName;
    private double rating;
    private double cost;
    private Supplier supplier;

    public ItogovyDto(int id, String stockName, String supplierName, double rating, double cost) {
        this.id = id;
        this.stockName = stockName;
        this.supplierName = supplierName;
        this.rating = rating;
        this.cost = cost;
    }
}
