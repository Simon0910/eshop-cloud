package com.roncoo.eshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author lzp on 2020/3/10.
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class EshopDataLinkServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EshopDataLinkServiceApplication.class, args);
    }

}
