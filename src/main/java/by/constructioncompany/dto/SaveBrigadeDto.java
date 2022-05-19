package by.constructioncompany.dto;


import by.constructioncompany.entity.person.brigade.BrigadeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveBrigadeDto {

    private int id;
    private double salary;
    private BrigadeStatus status;
}
