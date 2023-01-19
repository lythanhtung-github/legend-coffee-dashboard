package com.cg.controller;

import com.cg.domain.dto.staff.StaffDTO;
import com.cg.service.staff.IStaffService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;


@Controller
@RequestMapping("/cp/products")
public class ProductCPController {

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private IStaffService staffService;

    @GetMapping
    public ModelAndView showListPage() {

        Optional<StaffDTO> staffDTOOptional = staffService.getByUsernameDTO(appUtils.getUserName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("staff",staffDTOOptional.get());
        modelAndView.setViewName("/cp/product/list");

        return modelAndView;
    }

    @GetMapping("/create")
    public ModelAndView showCreatePage() {

        Optional<StaffDTO> staffDTOOptional = staffService.getByUsernameDTO(appUtils.getUserName());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("staff",staffDTOOptional.get());
        modelAndView.setViewName("/cp/product/create");

        return modelAndView;

    }

    @GetMapping("/{productId}")
    public ModelAndView showViewProduct(@PathVariable Long productId) {

        ModelAndView modelAndView = new ModelAndView();

        Optional<StaffDTO> staffDTOOptional = staffService.getByUsernameDTO(appUtils.getUserName());
        modelAndView.addObject("staff",staffDTOOptional.get());

        modelAndView.setViewName("cp/product/view");

        return modelAndView;
    }
}
