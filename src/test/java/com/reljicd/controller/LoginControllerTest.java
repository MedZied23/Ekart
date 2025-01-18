package com.reljicd.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private Principal principal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_WhenUserNotAuthenticated_ReturnsLoginView() {
        String result = loginController.login(null);
        assertEquals("/login", result);
    }

    @Test
    void login_WhenUserAuthenticated_RedirectsToHome() {
        when(principal).thenReturn(principal);
        String result = loginController.login(principal);
        assertEquals("redirect:/home", result);
    }
}
