package by.constructioncompany.controller;

import by.constructioncompany.dto.AuthUserDto;
import by.constructioncompany.dto.SaveUserDto;
import by.constructioncompany.dto.UpdateUserDto;
import by.constructioncompany.entity.person.user.User;
import by.constructioncompany.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

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
            if (userService.existByEmail(user.getEmail())) {
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

    @GetMapping("/authorization")
    public String authorization(Model model) {
        model.addAttribute("authUser", new User());
        return "/user/authorization";
    }

    @PostMapping("/authorization")
    public String authorization(@ModelAttribute("authUser") AuthUserDto authUserDto, BindingResult result, Model model, HttpSession httpSession) {
        try {
            if (result.hasErrors()) {
                return "user/authorization";
            }
            if(userService.existByEmail(authUserDto.getEmail())) {
                User user = userService.findByEmail(authUserDto.getEmail());
                if (user.getPassword().equals(authUserDto.getPassword())) {
                    httpSession.setAttribute("authUser", user);
                    return "redirect:/";
                } else {
                    model.addAttribute("message", "Password not equals");
                }
            } else {
                model.addAttribute("message", "User not found");
            }
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

    @GetMapping("/logout")
    public String logout(HttpSession httpSession, Model model) {
        httpSession.invalidate();
        model.addAttribute("newUser", new User());
        return "redirect:/";
    }
}
