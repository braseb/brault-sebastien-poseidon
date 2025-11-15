package com.poseidon.app.services;

import com.poseidon.app.domain.RuleName;
import com.poseidon.app.dto.RuleNameDto;
import com.poseidon.app.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RuleNameServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;
    
    @InjectMocks
    private RuleNameService ruleNameService;

    private RuleName ruleName;
    

    @BeforeEach
    void setUp() {
        ruleName = new RuleName("name", "description", "json", "template", "sqlStr", "sqlPart");
        
    }

    
    @Test
    void save_ShouldCreateNewRuleName() {
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);

        RuleNameDto result = ruleNameService.save(new RuleNameDto(1,
                                                           ruleName.getName(),
                                                           ruleName.getDescription(),
                                                           ruleName.getJson(),
                                                           ruleName.getTemplate(),
                                                           ruleName.getSqlStr(),
                                                           ruleName.getSqlPart()));

        assertEquals("name", result.name());
        assertEquals("description", result.description());
        assertEquals("json", result.json());
        verify(ruleNameRepository, times(1)).save(any(RuleName.class));
    }

    @Test
    void save_ShouldUpdateExistingRuleName_WhenIdExists(){
        when(ruleNameRepository.existsById(1)).thenReturn(true);
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);

        RuleNameDto updatedDto = new RuleNameDto(1, "nameNew","descriptionNew","jsonNew", "templateNew", "sqlStrNew", "sqlPartNew");
        RuleNameDto result;
        result = ruleNameService.save(updatedDto);
       
        assertEquals("nameNew", result.name());
        assertEquals("descriptionNew", result.description());
        assertEquals("jsonNew", result.json());
        assertEquals("templateNew", result.template());
        verify(ruleNameRepository).save(any(RuleName.class));
    }

    
    @Test
    void getAll_ShouldReturnListOfRuleNames() {
        when(ruleNameRepository.findAll()).thenReturn(List.of(ruleName));

        List<RuleNameDto> result = ruleNameService.getAll();

        assertEquals(1, result.size());
        assertEquals("json", result.get(0).json());
        verify(ruleNameRepository).findAll();
    }

   
    @Test
    void getRuleNameById_ShouldReturnRuleName_WhenFound() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));

        RuleNameDto result = ruleNameService.getRuleNameById(1);

        assertEquals("json", result.json());
        verify(ruleNameRepository).findById(1);
    }

    @Test
    void getRuleNameById_ShouldThrowException_WhenNotFound() {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> ruleNameService.getRuleNameById(1));
    }

    
    @Test
    void deleteRuleNameById_ShouldCallRepository() {
        ruleNameService.deleteRuleNameById(1);
        verify(ruleNameRepository).deleteById(1);
    }
    
}

