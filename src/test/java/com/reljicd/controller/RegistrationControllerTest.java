package com.reljicd.controller;

import com.reljicd.model.User;
import com.reljicd.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RegistrationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private BindingResult bindingResult;

    private RegistrationController registrationController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registrationController = new RegistrationController(userService);
    }

    @Test
    public void testRegistrationGet() {
        ModelAndView modelAndView = registrationController.registration();
        
        assertEquals("/registration", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("user"));
    }

    @Test
    public void testSuccessfulRegistration() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setUsername("testuser");

        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        ModelAndView modelAndView = registrationController.createNewUser(user, bindingResult);

        assertEquals("/registration", modelAndView.getViewName());
        assertEquals("User has been registered successfully", modelAndView.getModel().get("successMessage"));
        verify(userService, times(1)).saveUser(user);
    }

    @Test
    public void testRegistrationWithExistingEmail() {
        User user = new User();
        user.setEmail("existing@test.com");
        user.setUsername("newuser");

        when(userService.findByEmail("existing@test.com")).thenReturn(Optional.of(new User()));
        when(userService.findByUsername(anyString())).thenReturn(Optional.empty());

        ModelAndView modelAndView = registrationController.createNewUser(user, bindingResult);

        assertEquals("/registration", modelAndView.getViewName());
        verify(bindingResult).rejectValue(
            eq("email"), 
            eq("error.user"), 
            eq("There is already a user registered with the email provided")
        );
    }

    @Test
    public void testRegistrationWithExistingUsername() {
        User user = new User();
        user.setEmail("new@test.com");
        user.setUsername("existing");

        when(userService.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userService.findByUsername("existing")).thenReturn(Optional.of(new User()));

        ModelAndView modelAndView = registrationController.createNewUser(user, bindingResult);

        assertEquals("/registration", modelAndView.getViewName());
        verify(bindingResult).rejectValue(
            eq("username"), 
            eq("error.user"), 
            eq("There is already a user registered with the username provided")
        );
    }
}
