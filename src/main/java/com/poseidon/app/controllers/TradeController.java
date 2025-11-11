package com.poseidon.app.controllers;

import com.poseidon.app.dto.TradeDto;
import com.poseidon.app.services.TradeService;
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
    TradeService tradeService;

    @GetMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", tradeService.getAll());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(TradeDto tradeDto) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid TradeDto tradeDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tradeDto", tradeDto);
            return "trade/add";
        }
        tradeService.save(tradeDto);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("tradeDto", tradeService.getTradeById(id));
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable Integer id, @Valid TradeDto tradeDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("tradeDto", tradeDto);
            return "/trade/update";
        }
        tradeService.save(tradeDto);
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable Integer id, Model model) {
        tradeService.deleteTradeById(id);
        return "redirect:/trade/list";
    }
}
