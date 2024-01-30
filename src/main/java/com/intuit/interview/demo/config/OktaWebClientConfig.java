package com.intuit.interview.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
@EnableWebSecurity
public class OktaWebClientConfig {
    @Autowired
    ClientRegistrationRepository clientRegistrations;
    @Autowired
    OAuth2AuthorizedClientRepository authorizedClients;

    @Bean
    WebClient webClient() {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrations, authorizedClients);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId("okta");
        return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
    }

}
