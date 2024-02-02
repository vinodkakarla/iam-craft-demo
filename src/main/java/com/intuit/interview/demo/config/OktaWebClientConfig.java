package com.intuit.interview.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * This class is used to configure the WebClient for OAuth2 authentication with Okta.
 * It also configures the security filter chain for the application.
 */
@Configuration
@EnableWebSecurity
public class OktaWebClientConfig {
    // Client registration repository for OAuth2 clients
//    @Autowired
//    ;
    // Repository for authorized OAuth2 clients
//    @Autowired
//    ;

    /**
     * This method creates a WebClient that is configured for OAuth2 authentication with Okta.
     *
     * @return a WebClient instance
     */
    @Bean
    public WebClient webClient(ClientRegistrationRepository clientRegistrations, OAuth2AuthorizedClientRepository authorizedClients) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrations, authorizedClients);
        oauth2.setDefaultOAuth2AuthorizedClient(true);
        oauth2.setDefaultClientRegistrationId("okta");
        return WebClient.builder().apply(oauth2.oauth2Configuration()).build();
    }

    /**
     * This method configures the security filter chain for the application.
     * It allows all requests to pass through.
     *
     * @param http the HttpSecurity instance to configure
     * @return a SecurityFilterChain instance
     * @throws Exception if an error occurs during configuration
     */
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(authorizeRequests ->
//                authorizeRequests.anyRequest().permitAll());
//        return http.build();
//    }

}