package com.saidboudad.grocerylistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class GroceryListServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryListServiceApplication.class, args);
    }
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }





    //For populating my DB.
//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepo) {
//        User user = new User("moooda","mooooda@gmail.com","sfqdfsdf");
//        return args -> {
//            userRepo.save(user);
//        };
//    }



}
