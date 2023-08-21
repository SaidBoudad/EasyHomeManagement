package com.saidboudad.grocerylistservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public static PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/", "/home").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin((form) -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
//                .logout((logout) -> logout.permitAll());
//
//        return http.build();
//    }
//
//
//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                User.withDefaultPasswordEncoder()
//                        .username("user")
//                        .password("password")
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }
//}
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)  //also @EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;













    //This part is for implementing JDBCAuthentication (deactivated)
    //When using JdbcUserDetailsManager, it expects the necessary tables to be pre-existing in your database, The tables required
    //users: Stores user account information like username, password, and whether the account is enabled.
    //authorities: Stores user roles/authorities associated with the user.
    //group_members: Stores group membership information, which is not always used.
//    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){ //means that I use the DB which in the properties file
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }

    //This part for implementing InMemoryAuthentication (deactivated)
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("2345")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("4567")).roles("USER","ADMIN").build()
        );
        return inMemoryUserDetailsManager;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.csrf().disable(); //this is for disablinging the csrf when we are in stateless security using JWT towken .
        httpSecurity.formLogin().loginPage("/login").defaultSuccessUrl("/home").permitAll();
        httpSecurity.rememberMe();
        httpSecurity.authorizeHttpRequests().anyRequest().authenticated();
        //First way to do the Authorization : URL-based security (HTTP Security) focuses on securing HTTP endpoints
//        httpSecurity.authorizeHttpRequests().requestMatchers("users/admin/**").hasRole("ADMIN");
//        httpSecurity.authorizeHttpRequests().requestMatchers("users/user/**").hasAnyRole("USER","ADMIN");
        //Second way is to use @EnableMethodSecurity(prePostEnabled = true) in SecurityConfig class
        //and the @PreAuthorize("hasRole('USER')") or Admin under endpoints : :GlobaleMethodSecurity at the method level
        httpSecurity.exceptionHandling().accessDeniedPage("/notAuthorized");
        return httpSecurity.build();
    }
}

