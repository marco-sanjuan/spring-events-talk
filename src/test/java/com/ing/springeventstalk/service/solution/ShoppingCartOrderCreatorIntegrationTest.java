package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import com.ing.springeventstalk.domain.ShoppingCart;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
class ShoppingCartOrderCreatorIntegrationTest {

    @Autowired
    private ShoppingCartOrderCreator shoppingCartOrderCreator;

    @MockitoSpyBean
    @Qualifier("solutionStockAllocator")
    private StockAllocator stockAllocator;

    @MockitoSpyBean
    @Qualifier("solutionInvoiceGenerator")
    private InvoiceGenerator invoiceGenerator;

    @MockitoSpyBean
    @Qualifier("solutionConfirmationEmailSender")
    private ConfirmationEmailSender confirmationEmailSender;

    @MockitoSpyBean
    @Qualifier("solutionCrossSellingManager")
    private CrossSellingManager crossSellingManager;

    @SneakyThrows
    @Test
    void shouldExecuteAllEventListenersWhenOrdering() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        Thread.sleep(1000);

        // Then - verificar que todos los event listeners se ejecutan
        verify(stockAllocator, times(1)).allocate(any(OrderCreated.class));
        verify(invoiceGenerator, times(1)).generate(any(OrderCreated.class));
        verify(confirmationEmailSender, times(1)).send(any(OrderCreated.class));
        verify(crossSellingManager, times(1)).manage(any(OrderCreated.class));
    }

    @Test
    void shouldExecuteEventListenersInCorrectOrder() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then - verificar el orden de ejecución basado en @Order
        var inOrder = inOrder(stockAllocator, invoiceGenerator, confirmationEmailSender, crossSellingManager);
        inOrder.verify(stockAllocator).allocate(any(OrderCreated.class));        // @Order(1)
        inOrder.verify(invoiceGenerator).generate(any(OrderCreated.class));      // @Order(2)
        inOrder.verify(confirmationEmailSender).send(any(OrderCreated.class));   // @Order(3)
        inOrder.verify(crossSellingManager).manage(any(OrderCreated.class));     // @Order(4)
    }

    @Test
    void shouldExecuteEventListenersWithCorrectShoppingCart() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then - verificar que los event listeners reciben el evento con el ShoppingCart correcto
        verify(stockAllocator, times(1)).allocate(argThat(event -> 
            event instanceof OrderCreated && 
            ((OrderCreated) event).getShoppingCart() == shoppingCart));
        
        verify(invoiceGenerator, times(1)).generate(argThat(event -> 
            event instanceof OrderCreated && 
            ((OrderCreated) event).getShoppingCart() == shoppingCart));
        
        verify(confirmationEmailSender, times(1)).send(argThat(event -> 
            event instanceof OrderCreated && 
            ((OrderCreated) event).getShoppingCart() == shoppingCart));
        
        verify(crossSellingManager, times(1)).manage(argThat(event -> 
            event instanceof OrderCreated && 
            ((OrderCreated) event).getShoppingCart() == shoppingCart));
    }
}
