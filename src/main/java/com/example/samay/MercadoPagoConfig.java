
package com.example.samay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class MercadoPagoConfig {

    @PostConstruct
    public void init() {
        com.mercadopago.MercadoPagoConfig.setAccessToken("TEST-842565025555115-052521-61af028e01929d96e60ed748860348e4-726255972");
    }


}