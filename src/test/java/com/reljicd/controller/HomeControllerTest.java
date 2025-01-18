package com.reljicd.controller;

import com.reljicd.model.Product;
import com.reljicd.service.ProductService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HomeControllerTest {

    @Mock
    private ProductService productService;

    private HomeController homeController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        homeController = new HomeController(productService);
    }

    @Test
    public void testHomeWithoutPageParam() {
        // Arrange
        Page<Product> productPage = new PageImpl<>(new ArrayList<>(), new PageRequest(0, 5), 0);
        when(productService.findAllProductsPageable(any(PageRequest.class))).thenReturn(productPage);

        // Act
        ModelAndView modelAndView = homeController.home(Optional.empty());

        // Assert
        assertNotNull(modelAndView);
        assertEquals("/home", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("products"));
        assertNotNull(modelAndView.getModel().get("pager"));
    }

    @Test
    public void testHomeWithPageParam() {
        // Arrange
        Page<Product> productPage = new PageImpl<>(new ArrayList<>(), new PageRequest(1, 5), 0);
        when(productService.findAllProductsPageable(any(PageRequest.class))).thenReturn(productPage);

        // Act
        ModelAndView modelAndView = homeController.home(Optional.of(2));

        // Assert
        assertNotNull(modelAndView);
        assertEquals("/home", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("products"));
        assertNotNull(modelAndView.getModel().get("pager"));
    }
}
