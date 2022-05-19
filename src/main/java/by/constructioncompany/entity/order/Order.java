package by.constructioncompany.entity.order;

import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.user.User;
import by.constructioncompany.entity.stock.Material;
import by.constructioncompany.service.OrderService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Autowired
    private OrderService orderService;

    private int id;
    private String address;
    private LocalDate beginningOfWork ;
    private LocalDate endOfWork;
    private Brigade brigade;
    private User user;
    private ConstructObject constructObject;
    private double cost;
    private List<Material> materials;
    private OrderStatus orderStatus = OrderStatus.FORMED;

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }

    public boolean changeOrderStatus() {
        LocalDate dateNow = LocalDate.now();
        if (orderStatus == OrderStatus.COMPLETED) {
            return false;
        }
        if (dateNow.isBefore(beginningOfWork)) {
            if (orderStatus != OrderStatus.FORMED) {
                orderStatus = OrderStatus.FORMED;
                return true;
            }
        }
        if (dateNow.isAfter(beginningOfWork) && dateNow.isBefore(endOfWork) || dateNow.isEqual(beginningOfWork)) {
            if (orderStatus != OrderStatus.IN_PROGRESS) {
                orderStatus = OrderStatus.IN_PROGRESS;
                return true;
            }
        }
        if (dateNow.isAfter(endOfWork)) {
            if (orderStatus != OrderStatus.COMPLETED) {
                orderStatus = OrderStatus.COMPLETED;
                return true;
            }
        }
        return false;
    }

    private OrderStatus confirmedOrderStatus() {
        LocalDate dateNow = LocalDate.now();
        if (beginningOfWork != null && endOfWork != null) {
            if (dateNow.isBefore(beginningOfWork)) {
                return OrderStatus.FORMED;
            }
            if (dateNow.isEqual(beginningOfWork) || dateNow.isBefore(endOfWork)) {
                return OrderStatus.IN_PROGRESS;
            }
            if (dateNow.isAfter(endOfWork)) {
                return OrderStatus.COMPLETED;
            }
        }
        return OrderStatus.FORMED;
    }
}
