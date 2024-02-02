package com.intuit.interview.demo.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class is used to configure the WebClient for OAuth2 authentication with Okta.
 * It also configures the security filter chain for the application.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class OktaWebClientConfig {

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
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated());
        http.oauth2Login(Customizer.withDefaults());

        return http.build();
    }

//    /**
//     * Map authorities from "groups" or "roles" claim in ID Token.
//     *
//     * @return a {@link GrantedAuthoritiesMapper} that maps groups from
//     * the IdP to Spring Security Authorities.
//     */
//    //@Bean
//    public GrantedAuthoritiesMapper userAuthoritiesMapper() {
//        return (authorities) -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//
//            authorities.forEach(authority -> {
//                // Check for OidcUserAuthority because Spring Security 5.2 returns
//                // each scope as a GrantedAuthority, which we don't care about.
//                if (authority instanceof OidcUserAuthority) {
//                    OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
//                    mappedAuthorities.addAll(extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
//                }
//            });
//            return mappedAuthorities;
//        };
//    }
//
////    @Bean
////    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
////        return http.authorizeExchange()
////                .pathMatchers("/actuator/**").permitAll()
////                .anyExchange().authenticated()
////                .and().build();
////    }
//
//    public List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
//        return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
//    }
//
//    @SuppressWarnings("unchecked")
//    private Collection<String> getRolesFromClaims(Map<String, Object> claims) {
//        return (Collection<String>) claims.getOrDefault("groups",
//                claims.getOrDefault("roles", new ArrayList<>()));
//    }
//
//    private List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
//        return roles.stream()
//                .filter(role -> role.startsWith("ROLE_"))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
//    }
}