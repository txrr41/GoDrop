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
        // Log para verificar se o token está sendo carregado
        if (accessToken == null || accessToken.isEmpty() || accessToken.equals("${MP_ACCESS_TOKEN}")) {
            System.err.println("❌ ERRO: Access Token do Mercado Pago NÃO foi carregado!");
            System.err.println("❌ Verifique as variáveis de ambiente!");
        } else {
            System.out.println("✅ MP TOKEN CARREGADO: " + accessToken.substring(0, 20) + "...");
            MercadoPagoConfig.setAccessToken(accessToken);
        }
    }
}