package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.config.TestTransactionConfig;
import com.ing.springeventstalk.domain.OrderCreated;
import com.ing.springeventstalk.domain.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestTransactionConfig.class)
@Transactional
class ShoppingCartOrderCreatorIT {

    @Autowired
    private ShoppingCartOrderCreator shoppingCartOrderCreator;

    @MockitoBean
    @Qualifier("solutionStockAllocator")
    private StockAllocator stockAllocator;

    @MockitoBean
    @Qualifier("solutionInvoiceGenerator")
    private InvoiceGenerator invoiceGenerator;

    @MockitoBean
    @Qualifier("solutionConfirmationEmailSender")
    private ConfirmationEmailSender confirmationEmailSender;

    @MockitoBean
    @Qualifier("solutionCrossSellingManager")
    private CrossSellingManager crossSellingManager;

    @Test
    void shouldCallAllSecondaryCollaboratorsWhenOrdering() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        forceCommit();

        // Then - verificar que todos los event listeners se ejecutan
        verify(stockAllocator, times(1)).allocate(any(OrderCreated.class));
        verify(invoiceGenerator, times(1)).generate(any(OrderCreated.class));
        verify(confirmationEmailSender, times(1)).send(any(OrderCreated.class));
        verify(crossSellingManager, times(1)).manage(any(OrderCreated.class));
    }

    @Test
    void shouldCallCollaboratorsInCorrectOrder() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        forceCommit();

        // Then - verificar el orden de las llamadas
        var inOrder = inOrder(stockAllocator, invoiceGenerator, confirmationEmailSender, crossSellingManager);
        inOrder.verify(stockAllocator).allocate(any(OrderCreated.class));        // @Order(1)
        inOrder.verify(invoiceGenerator).generate(any(OrderCreated.class));      // @Order(2)
        //Los listeners asincronos no garantizan el orden de ejecución
    }




    private static void forceCommit() {
        // Forzar el commit de la transacción para que los @TransactionalEventListener se ejecuten
        TestTransaction.flagForCommit();
        TestTransaction.end();
    }
}
