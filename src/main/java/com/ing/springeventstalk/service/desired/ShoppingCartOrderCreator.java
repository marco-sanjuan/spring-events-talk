package com.ing.springeventstalk.service.desired;

import com.ing.springeventstalk.domain.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class ShoppingCartOrderCreator {

    private final StockAllocator stockAllocator;
    
    private final ConfirmationEmailSender confirmationEmailSender;
    
    private final CrossSellingManager crossSellingManager;
    
    private final InvoiceGenerator invoiceGenerator;

    public void order(ShoppingCart shoppingCart){

        //***
        // SOME ORDER STUFF
        //***

        //Sync in same TX
        stockAllocator.allocate(shoppingCart);

        //Sync in NEW TX on AFTERR_COMMIT
        // - si funciona, podemos devolver la invoiceId
        // - si falla no deshace el pedido, no se devuelve invoice y se intenta generar más tarde
        invoiceGenerator.generate(shoppingCart);

        //***
        // SOME FINAL ORDER STUFF
        //***

        //Async (Nueva TX)
        confirmationEmailSender.send(shoppingCart);


        //Async (Nueva TX)
        crossSellingManager.manage(shoppingCart);

        // FUTURE FEATURES?

        //TODO
        // - Error management? what if anything fails? Rollback all? transactionality?
        // - SOlid?
        // - future features?
    }
}
