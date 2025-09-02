package com.ing.springeventstalk.service.actual;

import com.ing.springeventstalk.domain.ShoppingCart;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
class ShoppingCartOrderCreatorIntegrationTest {

    @Autowired
    private ShoppingCartOrderCreator shoppingCartOrderCreator;

    @MockitoBean
    @Qualifier("actualStockAllocator")
    private StockAllocator stockAllocator;

    @MockitoBean
    @Qualifier("actualConfirmationEmailSender")
    private ConfirmationEmailSender confirmationEmailSender;

    @MockitoBean
    @Qualifier("actualCrossSellingManager")
    private CrossSellingManager crossSellingManager;

    @MockitoBean
    @Qualifier("actualInvoiceGenerator")
    private InvoiceGenerator invoiceGenerator;

    @Test
    void shouldCallAllSecondaryCollaboratorsWhenOrdering() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then
        verify(stockAllocator, times(1)).allocate(shoppingCart);
        verify(invoiceGenerator, times(1)).generate(shoppingCart);
        verify(confirmationEmailSender, times(1)).send(shoppingCart);
        verify(crossSellingManager, times(1)).manage(shoppingCart);
    }

    @Test
    void shouldCallCollaboratorsInCorrectOrder() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then - verificar el orden de las llamadas
        var inOrder = inOrder(stockAllocator, invoiceGenerator, confirmationEmailSender, crossSellingManager);
        inOrder.verify(stockAllocator).allocate(shoppingCart);
        inOrder.verify(invoiceGenerator).generate(shoppingCart);
        inOrder.verify(confirmationEmailSender).send(shoppingCart);
        inOrder.verify(crossSellingManager).manage(shoppingCart);
    }
}
