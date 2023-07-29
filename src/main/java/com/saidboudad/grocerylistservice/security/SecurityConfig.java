package com.saidboudad.grocerylistservice.security;

//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig implements WebSecurityConfigurerAdapter {
//
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//
//    public SecurityConfig(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
//        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable() // Disable CSRF for simplicity (consider enabling it for production)
//                .authorizeRequests()
//                .antMatchers("/items").authenticated() // Secure the "/items" endpoint, allowing only authenticated users
//                .antMatchers("/users/current").authenticated() // Secure the "/users/current" endpoint
//                .anyRequest().permitAll() // Allow all other requests without authentication
//                .and()
//                .httpBasic(); // Use HTTP Basic authentication
//    }
//}

//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.SecurityBuilder;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.UserDetailsService;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurer {
//
//    private final UserDetailsService userDetailsService;
//
//    public SecurityConfig(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable() // Disable CSRF for simplicity (consider enabling it for production)
//                .authorizeRequests()
//                .antMatchers("/items").authenticated() // Secure the "/items" endpoint, allowing only authenticated users
//                .antMatchers("/users/current").authenticated() // Secure the "/users/current" endpoint
//                .anyRequest().permitAll() // Allow all other requests without authentication
//        ;
//    }
////    @Override
////    public void init(WebSecurity web) throws Exception {
////        // Configure a custom authorization policy
////        web.authorizeRequests()
////                .antMatchers("/items").hasRole("ADMIN")
////                .anyRequest().permitAll();
////    }
//
//    @Override
//    public void init(SecurityBuilder builder) throws Exception {
//
//    }
//
//    @Override
//    public void configure(SecurityBuilder builder) throws Exception {
//
//    }
//}

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout.permitAll());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}