package com.poseidon.app.controllers;


import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class CustomErrorController implements ErrorController {
        
    @RequestMapping("/error")
    public ModelAndView handleError(HttpServletRequest httpServletRequest) {
        Object status = httpServletRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
                 
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
        
            if(statusCode == HttpStatus.NOT_FOUND.value()) {
                ModelAndView mav = new ModelAndView();
                String errorMessage= "Page not found.";
                mav.addObject("errorMsg", errorMessage);
                mav.setViewName("404");
                return mav;
                
            }
            else if(statusCode == HttpStatus.FORBIDDEN.value()) {
                ModelAndView mav = new ModelAndView();
                String errorMessage= "You are not authorized for the requested data.";
                mav.addObject("errorMsg", errorMessage);
                mav.setViewName("403");
                return mav;
            }
            
        }
        ModelAndView mav = new ModelAndView();
        String errorMessage= "Internal error.";
        mav.addObject("errorMsg", errorMessage);
        mav.setViewName("500");
        return mav;
    }

}
