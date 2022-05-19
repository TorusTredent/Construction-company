package by.constructioncompany.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConstructObject {

    private int id;
    private double square;
    private ConstructObjectType constructObjectType;
}
