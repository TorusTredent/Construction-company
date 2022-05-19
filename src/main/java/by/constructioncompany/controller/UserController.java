package by.constructioncompany.controller;

import by.constructioncompany.dto.SaveOrderDto;
import by.constructioncompany.dto.user.AuthPersonDto;
import by.constructioncompany.dto.user.SaveUserDto;
import by.constructioncompany.dto.user.UpdateUserDto;
import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.Order;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.employee.Employee;
import by.constructioncompany.entity.person.employee.EmployeeRole;
import by.constructioncompany.entity.person.user.User;
import by.constructioncompany.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ConstructObjectService constructObjectService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("newUser", new SaveUserDto());
        return "user/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("newUser") SaveUserDto newUser, BindingResult result, Model model) {
        try {
            if (result.hasErrors()) {
                return "user/registration";
            }
            User user = mapper.map(newUser, User.class);
            if (userService.existByEmail(user.getEmail()) && employeeService.existByEmail(user.getEmail())) {
                model.addAttribute("message", "Email is already exist");
                return "user/registration";
            } else {
                userService.save(user);
                return "redirect:/user/authorization";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "user/registration";
    }

    @GetMapping(value = {"/authorization", "/"})
    public String authorization(Model model) {
        model.addAttribute("authUser", new User());
        return "/user/authorization";
    }

    @PostMapping("/authorization")
    public String authorization(@ModelAttribute("authUser") AuthPersonDto authPersonDto, BindingResult result, Model model, HttpSession httpSession) {
        try {
            if (result.hasErrors()) {
                return "user/authorization";
            }
            if (userService.existByEmail(authPersonDto.getEmail())) {
                User user = userService.findByEmail(authPersonDto.getEmail());
                if (user.getPassword().equals(authPersonDto.getPassword())) {
                    httpSession.setAttribute("authUser", user);
                    return "redirect:/user/history";
                } else {
                    model.addAttribute("message", "Password not equals");
                }
            } else {
                if (employeeService.existByEmail(authPersonDto.getEmail())) {
                    Employee employee = employeeService.findByEmail(authPersonDto.getEmail());
                    if (employee.getPassword().equals(authPersonDto.getPassword())) {
                        httpSession.setAttribute("authEmployee", employee);
                        if (employee.getEmployeeRole() == EmployeeRole.DIRECTOR) {
                            return "redirect:/order/all";
                        }
                    } else {
                        model.addAttribute("message", "Password not equals");
                    }
                } else {
                    model.addAttribute("message", "User not found");
                }
            }
            return "user/authorization";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "user/authorization";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("authUser");
        model.addAttribute("updateUser", user);
        return "user/profile";
    }

    @GetMapping("/profile/{id}")
    public String selectedUserProfile(@PathVariable("id") int id, Model model, HttpSession session) {
        User user = userService.findById(id);
        model.addAttribute("updateUser", user);
        return "user/selectedProfile";
    }

    @PostMapping("/profile")
    public String profileUpdate(@ModelAttribute("updateUser") UpdateUserDto updateUserDto, BindingResult bindingResult,
                                Model model, HttpSession session) {
        try {
            if (!bindingResult.hasErrors()) {
                User user = mapper.map(updateUserDto, User.class);
                User sessionUser = (User) session.getAttribute("authUser");
                userService.update(user, sessionUser.getEmail());
                User userMemory = userService.findByEmail(user.getEmail());
                session.setAttribute("authUser", userMemory);
                model.addAttribute("messageComplete", true);
            }
            return "user/profile";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "user/profile";
    }

    @GetMapping("/history")
    public String history(Model model, HttpSession session) {
        User user = (User) session.getAttribute("authUser");
        List<Order> orders = orderService.findAllByUserId(user.getId());
        for(Order order : orders) {
            order.setConstructObject(constructObjectService.findById(order.getConstructObject().getId()));
        }
        model.addAttribute("orders", orders);
        return "user/history";
    }

    @GetMapping("/all")
    public String users(Model model) {
        List<User> users = (List<User>) userService.findPersons();
        model.addAttribute("users", users);
        return "user/users";
    }

    @GetMapping("/add/order")
    public String addOrder(Model model, HttpSession session) {
        User user = (User) session.getAttribute("authUser");
        List<Brigade> brigades = brigadeService.findAll();
        List<Integer> freeBrigades = brigadeService.getFreeBrigadeIds(brigades);
        List<ConstructObject> objects = constructObjectService.findAll();
        model.addAttribute("user", user);
        model.addAttribute("brigades", freeBrigades);
        model.addAttribute("objects", objects);
        model.addAttribute("saveOrder", new SaveOrderDto());
        return "user/addOrder";
    }

    @PostMapping("/add/order")
    public String addOrder(@ModelAttribute("saveOrder") SaveOrderDto saveOrderDto, Model model, HttpSession session) {
        Order order = new Order();
        order.setAddress(saveOrderDto.getAddress());
        order.setOrderStatus(saveOrderDto.getOrderStatus());
        order.setEndOfWork(saveOrderDto.getEndOfWork());
        order.setBeginningOfWork(saveOrderDto.getBeginningOfWork());
        User user = (User) session.getAttribute("authUser");
        order.setUser(user);
        order.setBrigade(brigadeService.findById(saveOrderDto.getBrigadeId()));
        order.setConstructObject(constructObjectService.findByTypeAndSquare(saveOrderDto.getConstructObject()));
        orderService.save(order);
        return "redirect:/user/history";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser (@PathVariable("id") int id) {
        List<Order> orders =  orderService.findAllByUserId(id);
        if (orders != null) {
            for (Order order : orders) {
                orderService.deleteOrderStock(order.getId());
            }
            orderService.deleteByUserId(id);
        }
        userService.deleteById(id);
        return "redirect:/user/all";
    }



    @GetMapping("/logout")
    public String logout(HttpSession httpSession, Model model) {
        httpSession.invalidate();
        model.addAttribute("newUser", new User());
        return "redirect:/user/authorization";
    }
}
