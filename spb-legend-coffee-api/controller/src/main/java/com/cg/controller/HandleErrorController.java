package com.cg.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/error")
public class    HandleErrorController {
    @RequestMapping("/400")
    public ModelAndView badRequest() {
        return new ModelAndView("error/400");
    }

    @RequestMapping("/401")
    public ModelAndView unauthorized() {
        return new ModelAndView("error/401");
    }

    @RequestMapping("/403")
    public ModelAndView accessDenied() {
        return new ModelAndView("error/403");
    }

    @RequestMapping("/404")
    public ModelAndView resourceNotFound() {
        return new ModelAndView("error/404");
    }

    @RequestMapping("/405")
    public ModelAndView methodNotAllowed() {
        return new ModelAndView("error/405");
    }

    @RequestMapping("/409")
    public ModelAndView dataConflict() {
        return new ModelAndView("error/409");
    }

    @RequestMapping("/500")
    public ModelAndView internalServerError() {
        return new ModelAndView("error/500");
    }
}
