package com.alexander.services.gateway;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public List<GroupedOpenApi> groupedOpenApis(RouteDefinitionLocator locator) {
        List<RouteDefinition> definitions = locator.getRouteDefinitions().collectList().block();
        assert definitions != null;
        return definitions.stream().filter(routeDefinition -> routeDefinition.getId().matches(".*-service")).map(routeDefinition -> {
            String name = routeDefinition.getId().replace("-service", "");
            return GroupedOpenApi.builder().pathsToMatch("/" + name + "/**").group(name).build();
        }).toList();
    }

}
