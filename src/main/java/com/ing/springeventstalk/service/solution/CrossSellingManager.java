package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class CrossSellingManager {

    @Async //new thread, does not impact on user experience
    @TransactionalEventListener //default phase AFTER_COMMIT
    @Transactional(propagation = Propagation.REQUIRES_NEW) //stats new transaction
    public void manage(OrderCreated event){

        //DO THE STUFF

        log.info("Manage crossShelling for {}",event.getShoppingCart());
    }
}
