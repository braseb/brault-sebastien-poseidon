package com.poseidon.app.controllers;

import com.poseidon.app.domain.BidList;
import com.poseidon.app.repositories.BidListRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class BidListController {
    
    @Autowired
    BidListRepository bidListRepository;

    @GetMapping("/bidList/list")
    public String home(Model model)
    {
       model.addAttribute("bidLists", bidListRepository.findAll());
       return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
       if (result.hasErrors()){
           model.addAttribute("bidList", bid);
           return "bidList/add";
       }
        bidListRepository.save(bid);
        
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("bidList", bidListRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException("Invalid bidList Id:" + id)));
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bidList", bidList);
            return "/bidList/update";
        }
        bidListRepository.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable Integer id, Model model) {
        bidListRepository.deleteById(id);
        return "redirect:/bidList/list";
    }
}
