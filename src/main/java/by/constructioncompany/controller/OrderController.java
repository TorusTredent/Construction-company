package by.constructioncompany.controller;

import by.constructioncompany.dto.*;
import by.constructioncompany.dto.iosu.ItogovyDto;
import by.constructioncompany.dto.iosu.PerekrestDto;
import by.constructioncompany.dto.iosu.UnionDto;
import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.Order;
import by.constructioncompany.entity.order.OrderStatus;
import by.constructioncompany.entity.person.brigade.Brigade;
import by.constructioncompany.entity.person.user.User;
import by.constructioncompany.entity.stock.Material;
import by.constructioncompany.entity.stock.Supplier;
import by.constructioncompany.service.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BrigadeService brigadeService;

    @Autowired
    private ConstructObjectService constructObjectService;

    @Autowired
    private StockService stockService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/all")
    public String orders(Model model, HttpSession session) {
        List<Order> orders = orderService.findAll();
        for (Order order : orders) {
            orderService.setCost(order);
            ConstructObject constructObject = constructObjectService.findById(order.getId());
            order.setConstructObject(constructObject);
        }
        model.addAttribute("orders", orders);
        return "order/orders";
    }

    @GetMapping(value = {"/{id}", "/"})
    public String order(@PathVariable("id") Optional<Integer> id, Model model, HttpSession session) {
        int orderId = getOrderId(id, session);
        if (orderId == -1) {
            return "redirect:/order/all";
        }
        Order order = orderService.findById(orderId);
        List<Material> materials = stockService.findAllByOrderId(order.getId());
        order.setMaterials(materials);
        orderService.setCost(order);
        UpdateOrder updateOrder = mapper.map(order, UpdateOrder.class);
        List<Brigade> brigades = brigadeService.findAll();
        List<Integer> freeBrigades = brigadeService.getFreeBrigadeIds(brigades);
        List<ConstructObject> constructObjects = constructObjectService.findAll();
        List<OrderStatus> statuses = OrderStatus.getAll();
        model.addAttribute("materialNotFound", session.getAttribute("materialNotFound"));
        model.addAttribute("thisAmountOfMaterialIsOutOfStock",
                session.getAttribute("thisAmountOfMaterialIsOutOfStock"));
        model.addAttribute("materialHasNotBeenRemoved", session.getAttribute("materialHasNotBeenRemoved"));
        model.addAttribute("beginDate", order.getBeginningOfWork());
        model.addAttribute("endDate", order.getEndOfWork());
        model.addAttribute("constructObjects", constructObjects);
        model.addAttribute("brigadesIds", freeBrigades);
        model.addAttribute("order", updateOrder);
        model.addAttribute("statuses", statuses);
        model.addAttribute("messageComplete", session.getAttribute("messageComplete"));

        session.setAttribute("Material not found", null);
        session.setAttribute("This amount of material is out of stock", null);
        session.setAttribute("Material has not been removed", null);
        session.setAttribute("messageComplete", null);
        return "order/order";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("order") UpdateOrder updateOrder, Model model, HttpSession session) {
        Order order = mapper.map(updateOrder, Order.class);
        Brigade brigade = brigadeService.findById(updateOrder.getBrigadeId());
        ConstructObject constructObject = constructObjectService.findByTypeAndSquare(updateOrder.getConstructSelected());
        order.setBrigade(brigade);
        order.setConstructObject(constructObject);
        if (orderService.update(order, updateOrder.getId())) {
            session.setAttribute("messageComplete", true);
        } else {
            session.setAttribute("messageComplete", false);
        }
        session.setAttribute("orderId", updateOrder.getId());
        return "redirect:/order/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id, Model model, HttpSession session) {
        if (orderService.delete(id)) {
            session.setAttribute("messageComplete", true);
        } else {
            session.setAttribute("messageComplete", false);
        }
        return "redirect:/order/all";
    }

    @PostMapping("/material/changeCount/{orderId}")
    public String changeMaterialCount(@PathVariable("orderId") int orderId, int count, int materialId, String page, HttpSession session) {
        Material material = stockService.findById(materialId);
        if (material != null) {
            if (stockService.changeMaterialCount(material, orderId, count)) {
                session.setAttribute("messageComplete", true);
            } else {
                session.setAttribute("thisAmountOfMaterialIsOutOfStock", true);
            }
        } else {
            session.setAttribute("materialNotFound", true);
        }
        session.setAttribute("orderId", orderId);
        if (page.equals("order")) {
            return "redirect:/order/";
        }
        if (page.equals("materials/add")) {
            return "redirect:/order/material/add/";
        }
        return "redirect:/order/all";
    }

    @PostMapping("/material/delete")
    public String deleteMaterial(int orderId, int count, int materialId, String page, HttpSession session) {
        Material material = stockService.findById(materialId);
        if (material != null) {
            if (stockService.deleteMaterialFromOrder(material, orderId, count)) {
                session.setAttribute("messageComplete", true);
            } else {
                session.setAttribute("materialHasNotBeenRemoved", true);
            }
        } else {
            session.setAttribute("materialNotFound", true);
        }
        session.setAttribute("orderId", orderId);
        if (page.equals("order")) {
            return "redirect:/order/";
        }
        if (page.equals("materials/add")) {
            return "redirect:/order/material/add/";
        }
        return "redirect:/order/all";
    }

    @GetMapping(value = {"/material/add/{orderId}", "/material/add/"})
    public String addMaterial(@PathVariable("orderId") Optional<Integer> id, Model model, HttpSession session) {
        int orderId = getOrderId(id, session);
        if (orderId == -1) {
            return "redirect:/order/all";
        }
        Order order = orderService.findById(orderId);
        List<Material> materials = stockService.findAllByOrderId(orderId);
        List<Material> stockMaterials = stockService.findAll();
        model.addAttribute("materials", materials);
        model.addAttribute("stockMaterials", stockMaterials);
        model.addAttribute("id", orderId);
        model.addAttribute("order", order);
        return "order/chooseMaterials";
    }

    @PostMapping("/material/add/changeStockCount/{orderId}")
    public String addMaterial(@PathVariable("orderId") int orderId, int count, int materialId, HttpSession session) {
        Material material = stockService.findById(materialId);
        if (material != null) {
            if (stockService.changeStockMaterialCount(material, orderId, count)) {
                session.setAttribute("messageComplete", true);
            } else {
                session.setAttribute("thisAmountOfMaterialIsOutOfStock", true);
            }
        } else {
            session.setAttribute("materialNotFound", true);
        }
        session.setAttribute("orderId", orderId);
        return "redirect:/order/material/add/";
    }

    @GetMapping("/add")
    public String addOrder(Model model, HttpSession session) {
        List<Brigade> brigades = brigadeService.findAll();
        List<Integer> freeBrigades = brigadeService.getFreeBrigadeIds(brigades);
        List<ConstructObject> objects = constructObjectService.findAll();
        List<User> users = userService.findPersons();
        model.addAttribute("users", users);
        model.addAttribute("brigades", freeBrigades);
        model.addAttribute("objects", objects);
        model.addAttribute("saveOrder", new SaveOrderDto());
        return "order/addOrder";
    }

    @PostMapping("/add")
    public String addOrder(@ModelAttribute("saveOrder") SaveOrderDto saveOrderDto, Model model, HttpSession session) {
        Order order = new Order();
        order.setAddress(saveOrderDto.getAddress());
        order.setOrderStatus(saveOrderDto.getOrderStatus());
        order.setEndOfWork(saveOrderDto.getEndOfWork());
        order.setBeginningOfWork(saveOrderDto.getBeginningOfWork());
        if (userService.existByEmail(saveOrderDto.getEmail())) {
            order.setUser(userService.findByEmail(saveOrderDto.getEmail()));
            order.setConstructObject(constructObjectService.findByTypeAndSquare(saveOrderDto.getConstructObject()));
            order.setBrigade(brigadeService.findById(saveOrderDto.getBrigadeId()));
            orderService.save(order);
            return "redirect:/order/all";
        } else {
            model.addAttribute("message", "User not found");
        }
        return "order/addOrder";
    }

    @GetMapping("/print/{id}")
    public String print(@PathVariable("id") int id, Model model) {
        Order order = orderService.findById(id);
        List<Material> materials = stockService.findAllByOrderId(order.getId());
        setSuppliers(materials);
        order.setMaterials(materials);
        orderService.setCost(order);
        model.addAttribute("order", order);

        return "user/invoice";
    }

    @GetMapping("/iosu/chooseQuery")
    public String chooseQuery() {
        return "iosu/choosePage";
    }

    @GetMapping("/iosu/union")
    public String union(Model model) {
        List<String> union = stockService.union();
        List<Integer> numbers = new ArrayList<>();
        int i = 0;
        for (String s : union) {
            numbers.add(++i);
        }
        UnionDto un = new UnionDto();
        un.setNumbers(numbers);
        un.setNames(union);
        model.addAttribute("union", un);
        return "iosu/union";
    }

    @GetMapping("/iosu/uslovn")
    public String uslovn(Model model, HttpSession session) {
        List<Order> ordersParam = (List<Order>) session.getAttribute("ordersUslovn");
        List<Order> param;
        if (ordersParam == null) {
            param = orderService.findAll();
        } else {
            param = ordersParam;
        }
        model.addAttribute("orders", param);
        model.addAttribute("status", new Status());
        model.addAttribute("statuses", OrderStatus.getAll());
        return "iosu/uslovn";
    }

    @PostMapping("/iosu/uslovn")
    public String param(@ModelAttribute("save") Status status, Model model, HttpSession session) {
        OrderStatus orderStatus = status.getOrderStatus();
        List<Order> ordersByOrderStatus = orderService.findAllByStatus(orderStatus);
        session.setAttribute("ordersUslovn", ordersByOrderStatus);
        return "redirect:/order/iosu/uslovn";
    }

    @GetMapping("/iosu/param")
    public String param(Model model, HttpSession session) {
        List<Order> ordersParam = (List<Order>) session.getAttribute("ordersParam");
        List<Order> param;
        if (ordersParam == null) {
            param = orderService.findAll();
        } else {
            param = ordersParam;
        }
        model.addAttribute("orders", param);
        model.addAttribute("dates", new UpdateOrder());
        model.addAttribute("statuses", OrderStatus.getAll());
        return "iosu/param";
    }

    @PostMapping("/iosu/param")
    public String param(@ModelAttribute("dates") UpdateOrder dates, Model model, HttpSession session) {
        Order order = mapper.map(dates, Order.class);
        List<Order> ordersByDate = orderService.findAllByDates(order.getBeginningOfWork(), order.getEndOfWork());
        session.setAttribute("ordersParam", ordersByDate);
        return "redirect:/order/iosu/param";
    }

    @GetMapping("/iosu/itogovy")
    public String itogovy(Model model, HttpSession session) {
        List<ItogovyDto> itogovySession = (List<ItogovyDto>) session.getAttribute("itogovy");
        if (itogovySession == null || itogovySession.isEmpty()) {
            itogovySession = stockService.itogovy(null);
        }
        for (ItogovyDto itogovyDto : itogovySession) {
            itogovyDto.setSupplier(supplierService.findByName(itogovyDto.getSupplierName()));
        }
        model.addAttribute("itogovy", itogovySession);
        model.addAttribute("date", LocalDate.now());
        return "iosu/itogovy";
    }

    @PostMapping("/iosu/itogovy")
    public String itogovy(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date, Model model, HttpSession session) {
        List<ItogovyDto> itogovyDtos = stockService.itogovy(date);
        for (ItogovyDto itogovyDto : itogovyDtos) {
            itogovyDto.setSupplier(supplierService.findByName(itogovyDto.getSupplierName()));
        }
        model.addAttribute("itogovy", itogovyDtos);
        model.addAttribute("date", date);
        return "iosu/itogovy";
    }

    @GetMapping("/iosu/perekrest")
    public String perekrest(Model model) {
        List<PerekrestDto> perekrestDtos = supplierService.perekrest();
        model.addAttribute("perekrest", perekrestDtos);
        return "iosu/perekrest";
    }


    private int getOrderId(Optional<Integer> id, HttpSession session) {
        int orderId;
        if (id.isEmpty() || id.get() == 0) {
            orderId = (int) session.getAttribute("orderId");
            if (session.getAttribute("orderId") == null) {
                return -1;
            }
            session.setAttribute("orderId", null);
        } else {
            orderId = id.get();
        }
        return orderId;
    }

    private void setSuppliers(List<Material> materials) {
        for (int i = 0; i < materials.size(); i++) {
            Supplier supplier = supplierService.findById(materials.get(i).getId());
            materials.get(i).setSupplier(supplier);
        }
    }
}
