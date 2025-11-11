package com.poseidon.app.controllers;

import com.poseidon.app.dto.BidListDto;
import com.poseidon.app.services.BidListService;
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
    BidListService bidListService;

    @GetMapping("/bidList/list")
    public String home(Model model)
    {
       model.addAttribute("bidLists", bidListService.getAll());
       return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidListDto bidListDto) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidListDto bidListDto, BindingResult result, Model model) {
       if (result.hasErrors()){
           model.addAttribute("bidListDto", bidListDto);
           return "bidList/add";
       }
       bidListService.save(bidListDto);
        
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("bidListDto", bidListService.getBidListById(id));                         
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable Integer id, @Valid BidListDto bidListDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("bidListDto", bidListDto);
            return "/bidList/update";
        }
        bidListService.save(bidListDto);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable Integer id, Model model) {
        bidListService.deleteBidListById(id);
        return "redirect:/bidList/list";
    }
}
