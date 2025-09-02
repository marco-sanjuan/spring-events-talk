package com.ing.springeventstalk.service.actual;

import com.ing.springeventstalk.domain.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartOrderCreatorTest {

    @Mock
    private StockAllocator stockAllocator;

    @Mock
    private ConfirmationEmailSender confirmationEmailSender;

    @Mock
    private CrossSellingManager crossSellingManager;

    @Mock
    private InvoiceGenerator invoiceGenerator;

    @InjectMocks
    private ShoppingCartOrderCreator shoppingCartOrderCreator;

    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
    }

    @Test
    void order_ShouldCallAllCollaboratorsInCorrectOrder() {
        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then
        // Verificar que se llaman todos los colaboradores con el shoppingCart correcto
        verify(stockAllocator, times(1)).allocate(shoppingCart);
        verify(invoiceGenerator, times(1)).generate(shoppingCart);
        verify(confirmationEmailSender, times(1)).send(shoppingCart);
        verify(crossSellingManager, times(1)).manage(shoppingCart);

        // Verificar que no se llaman otros métodos
        verifyNoMoreInteractions(stockAllocator, confirmationEmailSender, crossSellingManager, invoiceGenerator);
    }

    @Test
    void order_ShouldCallCollaboratorsInSpecificOrder() {
        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then
        // Verificar el orden de las llamadas usando InOrder
        var inOrder = inOrder(stockAllocator, invoiceGenerator, confirmationEmailSender, crossSellingManager);
        
        inOrder.verify(stockAllocator).allocate(shoppingCart);
        inOrder.verify(invoiceGenerator).generate(shoppingCart);
        inOrder.verify(confirmationEmailSender).send(shoppingCart);
        inOrder.verify(crossSellingManager).manage(shoppingCart);
    }
}
