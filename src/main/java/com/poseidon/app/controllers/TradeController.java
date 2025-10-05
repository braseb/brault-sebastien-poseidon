package com.poseidon.app.controllers;

import com.poseidon.app.domain.Trade;
import com.poseidon.app.repositories.TradeRepository;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class TradeController {
    @Autowired
    TradeRepository tradeRepository;

    @GetMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", tradeRepository.findAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trade", trade);
            return "trade/add";
        }
        tradeRepository.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("trade", tradeRepository.findById(id)
                                    .orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id)));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("trade", trade);
            return "/trade/update";
        }
        tradeRepository.save(trade);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable Integer id, Model model) {
        tradeRepository.deleteById(id);
        return "redirect:/trade/list";
    }
}
