package com.poseidon.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.Trade;
import com.poseidon.app.dto.TradeDto;
import com.poseidon.app.repositories.TradeRepository;

@Service
public class TradeService {

    @Autowired
    TradeRepository TradeRepository;
    
    public TradeDto save(TradeDto tradeDto) {
        
        Trade trade;
        if (tradeDto.id() != null && TradeRepository.existsById(tradeDto.id())) {
            trade = TradeRepository.findById(tradeDto.id()).orElseThrow();
            trade.setAccount(tradeDto.account());
            trade.setType(tradeDto.type());
            trade.setBuyQuantity(tradeDto.buyQuantity());
            
        } else {
            trade = new Trade(
                    tradeDto.account(),
                    tradeDto.type(),
                    tradeDto.buyQuantity()
                   
            );
        }
        
        Trade saved = TradeRepository.save(trade);
                
        
        return new TradeDto(   saved.getId(),
                                    saved.getAccount(),
                                    saved.getType(),
                                    saved.getBuyQuantity());
    }
    
    public List<TradeDto> getAll(){
        return TradeRepository.findAll().stream()
                                            .map(c -> {return new TradeDto(c.getId(),
                                                                                c.getAccount(),
                                                                                c.getType(),
                                                                                c.getBuyQuantity());})
                                                                        .collect(Collectors.toList());
    }
    
    public TradeDto getTradeById(Integer id) {
        Trade trade = TradeRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid Trade Id:" + id));
        
        return new TradeDto(  trade.getId(),
                                   trade.getAccount(), 
                                   trade.getType(),
                                   trade.getBuyQuantity());
                                   
    }
    
    public void deleteTradeById(Integer id) {
        TradeRepository.deleteById(id);
    }
    
}
