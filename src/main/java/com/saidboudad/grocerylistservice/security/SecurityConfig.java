package com.saidboudad.grocerylistservice.security;

import com.saidboudad.grocerylistservice.security.Service.MyUserDetailsService;
import lombok.AllArgsConstructor;
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

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  //also @EnableGlobalMethodSecurity(prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig {


    private PasswordEncoder passwordEncoder;
    private MyUserDetailsService myUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//      httpSecurity.csrf().disable(); //this is for disablinging the csrf when we are in stateless security using JWT towken

        httpSecurity.authorizeHttpRequests((authz) -> authz.requestMatchers("/")
                                    .permitAll()
                                    .anyRequest()
                                    .authenticated())
                    .formLogin(formLogin -> formLogin.loginPage("/login")
                                    .defaultSuccessUrl("/home")
                                    .permitAll())
                   .exceptionHandling((exceptionHandling) -> exceptionHandling
                                    .accessDeniedPage("/notAuthorized"))
                   .rememberMe(withDefaults());
        httpSecurity.userDetailsService(myUserDetailsService);
        return httpSecurity.build();


        //    First way to do the Authorization : URL-based security (HTTP Security) focuses on securing HTTP endpoints

//        httpSecurity.authorizeHttpRequests().requestMatchers("users/admin/**").hasRole("ADMIN");
//        httpSecurity.authorizeHttpRequests().requestMatchers("users/user/**").hasAnyRole("USER","ADMIN");

        //    Second way is to use @EnableMethodSecurity(prePostEnabled = true) in SecurityConfig class
        //and the @PreAuthorize("hasRole('USER')") or Admin under endpoints : :GlobaleMethodSecurity at the method level


    }



    //     This part for implementing InMemoryAuthentication (deactivated)

    //@Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager(){
        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager(
                User.withUsername("user1").password(passwordEncoder.encode("1234")).roles("USER").build(),
                User.withUsername("user2").password(passwordEncoder.encode("2345")).roles("USER").build(),
                User.withUsername("admin").password(passwordEncoder.encode("4567")).roles("USER","ADMIN").build()
        );
        return inMemoryUserDetailsManager;
    }


    //    This part is for implementing JDBCAuthentication (deactivated)

    //When using JdbcUserDetailsManager, it expects the necessary tables to be pre-existing in your database, The tables required
    //users: Stores user account information like username, password, and whether the account is enabled.
    //authorities: Stores user roles/authorities associated with the user.
    //group_members: Stores group membership information, which is not always used.
    //query to add these tables in the db : CREATE TABLE users (
    //  username VARCHAR(50) NOT NULL,
    //  password VARCHAR(500) NOT NULL,
    //  enabled BOOLEAN NOT NULL DEFAULT TRUE,
    //  PRIMARY KEY (username)
    //);
    //CREATE TABLE authorities (
    //  username VARCHAR(50) NOT NULL,
    //  authority VARCHAR(50) NOT NULL,
    //  UNIQUE (username, authority),
    //  FOREIGN KEY (username) REFERENCES users (username) ON DELETE CASCADE
    //);
    // @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource){ //means that I use the DB which in the properties file
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
        return jdbcUserDetailsManager;
    }

}

