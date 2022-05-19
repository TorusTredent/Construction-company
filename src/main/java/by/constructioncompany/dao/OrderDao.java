package by.constructioncompany.dao;

import by.constructioncompany.entity.order.OrderStatus;

import java.util.Optional;

public interface OrderDao {

    Optional<Integer> getOrderCounterWithInProgressStatus();

    boolean setOrderStatus (OrderStatus orderStatus, int id);

}
