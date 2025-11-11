package com.poseidon.app.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.poseidon.app.domain.BidList;
import com.poseidon.app.dto.BidListDto;
import com.poseidon.app.repositories.BidListRepository;


@Service
public class BidListService {

    @Autowired
    BidListRepository bidListRepository;
    
    public BidListDto save(BidListDto bidListDto) {
        
        BidList bidList;
        if (bidListDto.id() != null && bidListRepository.existsById(bidListDto.id())) {
            bidList = bidListRepository.findById(bidListDto.id()).orElseThrow();
            bidList.setAccount(bidListDto.account());
            bidList.setType(bidListDto.type());
            bidList.setBidQuantity(bidListDto.bidQuantity());
        } else {
                bidList = new BidList(
                bidListDto.account(),
                bidListDto.type(),
                bidListDto.bidQuantity()
            );
        }
        
        BidList saved = bidListRepository.save(bidList);

        return new BidListDto(
            saved.getId(),
            saved.getAccount(),
            saved.getType(),
            saved.getBidQuantity()
        );
    }
    
    public List<BidListDto> getAll(){
        return bidListRepository.findAll().stream()
                                            .map(b -> {return new BidListDto(b.getId(),
                                                                                b.getAccount(),
                                                                                b.getType(),
                                                                                b.getBidQuantity());})
                                            .collect(Collectors.toList());
    }
    
    public BidListDto getBidListById(Integer id) {
        BidList BidList = bidListRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException("Invalid BidList Id:" + id));
        
        return new BidListDto(  BidList.getId(),
                                   BidList.getAccount(), 
                                   BidList.getType(),
                                   BidList.getBidQuantity());
    }
    
    public void deleteBidListById(Integer id) {
        bidListRepository.deleteById(id);
    }
    
}
