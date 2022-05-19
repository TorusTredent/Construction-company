package by.constructioncompany.controller;

import by.constructioncompany.dto.employee.SaveEmployeeDto;
import by.constructioncompany.dto.employee.UpdateEmployeeDto;
import by.constructioncompany.entity.person.employee.Employee;
import by.constructioncompany.entity.person.employee.EmployeeRole;
import by.constructioncompany.service.EmployeeService;
import by.constructioncompany.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;


    @GetMapping("/registration")
    public String registration(Model model) {
        List<EmployeeRole> employeeRoles = EmployeeRole.getAll();
        model.addAttribute("statuses", employeeRoles);
        model.addAttribute("newEmployee", new SaveEmployeeDto());
        return "employee/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("newEmployee") SaveEmployeeDto newEmployee, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                return "user/registration";
            }
            Employee employee = mapper.map(newEmployee, Employee.class);
            if (userService.existByEmail(employee.getEmail()) && employeeService.existByEmail(employee.getEmail())) {
                model.addAttribute("message", "Email is already exist");
                return "employee/registration";
            } else {
                if (employeeService.save(employee)) {
                    return "redirect:/employee/all";
                }
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "employee/registration";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        Employee employee = (Employee) session.getAttribute("authEmployee");
        model.addAttribute("updateEmployee", employee);
        return "employee/profile";
    }

    @GetMapping(value = {"/selectedProfile/{id}", "/selectedProfile/"})
    public String selectedUserProfile(@PathVariable("id") Optional<Integer> id, Model model, HttpSession session) {
        Employee employee;
        if (session.getAttribute("employeeId") != null) {
            int employeeId = (int) session.getAttribute("employeeId");
            session.setAttribute("employeeId", null);
            employee = employeeService.findById(employeeId);
        } else {
            employee = employeeService.findById(id.get());
        }
        model.addAttribute("updateEmployee", employee);
        return "employee/selectedProfile";
    }

    @PostMapping("/profile")
    public String profileUpdate(@ModelAttribute("updateEmployee") UpdateEmployeeDto updateEmployeeDto, BindingResult bindingResult,
                                Model model, HttpSession session) {
        try {
            if (!bindingResult.hasErrors()) {
                Employee employee = mapper.map(updateEmployeeDto, Employee.class);
                Employee sessionEmployee = (Employee) session.getAttribute("authEmployee");
                Employee updateEmployeeDb = employeeService.findById(sessionEmployee.getId());
                employee.setEmployeeRole(updateEmployeeDb.getEmployeeRole());
                employeeService.update(employee, updateEmployeeDb.getEmail());
                if (sessionEmployee.getId() != updateEmployeeDb.getId() && sessionEmployee.getEmployeeRole() == EmployeeRole.DIRECTOR ) {
                    session.setAttribute("employeeId", employee.getId());
                    return "redirect:/employee/selectedProfile/";
                } else {
                    session.setAttribute("authEmployee", employee);
                    model.addAttribute("messageComplete", true);
                }
                return "employee/profile";
            }

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "employee/profile";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") int id, HttpSession session) {
        employeeService.deleteById(id);
        return "redirect:/employee/all";
    }

    @GetMapping("/all")
    public String employees(Model model, HttpSession session) {
        List<Employee> employees = employeeService.findPersons();
        model.addAttribute("employees", employees);
        return "employee/employees";
    }


    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.invalidate();
        return "redirect:/";
    }
}
