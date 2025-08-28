package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import com.ing.springeventstalk.domain.ShoppingCart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShoppingCartOrderCreator {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void order(ShoppingCart shoppingCart){

        //***
        // SOME ORDER STUFF
        //***

        log.info("Order placed for {}", shoppingCart);

        applicationEventPublisher.publishEvent(OrderCreated.of(shoppingCart));

        //TODO
        // ✓ Error management? what if anything fails? Rollback all? transactionality?
        // ✓ SOlid?
        // ✓ future features?
    }
}
