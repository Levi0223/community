package com.levi.community.controller;

import com.levi.community.entity.User;
import com.levi.community.service.UserService;
import com.levi.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserService userService;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage() {

        return "/site/register";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage() {

        return "/site/login";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "Register Success, please verify email as soon as possible");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";
        }
    }

    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        System.out.println(userId);
        System.out.println(code);
        int result = userService.activation(userId, code);
        if (result == ACTIVATE_SUCCESS) {
            model.addAttribute("msg", "Activate success, your account is ready to use");
            model.addAttribute("target", "/login");
        } else if (result == ACTIVATE_REPEAT) {
            model.addAttribute("msg", "Activate repeat, your account has been activated");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "Activate failure, activation code is incorrect");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }
}
