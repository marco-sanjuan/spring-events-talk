package com.ing.springeventstalk.service.actual;

import com.ing.springeventstalk.domain.ShoppingCart;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class ShoppingCartOrderCreator {

    private final StockAllocator stockAllocator;
    private final ConfirmationEmailSender confirmationEmailSender;
    private final CrossSellingManager crossSellingManager;
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
