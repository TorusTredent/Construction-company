package by.constructioncompany.dto.stock;

import by.constructioncompany.entity.stock.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveStockDto {

    private String name;
    private double unitPrice;
    private Supplier supplier = new Supplier();
    private int countOfMaterials;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;
}
