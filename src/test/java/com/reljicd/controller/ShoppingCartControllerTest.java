package com.reljicd.controller;

import com.reljicd.exception.NotEnoughProductsInStockException;
import com.reljicd.model.Product;
import com.reljicd.service.ProductService;
import com.reljicd.service.ShoppingCartService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ShoppingCartControllerTest {

    @Mock
    private ShoppingCartService shoppingCartService;

    @Mock
    private ProductService productService;

    private ShoppingCartController shoppingCartController;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        shoppingCartController = new ShoppingCartController(shoppingCartService, productService);
    }

    @Test
    public void testShoppingCart() {
        // Prepare test data
        Map<Product, Integer> products = new HashMap<>();
        when(shoppingCartService.getProductsInCart()).thenReturn(products);
        when(shoppingCartService.getTotal()).thenReturn(new BigDecimal("100.00"));

        // Execute
        ModelAndView modelAndView = shoppingCartController.shoppingCart();

        // Verify
        assertEquals("/shoppingCart", modelAndView.getViewName());
        assertNotNull(modelAndView.getModel().get("products"));
        assertEquals("100.00", modelAndView.getModel().get("total"));
    }

    @Test
public void testAddProductToCart() {
    // Prepare test data
    Product product = new Product();
    product.setId(1L); // Ensure the ID matches
    when(productService.findById(1L)).thenReturn(Optional.of(product));

    // Execute
    ModelAndView modelAndView = shoppingCartController.addProductToCart(1L);

    // Verify
    verify(shoppingCartService).addProduct(product);
    assertEquals("/shoppingCart", modelAndView.getViewName());
}


    @Test
public void testRemoveProductFromCart() {
    // Prepare test data
    Product product = new Product();
    product.setId(1L); // Ensure the ID matches
    when(productService.findById(1L)).thenReturn(Optional.of(product));

    // Execute
    ModelAndView modelAndView = shoppingCartController.removeProductFromCart(1L);

    // Verify
    verify(shoppingCartService).removeProduct(product);
    assertEquals("/shoppingCart", modelAndView.getViewName());
}


    @Test
    public void testCheckoutSuccess() throws NotEnoughProductsInStockException {
        // Execute
        ModelAndView modelAndView = shoppingCartController.checkout();

        // Verify
        verify(shoppingCartService).checkout();
        assertEquals("/shoppingCart", modelAndView.getViewName());
    }

    @Test
    public void testCheckoutWithNotEnoughStock() throws NotEnoughProductsInStockException {
        // Prepare test data
        String errorMessage = "Not enough products in stock";
        doThrow(new NotEnoughProductsInStockException(errorMessage))
            .when(shoppingCartService).checkout();

        // Execute
        ModelAndView modelAndView = shoppingCartController.checkout();

        // Verify
        assertEquals("/shoppingCart", modelAndView.getViewName());
        assertEquals(errorMessage, modelAndView.getModel().get("outOfStockMessage"));
    }
}
