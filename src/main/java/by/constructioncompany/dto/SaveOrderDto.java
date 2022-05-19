package by.constructioncompany.dto;


import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.OrderStatus;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SaveOrderDto {

    private String address;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginningOfWork ;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOfWork;
    private int brigadeId;
    private String email;
    private int userId;
    private String constructObject;
    private OrderStatus orderStatus = OrderStatus.FORMED;
}
