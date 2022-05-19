package by.constructioncompany.controller;

import by.constructioncompany.dto.stock.ChangeStockCountDto;
import by.constructioncompany.dto.stock.SaveStockDto;
import by.constructioncompany.entity.stock.Material;
import by.constructioncompany.entity.stock.Supplier;
import by.constructioncompany.service.StockService;
import by.constructioncompany.service.SupplierService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {

    @Autowired
    private StockService stockService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private SupplierService supplierService;

    @GetMapping("/all")
    public String materials(Model model, HttpSession session) {
        List<Material> materials = stockService.findAll();
        model.addAttribute("materials", materials);
        model.addAttribute("changeCount", new ChangeStockCountDto());
        return "stock/materials";
    }

    @PostMapping("/material/delete")
    public String delete(@ModelAttribute("changeCount") ChangeStockCountDto countDto, Model model, HttpSession session) {
        Material material = stockService.findByNameAndDate(countDto.getName(), countDto.getDate());
        if (material != null) {
            if (stockService.delete(material.getId())) {
                model.addAttribute("messageComplete", true);
            } else {
                model.addAttribute("TheMaterialWasNotPreserved", true);
            }
        } else {
            model.addAttribute("materialNotFound", true);
        }
        return "redirect:/stock/all";
    }

    @PostMapping("/material/changeCount")
    public String changeCount(@ModelAttribute("changeCount") ChangeStockCountDto countDto, Model model, HttpSession session) {
        Material material = stockService.findByNameAndDate(countDto.getName(), countDto.getDate());
        if (material != null) {
            if (stockService.changeMaterialCount(material, countDto.getCount())) {
                model.addAttribute("messageComplete", true);
            } else {
                model.addAttribute("TheMaterialWasNotPreserved", true);
            }
        } else {
            model.addAttribute("materialNotFound", true);
        }
        return "redirect:/stock/all";
    }

    @GetMapping("/material/add")
    public String addMaterial(Model model, HttpSession session) {
        model.addAttribute("saveStock", new SaveStockDto());
        return "stock/saveMaterial";
    }

    @PostMapping("/material/add")
    public String addMaterial(@ModelAttribute("saveStock") SaveStockDto saveStockDto, BindingResult result, Model model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                return "stock/saveMaterial";
            }
            Material material = mapper.map(saveStockDto, Material.class);
            if (stockService.existByNameAndDate(material.getName(), material.getPurchaseDate())) {
                model.addAttribute("message", "Material is already exist");
                return "stock/saveMaterial";
            } else {
                if (!supplierService.existByName(material.getSupplier().getName())) {
                    supplierService.save(new Supplier(material.getSupplier().getName(), 0));
                }
                Supplier supplier = supplierService.findByName(material.getSupplier().getName());
                material.setSupplier(supplier);
                stockService.save(material);
                return "redirect:/stock/all";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "stock/saveMaterial";
    }
}
