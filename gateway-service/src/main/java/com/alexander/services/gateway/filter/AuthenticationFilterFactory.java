package com.alexander.services.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilterFactory extends AbstractGatewayFilterFactory<AuthenticationFilter> {

    private final AuthenticationFilter authenticationFilter;

    @Autowired
    public AuthenticationFilterFactory(AuthenticationFilter authenticationFilter) {
        super(AuthenticationFilter.class);
        this.authenticationFilter = authenticationFilter;
    }

    @Override
    public GatewayFilter apply(AuthenticationFilter config) {
        return authenticationFilter;
    }

    @Override
    public String name() {
        return "AuthenticationFilter";
    }

}
