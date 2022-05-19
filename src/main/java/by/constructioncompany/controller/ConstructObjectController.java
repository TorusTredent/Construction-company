package by.constructioncompany.controller;

import by.constructioncompany.dto.conObject.SaveObjectDto;
import by.constructioncompany.entity.order.ConstructObject;
import by.constructioncompany.entity.order.ConstructObjectType;
import by.constructioncompany.service.ConstructObjectService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/object")
public class ConstructObjectController {
    @Autowired
    private ConstructObjectService constructObjectService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/all")
    public String objects(Model model, HttpSession session) {
        List<ConstructObject> objects = constructObjectService.findAll();

        model.addAttribute("conObjects", objects);
        model.addAttribute("types", ConstructObjectType.getAll());
        model.addAttribute("saveObject", new SaveObjectDto());
        model.addAttribute("objectIsAlreadyExist", session.getAttribute("objectIsAlreadyExist"));
        model.addAttribute("messageComplete", session.getAttribute("messageComplete"));
        model.addAttribute("messageWarning", session.getAttribute("messageWarning"));
        model.addAttribute("objectNotFount", session.getAttribute("objectNotFount"));

        session.setAttribute("ObjectIsAlreadyExist", null);
        session.setAttribute("messageComplete", null);
        session.setAttribute("messageWarning", null);
        session.setAttribute("objectNotFount", null);
        return "constructObject/constructObjects";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("saveObject") SaveObjectDto saveObjectDto, Model model, HttpSession session) {
        ConstructObject object = mapper.map(saveObjectDto, ConstructObject.class);
        if (constructObjectService.existBySquareAndType(object.getSquare(), object.getConstructObjectType())) {
            session.setAttribute("objectIsAlreadyExist", "Object is already exist");
        } else {
            constructObjectService.save(object);
            session.setAttribute("messageComplete", "Success");
        }
        return "redirect:/object/all";
    }

    @PostMapping("/delete")
    public String delete(@ModelAttribute("saveObject") SaveObjectDto saveObjectDto, Model model, HttpSession session) {
        if (constructObjectService.deleteById(saveObjectDto.getId())){
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The object wasn't delete");
        }

        return "redirect:/object/all";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("saveObject") SaveObjectDto saveObjectDto, Model model, HttpSession session) {
        ConstructObject object = mapper.map(saveObjectDto, ConstructObject.class);
        ConstructObject objectDb = constructObjectService.findById(object.getId());
        if (constructObjectService.update(object, objectDb.getId())) {
            session.setAttribute("messageComplete", "Success");
        } else {
            session.setAttribute("messageWarning", "The object wasn't update");
        }
        return "redirect:/object/all";
    }
}
