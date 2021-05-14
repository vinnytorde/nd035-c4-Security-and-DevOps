package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    private final Item thingy = Item.builder()
            .id(3L)
            .description("a thingamajig")
            .name("thingy")
            .price(BigDecimal.TEN)
            .build();

    private final Cart cart = Cart.builder()
            .id(1L)
            .items(Collections.singletonList(thingy))
            .total(BigDecimal.TEN)
            .build();

    private final User vinny = User.builder()
            .id(2L)
            .username("vinny")
            .password("encoded")
            .cart(cart)
            .build();

    private final List<UserOrder> orders = Collections.singletonList(UserOrder.builder().user(vinny).items(cart.getItems()).build());

    @Before
    public void setup(){
        cart.setUser(vinny);
        when(userRepository.findByUsername(vinny.getUsername())).thenReturn(vinny);
        when(orderRepository.findByUser(any())).thenReturn(orders);
    }

    @Test
    public void submit(){
        val actual = orderController.submit(vinny.getUsername());

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(cart.getItems(), actual.getBody().getItems());
        assertEquals(cart.getTotal(), actual.getBody().getTotal());
        assertEquals(vinny, actual.getBody().getUser());
    }

    @Test
    public void submitNoUserFound(){
        val actual = orderController.submit("not vinny");

        assertEquals(404, actual.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUser(){
        val actual = orderController.getOrdersForUser(vinny.getUsername());

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(1, actual.getBody().size());
        assertEquals(cart.getItems(), actual.getBody().iterator().next().getItems());

    }

    @Test
    public void getOrdersForUserUserNotFound(){
        val actual = orderController.getOrdersForUser("not vinny");

        assertEquals(404, actual.getStatusCodeValue());
    }

}
