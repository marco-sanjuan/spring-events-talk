package com.ing.springeventstalk.service.actual;

import com.ing.springeventstalk.domain.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Component("actualShoppingCartOrderCreator")
@RequiredArgsConstructor
@Transactional
public class ShoppingCartOrderCreator {

    @Qualifier("actualStockAllocator")
    private final StockAllocator stockAllocator;
    
    @Qualifier("actualConfirmationEmailSender")
    private final ConfirmationEmailSender confirmationEmailSender;
    
    @Qualifier("actualCrossSellingManager")
    private final CrossSellingManager crossSellingManager;
    
    @Qualifier("actualInvoiceGenerator")
    private final InvoiceGenerator invoiceGenerator;
    //...

    public void order(ShoppingCart shoppingCart){

        //***
        // SOME ORDER STUFF
        //***

        stockAllocator.allocate(shoppingCart);

        invoiceGenerator.generate(shoppingCart);

        confirmationEmailSender.send(shoppingCart);

        crossSellingManager.manage(shoppingCart);

        // FUTURE FEATURES?
        // ....

        //TODO
        // - Error management? what if anything fails? Rollback all? transactionality?
        // - SOlid?
        // - future features?

    }
}
