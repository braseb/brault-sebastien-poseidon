package com.poseidon.app.services;

import com.poseidon.app.domain.Trade;
import com.poseidon.app.dto.TradeDto;
import com.poseidon.app.repositories.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;
    
    @InjectMocks
    private TradeService tradeService;

    private Trade trade;
    

    @BeforeEach
    void setUp() {
        trade = new Trade("accountTest", "typeTest", 3.0);
        
    }

    
    @Test
    void save_ShouldCreateNewTrade() {
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        TradeDto result = tradeService.save(new TradeDto(1,
                                                           trade.getAccount(),
                                                           trade.getType(),
                                                           trade.getBuyQuantity()));

        assertEquals("accountTest", result.account());
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    void save_ShouldUpdateExistingTrade_WhenIdExists(){
        when(tradeRepository.existsById(1)).thenReturn(true);
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);

        TradeDto updatedDto = new TradeDto(1, "accountNew", "typeNew", 5.0);
        TradeDto result;
        result = tradeService.save(updatedDto);
       
        assertEquals("accountNew", result.account());
        assertEquals("typeNew", result.type());
        assertEquals(5.0, result.buyQuantity());
        verify(tradeRepository).save(any(Trade.class));
    }

    
    @Test
    void getAll_ShouldReturnListOfTrades() {
        when(tradeRepository.findAll()).thenReturn(List.of(trade));

        List<TradeDto> result = tradeService.getAll();

        assertEquals(1, result.size());
        assertEquals("accountTest", result.get(0).account());
        verify(tradeRepository).findAll();
    }

   
    @Test
    void getTradeById_ShouldReturnTrade_WhenFound() {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));

        TradeDto result = tradeService.getTradeById(1);

        assertEquals("accountTest", result.account());
        verify(tradeRepository).findById(1);
    }

    @Test
    void getTradeById_ShouldThrowException_WhenNotFound() {
        when(tradeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> tradeService.getTradeById(1));
    }

    
    @Test
    void deleteTradeById_ShouldCallRepository() {
        tradeService.deleteTradeById(1);
        verify(tradeRepository).deleteById(1);
    }
    
}

