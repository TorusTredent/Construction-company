package by.constructioncompany.entity.person.brigade;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Brigade {

    private int id;
    private double salary;
    private BrigadeStatus brigadeStatus;
}
