package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component("solutionConfirmationEmailSender")
@Slf4j
public class ConfirmationEmailSender {

    @Async //new thread, does not impact on user experience
    @TransactionalEventListener //default phase AFTER_COMMIT
    @Transactional(propagation = Propagation.REQUIRES_NEW) //stats new transaction
    //@Order(3) // if async,
    public void send(OrderCreated event){

        //DO THE STUFF

        log.info("Send confirmation email to user for {}",event.getShoppingCart());
    }
}
