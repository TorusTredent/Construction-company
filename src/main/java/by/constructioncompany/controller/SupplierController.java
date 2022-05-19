package by.constructioncompany.controller;

import by.constructioncompany.dto.SaveSupplierDto;
import by.constructioncompany.entity.stock.Material;
import by.constructioncompany.entity.stock.Supplier;
import by.constructioncompany.service.StockService;
import by.constructioncompany.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private StockService stockService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/all")
    public String suppliers(Model model, HttpSession session) {
        List<Supplier> suppliers = supplierService.findAll();

        model.addAttribute("suppliers", suppliers);
        model.addAttribute("saveSupplier", new SaveSupplierDto());
        model.addAttribute("objectIsAlreadyExist", session.getAttribute("objectIsAlreadyExist"));
        model.addAttribute("messageComplete", session.getAttribute("messageComplete"));
        model.addAttribute("messageWarning", session.getAttribute("messageWarning"));
        model.addAttribute("objectNotFount", session.getAttribute("objectNotFount"));

        session.setAttribute("objectIsAlreadyExist", null);
        session.setAttribute("messageComplete", null);
        session.setAttribute("messageWarning", null);
        session.setAttribute("objectNotFount", null);
        return "supplier/suppliers";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("saveSupplier") SaveSupplierDto saveSupplierDto, Model model, HttpSession session) {
        Supplier supplier = mapper.map(saveSupplierDto, Supplier.class);
        if (supplierService.existByName(supplier.getName())) {
            session.setAttribute("objectIsAlreadyExist", "Object is already exist");
        } else {
            supplierService.save(supplier);
            session.setAttribute("messageComplete", "Success");
        }
        return "redirect:/supplier/all";
    }

    @PostMapping("/delete/")
    public String delete(int id, Model model, HttpSession session) {
        if (supplierService.delete(id)) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The supplier wasn't delete");
        }

        return "redirect:/supplier/all";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("saveSupplier") SaveSupplierDto saveSupplierDto, Model model, HttpSession session) {
        Supplier supplier = mapper.map(saveSupplierDto, Supplier.class);
        Supplier supplierDb = supplierService.findById(supplier.getId());
        supplier.setId(supplierDb.getId());
        if (supplierService.update(supplier, supplierDb.getId())) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The object wasn't update");
        }
        if (saveSupplierDto.getCount() == -1) {
            session.setAttribute("supplierId", supplierDb.getId());
            return "redirect:/supplier/selectedSupplier/";
        }
        return "redirect:/supplier/all";
    }

    @GetMapping(value = {"/selectedSupplier/{id}", "/selectedSupplier/"})
    public String selected(@PathVariable("id") Optional<Integer> id, Model model, HttpSession session) {
        Supplier supplier;
        if (id.isEmpty() || id.get() == 0) {
            int supplierId = (int) session.getAttribute("supplierId");
            supplier = supplierService.findById(supplierId);
            session.setAttribute("supplierId", null);
        } else {
            supplier = supplierService.findById(id.get());
        }
        List<Material> materials = stockService.findBySupplierId(supplier.getId());

        model.addAttribute("supplier", supplier);
        model.addAttribute("materials", materials);
        model.addAttribute("saveSupplier", new

                SaveSupplierDto());
        return "supplier/selectedSupplier";
    }
}
