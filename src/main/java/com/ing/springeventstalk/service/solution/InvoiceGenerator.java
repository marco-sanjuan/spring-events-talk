package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class InvoiceGenerator {

    @TransactionalEventListener //default phase AFTER_COMMIT
    @Order(2)
    @Transactional(propagation = Propagation.REQUIRES_NEW) //stats new transaction
    public void generate(OrderCreated event){

        //DO THE STUFF

        log.info("Generate invoice of fail without rollback for {}",event.getShoppingCart());
    }
}
