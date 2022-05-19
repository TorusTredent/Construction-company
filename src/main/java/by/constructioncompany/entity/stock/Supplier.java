package by.constructioncompany.entity.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supplier {

    private int id;
    private String name;
    private double rating;

    public Supplier(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }
}
