package by.constructioncompany.dto.conObject;

import by.constructioncompany.entity.order.ConstructObjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveObjectDto {

    private int id;
    private double square;
    private ConstructObjectType type;
}
