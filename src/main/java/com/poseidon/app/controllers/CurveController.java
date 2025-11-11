package com.poseidon.app.controllers;

import com.poseidon.app.dto.CurvePointDto;
import com.poseidon.app.services.CurvePointService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class CurveController {
        
    @Autowired
    CurvePointService curvePointService;

    @GetMapping("/curvePoint/list")
    public String home(Model model)
    {
        model.addAttribute("curvePoints", curvePointService.getAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePointDto curvePointDto) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePointDto curvePointDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curvePointDto", curvePointDto);
            return "curvePoint/add";
        }
        curvePointService.save(curvePointDto);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("curvePointDto", curvePointService.getCurvePointById(id));
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable Integer id, @Valid CurvePointDto curvePointDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curvePoint", curvePointDto);
            return "curvePoint/update";
        }
        curvePointService.save(curvePointDto);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable Integer id, Model model) {
        curvePointService.deleteCurvePointById(id);
        return "redirect:/curvePoint/list";
    }
}
