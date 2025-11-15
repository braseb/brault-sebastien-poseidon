package com.poseidon.app.services;

import com.poseidon.app.domain.BidList;
import com.poseidon.app.dto.BidListDto;
import com.poseidon.app.repositories.BidListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidListServiceTest {

    @Mock
    private BidListRepository bidListRepository;
    
    @InjectMocks
    private BidListService bidListService;

    private BidList bidList;
    

    @BeforeEach
    void setUp() {
        bidList = new BidList("accountTest", "typeTest", 3.0);
        
    }

    
    @Test
    void save_ShouldCreateNewBidList() {
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);

        BidListDto result = bidListService.save(new BidListDto(1,
                                                           bidList.getAccount(),
                                                           bidList.getType(),
                                                           bidList.getBidQuantity()));

        assertEquals("accountTest", result.account());
        verify(bidListRepository, times(1)).save(any(BidList.class));
    }

    @Test
    void save_ShouldUpdateExistingBidList_WhenIdExists(){
        when(bidListRepository.existsById(1)).thenReturn(true);
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);

        BidListDto updatedDto = new BidListDto(1, "accountNew", "typeNew", 5.0);
        BidListDto result;
        result = bidListService.save(updatedDto);
       
        assertEquals("accountNew", result.account());
        assertEquals("typeNew", result.type());
        assertEquals(5.0, result.bidQuantity());
        verify(bidListRepository).save(any(BidList.class));
    }

    
    @Test
    void getAll_ShouldReturnListOfBidLists() {
        when(bidListRepository.findAll()).thenReturn(List.of(bidList));

        List<BidListDto> result = bidListService.getAll();

        assertEquals(1, result.size());
        assertEquals("accountTest", result.get(0).account());
        verify(bidListRepository).findAll();
    }

   
    @Test
    void getBidListById_ShouldReturnBidList_WhenFound() {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));

        BidListDto result = bidListService.getBidListById(1);

        assertEquals("accountTest", result.account());
        verify(bidListRepository).findById(1);
    }

    @Test
    void getBidListById_ShouldThrowException_WhenNotFound() {
        when(bidListRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> bidListService.getBidListById(1));
    }

    
    @Test
    void deleteBidListById_ShouldCallRepository() {
        bidListService.deleteBidListById(1);
        verify(bidListRepository).deleteById(1);
    }
    
}

