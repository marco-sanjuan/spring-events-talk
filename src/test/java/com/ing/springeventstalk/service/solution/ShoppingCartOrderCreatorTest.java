package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import com.ing.springeventstalk.domain.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShoppingCartOrderCreatorTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private ShoppingCartOrderCreator shoppingCartOrderCreator;

    private ShoppingCart shoppingCart;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
    }

    @Test
    void order_ShouldPublishOrderCreatedEvent() {
        // When
        shoppingCartOrderCreator.order(shoppingCart);

        // Then
        ArgumentCaptor<OrderCreated> eventCaptor = ArgumentCaptor.forClass(OrderCreated.class);
        verify(applicationEventPublisher, times(1)).publishEvent(eventCaptor.capture());
        
        OrderCreated capturedEvent = eventCaptor.getValue();
        assertNotNull(capturedEvent);
        assertEquals(shoppingCart, capturedEvent.getShoppingCart());
    }

}
