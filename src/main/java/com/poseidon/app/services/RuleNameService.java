package com.poseidon.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.RuleName;
import com.poseidon.app.dto.RuleNameDto;
import com.poseidon.app.repositories.RuleNameRepository;

@Service
public class RuleNameService {

    @Autowired
    RuleNameRepository RuleNameRepository;
    
    public RuleNameDto save(RuleNameDto ruleNameDto) {
        
        RuleName ruleName;
        if (ruleNameDto.id() != null && RuleNameRepository.existsById(ruleNameDto.id())) {
            ruleName = RuleNameRepository.findById(ruleNameDto.id()).orElseThrow();
            ruleName.setName(ruleNameDto.name());
            ruleName.setDescription(ruleNameDto.description());
            ruleName.setJson(ruleNameDto.json());
            ruleName.setTemplate(ruleNameDto.template());
            ruleName.setSqlStr(ruleNameDto.sqlStr());
            ruleName.setSqlPart(ruleNameDto.sqlPart());
        } else {
            ruleName = new RuleName(
                    ruleNameDto.name(),
                    ruleNameDto.description(), 
                    ruleNameDto.json(),
                    ruleNameDto.template(),
                    ruleNameDto.sqlStr(),
                    ruleNameDto.sqlPart()
            );
        }
        
        RuleName saved = RuleNameRepository.save(ruleName);
                
        
        return new RuleNameDto(   saved.getId(),
                                    saved.getName(),
                                    saved.getDescription(),
                                    saved.getJson(),
                                    saved.getTemplate(),
                                    saved.getSqlStr(),
                                    saved.getSqlPart());
    }
    
    public List<RuleNameDto> getAll(){
        return RuleNameRepository.findAll().stream()
                                            .map(c -> {return new RuleNameDto(c.getId(),
                                                                                c.getName(),
                                                                                c.getDescription(),
                                                                                c.getJson(),
                                                                                c.getTemplate(),
                                                                                c.getSqlStr(),
                                                                                c.getSqlPart());})
                                                                        .collect(Collectors.toList());
    }
    
    public RuleNameDto getRuleNameById(Integer id) {
        RuleName ruleName = RuleNameRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid RuleName Id:" + id));
        
        return new RuleNameDto(  ruleName.getId(),
                                   ruleName.getName(), 
                                   ruleName.getDescription(),
                                   ruleName.getJson(),
                                   ruleName.getTemplate(),
                                   ruleName.getSqlStr(),
                                   ruleName.getSqlPart());
                                   
    }
    
    public void deleteRuleNameById(Integer id) {
        RuleNameRepository.deleteById(id);
    }
    
}
