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

//import javax.validation.Valid;


@Controller
public class BidListController {
    // TODO: Inject Bid service
    @Autowired
    BidListRepository bidListRepository;

    @GetMapping("/bidList/list")
    public String home(Model model)
    {
       //System.out.println(request.getRemoteUser());
       model.addAttribute("bidLists", bidListRepository.findAll());
       return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return bid list
       if (result.hasErrors()){
           model.addAttribute("bidList", bid);
           return "bidList/add";
       }
        bidListRepository.save(bid);
        
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        model.addAttribute("bidList", bidListRepository.findById(id).get());
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid
        if (result.hasErrors()) {
            model.addAttribute("bidList", bidList);
            return "/bidList/update";
        }
        bidListRepository.save(bidList);
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        
        bidListRepository.deleteById(id);
        return "redirect:/bidList/list";
    }
}
