package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController;

    private ModifyCartRequest request;

    private final Cart cart = Cart.builder()
            .id(1L)
            .build();

    private final User vinny = User.builder()
            .id(2L)
            .username("vinny")
            .password("encoded")
            .cart(cart)
            .build();

    private final Item thingy = Item.builder()
            .id(3L)
            .description("a thingamajig")
            .name("thingy")
            .price(BigDecimal.TEN)
            .build();

    @Before
    public void setup(){
        when(userRepository.findByUsername(vinny.getUsername())).thenReturn(vinny);
        when(itemRepository.findById(thingy.getId())).thenReturn(Optional.of(thingy));

        request = ModifyCartRequest.builder()
                .username(vinny.getUsername())
                .itemId(thingy.getId())
                .quantity(2)
                .build();
    }

    @Test
    public void addToCart(){

        val actual = cartController.addTocart(request);
        val items = actual.getBody().getItems();

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(2,items.size());
        assertTrue(items.stream().allMatch(item -> Objects.equals(item.getId(), thingy.getId())));
    }


    @Test
    public void addToCartNoUserFound(){
        request.setUsername("does not exist");
        val actual = cartController.addTocart(request);

        assertEquals(404, actual.getStatusCodeValue());
    }

    @Test
    public void addToCartNoItemFound(){
        request.setItemId(99L);
        val actual = cartController.addTocart(request);

        assertEquals(404, actual.getStatusCodeValue());
    }

    @Test
    public void removeFromCart(){
        val items = new ArrayList<Item>();
        items.add(thingy);
        items.add(thingy);
        cart.setItems(items);
        vinny.setCart(cart);

        val actual = cartController.removeFromcart(request);
        val actualItems = actual.getBody().getItems();

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(0, actualItems.size());
    }

    @Test
    public void removeFromCartNoUserFound(){
        request.setUsername("does not exist");
        val actual = cartController.removeFromcart(request);

        assertEquals(404, actual.getStatusCodeValue());
    }

    @Test
    public void removeFromCartNoItemFound(){
        request.setItemId(99L);
        val actual = cartController.removeFromcart(request);

        assertEquals(404, actual.getStatusCodeValue());
    }
}
