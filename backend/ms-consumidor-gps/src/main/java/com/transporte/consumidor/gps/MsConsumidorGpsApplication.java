package com.transporte.consumidor.gps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsConsumidorGpsApplication {
    public static void main(String[] args) {
        String walletPath = System.getenv("ORACLE_WALLET_PATH");
        String walletPassword = System.getenv("ORACLE_WALLET_PASSWORD");

        if (walletPath != null && walletPassword != null) {
            System.setProperty("javax.net.ssl.trustStore", walletPath + "/truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", walletPassword);
            System.setProperty("javax.net.ssl.keyStore", walletPath + "/keystore.jks");
            System.setProperty("javax.net.ssl.keyStorePassword", walletPassword);
        }

        SpringApplication.run(MsConsumidorGpsApplication.class, args);
    }
}
