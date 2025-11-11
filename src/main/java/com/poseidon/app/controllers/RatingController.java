package com.poseidon.app.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.poseidon.app.services.RatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import com.poseidon.app.dto.RatingDto;


@Controller
public class RatingController {
    @Autowired
    RatingService RatingService;
    
    @GetMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", RatingService.getAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(RatingDto ratingDto) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid RatingDto ratingDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ratingDto", ratingDto);
            return "rating/add";
        }
        RatingService.save(ratingDto);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("ratingDto", RatingService.getRatingById(id));                     
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable Integer id, @Valid RatingDto ratingDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ratingDto", ratingDto);
            return "/rating/update";
        }
        RatingService.save(ratingDto);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable Integer id, Model model) {
        RatingService.deleteRatingById(id);
        return "redirect:/rating/list";
    }
}
