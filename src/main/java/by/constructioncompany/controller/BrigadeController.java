package by.constructioncompany.controller;

import by.constructioncompany.dto.SaveBrigadeDto;
import by.constructioncompany.entity.order.Order;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.brigade.BrigadeStatus;
import by.constructioncompany.entity.person.employee.Employee;
import by.constructioncompany.service.BrigadeService;
import by.constructioncompany.service.EmployeeService;
import by.constructioncompany.service.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/brigade")
public class BrigadeController {

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/all")
    public String brigades(Model model, HttpSession session) {
        List<Brigade> brigades = brigadeService.findAll();

        model.addAttribute("brigades", brigades);
        model.addAttribute("statuses", BrigadeStatus.getAll());
        model.addAttribute("saveBrigade", new SaveBrigadeDto());
        model.addAttribute("objectIsAlreadyExist", session.getAttribute("objectIsAlreadyExist"));
        model.addAttribute("messageComplete", session.getAttribute("messageComplete"));
        model.addAttribute("messageWarning", session.getAttribute("messageWarning"));
        model.addAttribute("objectNotFount", session.getAttribute("objectNotFount"));

        session.setAttribute("ObjectIsAlreadyExist", null);
        session.setAttribute("messageComplete", null);
        session.setAttribute("messageWarning", null);
        session.setAttribute("objectNotFount", null);
        return "brigade/brigades";
    }

    @PostMapping("/update")
    public String update (@ModelAttribute("saveBrigade") SaveBrigadeDto saveBrigadeDto, HttpSession session) {
        Brigade brigade = mapper.map(saveBrigadeDto, Brigade.class);
        if (brigadeService.update(brigade, brigade.getId())) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The brigade wasn't update");
        }
        return "redirect:/brigade/all";
    }

    @PostMapping("/delete")
    public String brigade(@ModelAttribute("saveBrigade") SaveBrigadeDto saveBrigadeDto, HttpSession session) {
        if (brigadeService.delete(saveBrigadeDto.getId())){
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The brigade wasn't delete");
        }
        return "redirect:/brigade/all";
    }

    @PostMapping("/add")
    public String addBrigade(@ModelAttribute("saveBrigade") SaveBrigadeDto saveBrigadeDto, HttpSession session){
        Brigade brigade = mapper.map(saveBrigadeDto, Brigade.class);
        if (brigadeService.save(brigade)) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The brigade wasn't save");
        }
        return "redirect:/brigade/all";
    }
    @GetMapping(value = {"/{id}", "/"})
    public String brigade(@PathVariable("id") Optional<Integer> id, Model model, HttpSession session) {
        Brigade brigade;
        if (id.isEmpty() || id.get() == 0) {
            int brigadeId = (int) session.getAttribute("brigadeId");
            brigade = brigadeService.findBrigadeWithEmployeeByBrigadeId(brigadeId);
            session.setAttribute("brigadeId", null);
        } else {
            brigade = brigadeService.findBrigadeWithEmployeeByBrigadeId(id.get());
        }
        List<Order> brigadeOrders = orderService.findAllByBrigadeId(brigade.getId());
        List<Employee> employeesWithoutBrigade = employeeService.findAllWithoutBrigade();

        model.addAttribute("brigade", brigade);
        model.addAttribute("brigadeOrders", brigadeOrders);
        model.addAttribute("employeesWithoutBrigade", employeesWithoutBrigade);
        model.addAttribute("saveBrigade", new SaveBrigadeDto());
        model.addAttribute("statuses", BrigadeStatus.getAll());

        model.addAttribute("objectIsAlreadyExist", session.getAttribute("objectIsAlreadyExist"));
        model.addAttribute("messageComplete", session.getAttribute("messageComplete"));
        model.addAttribute("messageWarning", session.getAttribute("messageWarning"));
        model.addAttribute("objectNotFount", session.getAttribute("objectNotFount"));

        session.setAttribute("ObjectIsAlreadyExist", null);
        session.setAttribute("messageComplete", null);
        session.setAttribute("messageWarning", null);
        session.setAttribute("objectNotFount", null);
        return "brigade/brigade";
    }

    @PostMapping("/employee/delete/")
    public String deleteEmployee(int brigadeId, int id, Model model, HttpSession session) {
        if(brigadeService.deleteEmployeeById(brigadeId, id)) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The employee wasn't delete");
        }
        session.setAttribute("brigadeId", brigadeId);
        return "redirect:/brigade/";
    }

    @PostMapping("/employee/add/")
    public String addEmployee (int brigadeId, int id, HttpSession session) {
        if (brigadeService.addEmployee(brigadeId, id)) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The employee wasn't add");
        }
        session.setAttribute("brigadeId", brigadeId);
        return "redirect:/brigade/";
    }
}
