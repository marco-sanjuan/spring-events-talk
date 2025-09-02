package com.ing.springeventstalk.service.solution;

import com.ing.springeventstalk.domain.OrderCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("solutionStockAllocator")
@Slf4j
public class StockAllocator {

    @EventListener(OrderCreated.class)
    @Order(1)
    public void allocate(OrderCreated event){

        //DO THE STUFF

        log.info("Allocate stock for {}",event.getShoppingCart());
    }
}
