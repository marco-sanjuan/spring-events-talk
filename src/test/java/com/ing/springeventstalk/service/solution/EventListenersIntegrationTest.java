package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import com.ing.springeventstalk.domain.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.TestPropertySource;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.main.allow-bean-definition-overriding=true"
})
class EventListenersIntegrationTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @SpyBean
    @Qualifier("solutionStockAllocator")
    private StockAllocator stockAllocator;

    @SpyBean
    @Qualifier("solutionInvoiceGenerator")
    private InvoiceGenerator invoiceGenerator;

    @SpyBean
    @Qualifier("solutionConfirmationEmailSender")
    private ConfirmationEmailSender confirmationEmailSender;

    @SpyBean
    @Qualifier("solutionCrossSellingManager")
    private CrossSellingManager crossSellingManager;

    @Test
    void shouldExecuteAllEventListenersWhenOrderCreatedEventIsPublished() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();
        OrderCreated orderCreated = OrderCreated.of(shoppingCart);

        // When
        applicationEventPublisher.publishEvent(orderCreated);

        // Then - verificar que todos los event listeners se ejecutan
        verify(stockAllocator, times(1)).allocate(orderCreated);
        verify(invoiceGenerator, times(1)).generate(orderCreated);
        verify(confirmationEmailSender, times(1)).send(orderCreated);
        verify(crossSellingManager, times(1)).manage(orderCreated);
    }

    @Test
    void shouldExecuteEventListenersInCorrectOrder() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();
        OrderCreated orderCreated = OrderCreated.of(shoppingCart);

        // When
        applicationEventPublisher.publishEvent(orderCreated);

        // Then - verificar el orden de ejecución basado en @Order
        var inOrder = inOrder(stockAllocator, invoiceGenerator, confirmationEmailSender, crossSellingManager);
        inOrder.verify(stockAllocator).allocate(orderCreated);        // @Order(1)
        inOrder.verify(invoiceGenerator).generate(orderCreated);      // @Order(2)
        inOrder.verify(confirmationEmailSender).send(orderCreated);   // @Order(3)
        inOrder.verify(crossSellingManager).manage(orderCreated);     // @Order(4)
    }

    @Test
    void shouldExecuteEventListenersWhenShoppingCartOrderCreatorPublishesEvent() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When - simular el flujo completo desde ShoppingCartOrderCreator
        applicationEventPublisher.publishEvent(OrderCreated.of(shoppingCart));

        // Then - verificar que todos los colaboradores secundarios se ejecutan
        verify(stockAllocator, times(1)).allocate(any(OrderCreated.class));
        verify(invoiceGenerator, times(1)).generate(any(OrderCreated.class));
        verify(confirmationEmailSender, times(1)).send(any(OrderCreated.class));
        verify(crossSellingManager, times(1)).manage(any(OrderCreated.class));
    }
}
