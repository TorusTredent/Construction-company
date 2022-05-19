package by.constructioncompany.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveSupplierDto {

    private int id;
    private String name;
    private double rating;
    private int count;
}
