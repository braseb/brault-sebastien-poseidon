package com.poseidon.app.controllers;

import com.poseidon.app.dto.RuleNameDto;
import com.poseidon.app.services.RuleNameService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RuleNameController {
    @Autowired
    RuleNameService ruleNameService;

    @GetMapping("/ruleName/list")
    public String home(Model model)
    {
        model.addAttribute("ruleNames", ruleNameService.getAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleNameDto ruleNameDto) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleNameDto ruleNameDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleNameDto", ruleNameDto);
            return "/ruleName/add";
        }
        ruleNameService.save(ruleNameDto);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("ruleNameDto", ruleNameService.getRuleNameById(id));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable Integer id, @Valid RuleNameDto ruleNameDto,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleNameDto", ruleNameDto);
            return "/ruleName/update";
        }
        ruleNameService.save(ruleNameDto);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable Integer id, Model model) {
        ruleNameService.deleteRuleNameById(id);
        return "redirect:/ruleName/list";
    }
}
