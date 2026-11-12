package com.alexander.services.gateway.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RouterValidator {

    private final GatewaySecurityProperties properties;

    public boolean isSecured(ServerHttpRequest request) {
        String path = request.getPath().value();
        return properties.getOpenApiEndpoints().stream().noneMatch(path::equals);
    }

}
