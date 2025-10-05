package com.poseidon.app.controllers;

import com.poseidon.app.domain.RuleName;
import com.poseidon.app.repositories.RuleNameRepository;

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
    RuleNameRepository ruleNameRepository;

    @GetMapping("/ruleName/list")
    public String home(Model model)
    {
        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "/ruleName/add";
        }
        ruleNameRepository.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable Integer id, Model model) {
        model.addAttribute("ruleName", ruleNameRepository.findById(id)
                                        .orElseThrow(() -> new IllegalArgumentException("Invalid rule name id:" + id)));
        return "ruleName/update";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("ruleName", ruleName);
            return "/ruleName/update";
        }
        ruleNameRepository.save(ruleName);
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable Integer id, Model model) {
        ruleNameRepository.deleteById(id);
        return "redirect:/ruleName/list";
    }
}
