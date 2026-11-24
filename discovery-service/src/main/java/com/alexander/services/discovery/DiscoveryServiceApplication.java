package com.alexander.services.discovery;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DiscoveryServiceApplication {

    static void main(String[] args) {
        new SpringApplicationBuilder(DiscoveryServiceApplication.class).run(args);
    }

}
