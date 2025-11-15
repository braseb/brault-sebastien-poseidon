package com.poseidon.app.services;

import com.poseidon.app.domain.CurvePoint;
import com.poseidon.app.dto.CurvePointDto;
import com.poseidon.app.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;
    
    @InjectMocks
    private CurvePointService curvePointService;

    private CurvePoint curvePoint;
    

    @BeforeEach
    void setUp() {
        curvePoint = new CurvePoint(1,2.0, 3.0);
        
    }

    
    @Test
    void save_ShouldCreateNewCurvePoint() {
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

        CurvePointDto result = curvePointService.save(new CurvePointDto(1,
                                                           curvePoint.getCurveId(),
                                                           curvePoint.getTerm(),
                                                           curvePoint.getValue()));

        assertEquals(1, result.curveId());
        assertEquals(2.0, result.term());
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
    }

    @Test
    void save_ShouldUpdateExistingCurvePoint_WhenIdExists(){
        when(curvePointRepository.existsById(1)).thenReturn(true);
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

        CurvePointDto updatedDto = new CurvePointDto(1,1,4.0, 5.0);
        CurvePointDto result;
        result = curvePointService.save(updatedDto);
       
        assertEquals(1, result.curveId());
        assertEquals(4.0, result.term());
        assertEquals(5.0, result.value());
        verify(curvePointRepository).save(any(CurvePoint.class));
    }

    
    @Test
    void getAll_ShouldReturnListOfCurvePoints() {
        when(curvePointRepository.findAll()).thenReturn(List.of(curvePoint));

        List<CurvePointDto> result = curvePointService.getAll();

        assertEquals(1, result.size());
        assertEquals(2.0, result.get(0).term());
        verify(curvePointRepository).findAll();
    }

   
    @Test
    void getCurvePointById_ShouldReturnCurvePoint_WhenFound() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));

        CurvePointDto result = curvePointService.getCurvePointById(1);

        assertEquals(2.0, result.term());
        verify(curvePointRepository).findById(1);
    }

    @Test
    void getCurvePointById_ShouldThrowException_WhenNotFound() {
        when(curvePointRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> curvePointService.getCurvePointById(1));
    }

    
    @Test
    void deleteCurvePointById_ShouldCallRepository() {
        curvePointService.deleteCurvePointById(1);
        verify(curvePointRepository).deleteById(1);
    }
    
}

