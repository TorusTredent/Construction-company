package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.order.JdbcOrderDao;
import by.constructioncompany.entity.order.Order;
import by.constructioncompany.entity.order.OrderStatus;
import by.constructioncompany.entity.stock.Material;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private JdbcOrderDao orderDao;

    public boolean save(Order order) {
        checkStatus(order);
        return orderDao.save(order);
    }

    public boolean update(Order order, int id) {
        return orderDao.update(order, id);
    }

    public boolean delete(int id) {
        orderDao.deleteOrderStock(id);
        return orderDao.delete(id);
    }

    public Order findById(int id) {
        return (Order) orderDao.findById(id).orElse(null);
    }

    public List<Order> findAll() {
        List<Order> orders = (List<Order>) orderDao.findAll().orElse(null);
        for (Order order : orders) {
            checkStatus(order);
        }
        return orders;
    }

    public List<Order> findAllByDates(LocalDate begin, LocalDate end) {
        return (List<Order>) orderDao.findAllByDates(begin, end).orElse(null);
    }

    public List<Order> findAllByStatus(OrderStatus status) {
        return (List<Order>) orderDao.findAllByOrderStatus(status).orElse(null);
    }

    public List<Order> findAllByUserId(int userId) {
        return (List<Order>) orderDao.findAllByUserId(userId).orElse(null);
    }

    public boolean deleteByUserId(int id) {
        return orderDao.deleteByUserId(id);
    }

    public boolean deleteOrderStock(int id) {
        return orderDao.deleteOrderStock(id);
    }

    public void setCost(Order order) {
        if (order.getMaterials() != null) {
            double materialsCost = 0;
            for (Material material : order.getMaterials()) {
                materialsCost += material.getUnitPrice() * material.getCountOfMaterials();
            }
            switch (order.getConstructObject().getConstructObjectType()) {
                case COTTAGE: {
                    order.setCost(order.getBrigade().getSalary() + materialsCost + 1000);
                    break;
                }
                case PARKING: {
                    order.setCost(order.getBrigade().getSalary() + materialsCost + 3000);
                    break;
                }
                case HIGH_RISES: {
                    order.setCost(order.getBrigade().getSalary() + materialsCost + 5000);
                    break;
                }
                default: {
                    order.setCost(order.getBrigade().getSalary() + materialsCost);
                    break;
                }
            }
        }
    }

    public List<Order> findAllByBrigadeId(int id) {
        List<Order> orders = (List<Order>) orderDao.findAllByBrigadeId(id).orElse(null);
        Set<Order> set = new HashSet<>(orders);
        orders.clear();
        orders.addAll(set);
        return orders;
    }

    public Integer getOrderCounterWithInProgressStatus() {
        return orderDao.getOrderCounterWithInProgressStatus().orElse(0);
    }


    public boolean setOrderStatus(OrderStatus orderStatus, int id) {
        return orderDao.setOrderStatus(orderStatus, id);
    }


    private void checkStatus(Order order) {
        int count = getOrderCounterWithInProgressStatus();
        if (order.changeOrderStatus()) {
            if (order.getOrderStatus() != OrderStatus.COMPLETED) {
                if (count < 3) {
                    if (order.getOrderStatus() == OrderStatus.FORMED) {
                        order.changeOrderStatus();
                        setOrderStatus(order.getOrderStatus(), order.getId());
                    }
                }
            }
        } else {
            if (count > 3 && order.getOrderStatus() == OrderStatus.IN_PROGRESS) {
                order.setOrderStatus(OrderStatus.FORMED);
            }
        }
    }


}
