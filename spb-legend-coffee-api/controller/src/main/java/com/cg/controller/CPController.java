package com.cg.controller;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.domain.entity.User;
import com.cg.service.staff.IStaffService;
import com.cg.service.user.IUserService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/cp")
public class CPController {

    @Autowired
    private IUserService userService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IStaffService staffService;

    @GetMapping
    public ModelAndView showIndexPage() {
        Optional<StaffDTO> staffDTOOptional = staffService.getByUsernameDTO(appUtils.getUserName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("staff",staffDTOOptional.get());
        modelAndView.setViewName("/cp/index");
        return modelAndView;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "/login";
    }

    @GetMapping("/forget-password")
    public String showForgetPasswordPage() {

        return "/forget-password";
    }

    @GetMapping("/update-password/{codeFirstLogin}")
    public ModelAndView showUpdatePasswordPage(@PathVariable String codeFirstLogin) {
        ModelAndView modelAndView = new ModelAndView();

        Optional<User> userOptional = userService.findByCodeFirstLogin(codeFirstLogin);

        if(userOptional.isPresent()){
            User user = userOptional.get();
            if(user.getIsFirstLogin() && !user.getDeleted()){
                modelAndView.setViewName("/update-password");
                return modelAndView;
            }
        }

        modelAndView.setViewName("/error/expired-page");
        return modelAndView;
    }


}
