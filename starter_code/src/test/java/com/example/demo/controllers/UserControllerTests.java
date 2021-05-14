package com.example.demo.controllers;

import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    UserController userController;

    private final String encoded = "encoded";

    private final User vinny = User.builder().id(1L).username("vinny").password(encoded).build();

    @Before
    public void setMocks(){
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encoded);
        when(userRepository.findById(any())).thenReturn(Optional.of(vinny));
        when(userRepository.findByUsername(any())).thenReturn(vinny);
    }

    @Test
    public void findById(){
        val actual = userController.findById(1L);

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(vinny, actual.getBody());
    }

    @Test
    public void findByUserName(){
        val actual = userController.findByUserName(vinny.getUsername());

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals(vinny, actual.getBody());
    }

    @Test
    public void findByUserNameNotFound(){
        val actual = userController.findByUserName(vinny.getUsername());

        assertEquals(404, actual.getStatusCodeValue());
    }

    @Test
    public void create(){
        val request = new CreateUserRequest();
        request.setUsername("vinny");
        request.setPassword("tortilla");
        request.setConfirmPassword("tortilla");

        val actual = userController.createUser(request);

        assertEquals(200, actual.getStatusCodeValue());
        assertEquals("vinny", actual.getBody().getUsername());
        assertEquals(encoded, actual.getBody().getPassword());
    }

    @Test
    public void createExceptionPwdMismatch(){
        val request = new CreateUserRequest();
        request.setUsername("vinny");
        request.setPassword("tortilla");
        request.setConfirmPassword("not tortilla");

        val actual = userController.createUser(request);

        assertEquals(400, actual.getStatusCodeValue());

    }
}
