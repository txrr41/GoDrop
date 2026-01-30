package com.project.shiphub.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MercadoPagoConfigBean {

    @Value("${mp.accesstoken}")
    private String accessToken;

    @PostConstruct
    public void init() {
        System.out.println("MP TOKEN CARREGADO: " + accessToken);
        MercadoPagoConfig.setAccessToken(accessToken);
    }
}

