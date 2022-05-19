package by.constructioncompany.dao.jdbc.order;

import by.constructioncompany.entity.order.Order;
import by.constructioncompany.entity.order.OrderStatus;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class OrderFieldsSetter {

    private static final String ID = "id";
    private static final String ADDRESS = "address";
    private static final String BEGINNING_OF_WORK = "beginning_of_work";
    private static final String END_OF_WORK = "end_of_work";
    private static final String BRIGADE_ID = "brigade_id";
    private static final String USER_ID = "user_id";
    private static final String ORDER_STATUS = "order_status";
    private static final String CONSTRUCTION_OBJECT_ID = "construction_object_id";


    public void setOrderFieldsForSaveAUpdate(PreparedStatement prep, Order order) throws SQLException {
        prep.setString(1, order.getAddress());
        prep.setDate(2, Date.valueOf(order.getBeginningOfWork()));
        prep.setDate(3, Date.valueOf(order.getEndOfWork()));
        prep.setInt(4, order.getBrigade().getId());
        prep.setInt(5, order.getUser().getId());
        prep.setString(6, order.getOrderStatus().toString());
        prep.setInt(7, order.getConstructObject().getId());
    }

    public void setAllOrderFields(ResultSet rs, Order order) throws SQLException {
        order.setId(rs.getInt(ID));
        order.setAddress(rs.getString(ADDRESS));
        order.setBeginningOfWork(LocalDate.parse(rs.getString(BEGINNING_OF_WORK)));
        order.setEndOfWork(LocalDate.parse(rs.getString(END_OF_WORK)));
        order.setOrderStatus(OrderStatus.valueOf(rs.getString(ORDER_STATUS)));
    }
}
