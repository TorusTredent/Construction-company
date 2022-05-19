package by.constructioncompany.dto.iosu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UnionDto {

    private List<Integer> numbers;
    private List<String> names;
}
