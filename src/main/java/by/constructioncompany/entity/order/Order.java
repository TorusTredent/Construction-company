package by.constructioncompany.entity.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    private int id;
    private String address;
    private LocalDate beginningOfWork;
    private LocalDate endOfWork;
    private int brigadeId;
    private int userId;
    private OrderStatus orderStatus;
}
