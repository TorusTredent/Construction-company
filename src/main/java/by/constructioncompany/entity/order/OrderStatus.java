package by.constructioncompany.entity.order;

import java.util.ArrayList;
import java.util.List;

public enum OrderStatus {

    FORMED,
    IN_PROGRESS,
    COMPLETED;

    public static List<OrderStatus> getAll() {
        List<OrderStatus> statuses = new ArrayList<>();
        statuses.add(FORMED);
        statuses.add(IN_PROGRESS);
        statuses.add(COMPLETED);
        return statuses;
    }

    public static OrderStatus findEqualStatus(String status) {
        if (status.equals(FORMED.toString())) {
            return FORMED;
        }
        if (status.equals(IN_PROGRESS.toString())) {
            return IN_PROGRESS;
        }
        if (status.equals(COMPLETED.toString())) {
            return COMPLETED;
        }
        return null;
    }
}
