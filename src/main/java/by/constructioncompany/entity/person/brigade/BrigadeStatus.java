package by.constructioncompany.entity.person.brigade;

import java.util.ArrayList;
import java.util.List;

public enum BrigadeStatus {

    USED,
    FREE;

    public static List<BrigadeStatus> getAll () {
        List<BrigadeStatus> statuses = new ArrayList<>();
        statuses.add(USED);
        statuses.add(FREE);
        return statuses;
    }
}
