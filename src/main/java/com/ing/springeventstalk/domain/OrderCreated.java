package com.ing.springeventstalk.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(staticName = "of")
public class OrderCreated {

    private final ShoppingCart shoppingCart;
}
