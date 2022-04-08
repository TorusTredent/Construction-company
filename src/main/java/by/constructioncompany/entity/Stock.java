package by.constructioncompany.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    private int id;
    private String name;
    private double unitPrice;
    private int countOfMaterials;
    private LocalDate purchaseDate;
    private int supplierId;
}
