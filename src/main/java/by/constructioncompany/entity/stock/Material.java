package by.constructioncompany.entity.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    private int id;
    private String name;
    private double unitPrice;
    private Supplier supplier = new Supplier();
    private int countOfMaterials;
    private LocalDate purchaseDate;

    public int getCountOfMaterials() {
        return countOfMaterials;
    }
}
