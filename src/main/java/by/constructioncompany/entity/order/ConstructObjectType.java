package by.constructioncompany.entity.order;

import java.util.ArrayList;
import java.util.List;

public enum ConstructObjectType {

    HIGH_RISES,
    COTTAGE,
    PARKING;

    public static List<ConstructObjectType> getAll() {
        List<ConstructObjectType> statuses = new ArrayList<>();
        statuses.add(HIGH_RISES);
        statuses.add(COTTAGE);
        statuses.add(PARKING);
        return statuses;
    }
    public static ConstructObjectType findEqualType(String status) {
        if (status.equals(HIGH_RISES.toString())) {
            return HIGH_RISES;
        }
        if (status.equals(COTTAGE.toString())) {
            return COTTAGE;
        }
        if (status.equals(PARKING.toString())) {
            return PARKING;
        }
        return null;
    }
}
