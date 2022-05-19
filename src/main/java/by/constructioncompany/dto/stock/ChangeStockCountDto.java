package by.constructioncompany.dto.stock;

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
public class ChangeStockCountDto {

    private String name;
    private int count;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
