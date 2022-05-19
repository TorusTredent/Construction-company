package by.constructioncompany.entity.person.employee;

import java.util.ArrayList;
import java.util.List;

public enum EmployeeRole {

    DIRECTOR,
    WORKER;

    public static List<EmployeeRole> getAll() {
        List<EmployeeRole> statuses = new ArrayList<>();
        statuses.add(DIRECTOR);
        statuses.add(WORKER);
        return statuses;
    }

    public static EmployeeRole findEqualStatus(String status) {
        if (status.equals(DIRECTOR.toString())) {
            return DIRECTOR;
        }
        if (status.equals(WORKER.toString())) {
            return WORKER;
        }
        return null;
    }
}
