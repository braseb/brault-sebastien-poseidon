package com.poseidon.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.dto.CurvePointDto;
import com.poseidon.app.repositories.CurvePointRepository;

@Service
public class CurvePointService {

    @Autowired
    CurvePointRepository curvePointRepository;
    
    public CurvePointDto save(CurvePointDto curvePointDto) {
        
        CurvePoint curvePoint;
        if (curvePointDto.id() != null && curvePointRepository.existsById(curvePointDto.id())) {
            curvePoint = curvePointRepository.findById(curvePointDto.id()).orElseThrow();
            curvePoint.setCurveId(curvePointDto.curveId());
            curvePoint.setTerm(curvePointDto.term());
            curvePoint.setValue(curvePointDto.value());
        } else {
            curvePoint = new CurvePoint(
                    curvePointDto.curveId(),
                    curvePointDto.term(), 
                    curvePointDto.value()
            );
        }
        
        CurvePoint saved = curvePointRepository.save(curvePoint);
                
        
        return new CurvePointDto(   saved.getId(),
                                    saved.getCurveId(), 
                                    saved.getTerm(), 
                                    saved.getValue());
    }
    
    public List<CurvePointDto> getAll(){
        return curvePointRepository.findAll().stream()
                                            .map(c -> {return new CurvePointDto(c.getId(),
                                                                                c.getCurveId(),
                                                                                c.getTerm(),
                                                                                c.getValue());})
                                            .collect(Collectors.toList());
    }
    
    public CurvePointDto getCurvePointById(Integer id) {
        CurvePoint curvePoint = curvePointRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid curvePoint Id:" + id));
        
        return new CurvePointDto(  curvePoint.getId(),
                                   curvePoint.getCurveId(), 
                                   curvePoint.getTerm(),
                                   curvePoint.getValue());
    }
    
    public void deleteCurvePointById(Integer id) {
        curvePointRepository.deleteById(id);
    }
    
}
