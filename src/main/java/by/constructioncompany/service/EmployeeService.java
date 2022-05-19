package by.constructioncompany.service;

import by.constructioncompany.dao.jdbc.employee.JdbcEmployeeDao;
import by.constructioncompany.entity.person.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private JdbcEmployeeDao employeeDao;

    public boolean save (Employee employee) {
        return employeeDao.save(employee);
    }

    public boolean update (Employee employee, String email) {
        return employeeDao.update(employee, email);
    }

    public boolean deleteByEmail(String email) {
        return employeeDao.delete(email);
    }

    public boolean deleteById(int id) {
        return employeeDao.deleteById(id);
    }

    public boolean existByEmail (String email) {
        return employeeDao.existByEmail(email);
    }

    public boolean existByPhoneNumber (String phoneNumber) {
        return employeeDao.existByPhoneNumber(phoneNumber);
    }

    public Employee findByEmail (String email) {
        return (Employee) employeeDao.findByEmail(email).orElse(null);
    }

    public List<Employee> findPersons () {
        return (List<Employee>) employeeDao.findPersons().orElse(null);
    }

    public List<Employee> findAllWithoutBrigade() {
        List<Integer> ids = employeeDao.findAllEmployeeIdsInBrigades().orElse(null);
        List<Employee> employees = (List<Employee>) employeeDao.findPersons().orElse(null);
        List<Employee> employeesWithBrigade = new ArrayList<>();
        if (ids != null && employees != null) {
            for (Employee employee : employees) {
                for (Integer id : ids) {
                    if (employee.getId() == id) {
                        employeesWithBrigade.add(employee);
                    }
                }
            }
            employees.removeAll(employeesWithBrigade);
        }
        return employees;
    }
    public Employee findById(int id) {
        return (Employee) employeeDao.findById(id).orElse(null);
    }
}
