package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ItemControllerTests {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    ItemController itemController;

    private Item thingy = Item.builder()
            .id(1L)
            .description("a thingamajig")
            .name("thingy")
            .price(BigDecimal.TEN)
            .build();

    @Before
    public void setup(){
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(thingy));
        when(itemRepository.findById(any())).thenReturn(Optional.of(thingy));
        when(itemRepository.findByName(anyString())).thenReturn(Collections.singletonList(thingy));
    }

    @Test
    public void getItems(){
        val actual = itemController.getItems();

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(1, actual.getBody().size());
        assertEquals(thingy, actual.getBody().iterator().next());
    }

    @Test
    public void getItemById(){
        val actual = itemController.getItemById(1L);

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(thingy, actual.getBody());
    }

    @Test
    public void getItemsByName(){
        val actual = itemController.getItemsByName("thingies");

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(1, actual.getBody().size());
        assertEquals(thingy, actual.getBody().iterator().next());
    }
}
