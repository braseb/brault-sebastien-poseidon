package com.poseidon.app.controllers;

import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.repositories.CurvePointRepository;

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
    CurvePointRepository curvePointRepository;

    @GetMapping("/curvePoint/list")
    public String home(Model model)
    {
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/add";
        }
        curvePointRepository.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("curvePoint", curvePointRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException("Invalid curvePoint Id:" + id)));
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        }
        curvePointRepository.save(curvePoint);
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable Integer id, Model model) {
        curvePointRepository.deleteById(id);
        return "redirect:/curvePoint/list";
    }
}
