package by.constructioncompany.dto;

import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.OrderStatus;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.user.User;
import by.constructioncompany.entity.stock.Material;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateOrder {

    private int id;
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate beginningOfWork;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endOfWork;
    private Brigade brigade;
    private User user;
    private ConstructObject constructObject;
    private OrderStatus orderStatus;
    private List<Material> materials;
    private int brigadeId;
    private String constructSelected;
    private double cost;
}
