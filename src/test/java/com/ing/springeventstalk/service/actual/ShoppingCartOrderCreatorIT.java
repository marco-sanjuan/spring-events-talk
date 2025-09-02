package com.ing.springeventstalk.service.actual;

import com.ing.springeventstalk.config.TestTransactionConfig;
import com.ing.springeventstalk.domain.ShoppingCart;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Import(TestTransactionConfig.class)
@Transactional
class ShoppingCartOrderCreatorIT {

    @Autowired
    private ShoppingCartOrderCreator shoppingCartOrderCreator;

    @MockitoBean
    @Qualifier("actualStockAllocator")
    private StockAllocator stockAllocator;

    @MockitoBean
    @Qualifier("actualInvoiceGenerator")
    private InvoiceGenerator invoiceGenerator;

    @MockitoBean
    @Qualifier("actualConfirmationEmailSender")
    private ConfirmationEmailSender confirmationEmailSender;

    @MockitoBean
    @Qualifier("actualCrossSellingManager")
    private CrossSellingManager crossSellingManager;

    @Test
    void shouldCallAllSecondaryCollaboratorsWhenOrdering() {
        // Given
        ShoppingCart shoppingCart = new ShoppingCart();

        // When
        shoppingCartOrderCreator.order(shoppingCart);

        //NO FORCE COMMIT NEEDED

        // Then - verificar que todos los event listeners se ejecutan
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

        //NO FORCE COMMIT NEEDED

        // Then - verificar el orden de las llamadas
        var inOrder = inOrder(stockAllocator, invoiceGenerator, confirmationEmailSender, crossSellingManager);
        inOrder.verify(stockAllocator).allocate(shoppingCart);
        inOrder.verify(invoiceGenerator).generate(shoppingCart);
        inOrder.verify(confirmationEmailSender).send(shoppingCart);
        inOrder.verify(crossSellingManager).manage(shoppingCart);
    }
}
