package com.poseidon.app.controllers;

import com.poseidon.app.domain.Rating;
import com.poseidon.app.repositories.RatingRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RatingController {
    @Autowired
    RatingRepository ratingRepository;

    @GetMapping("/rating/list")
    public String home(Model model)
    {
        model.addAttribute("ratings", ratingRepository.findAll());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rating", rating);
            return "rating/add";
        }
        ratingRepository.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("rating", ratingRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("Invalid rating id:" + id)));
        return "rating/update";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("rating", rating);
            return "/rating/update";
        }
        ratingRepository.save(rating);
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable Integer id, Model model) {
        ratingRepository.deleteById(id);
        return "redirect:/rating/list";
    }
}
