package com.saidboudad.grocerylistservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GroceryListServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroceryListServiceApplication.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(UserRepository userRepo) {
//        User user = new User(null,"moooda","mooooda@gmail.com","sfqdfsdf");
//        return args -> {
//            userRepo.save(user);
//        };
//    }

}
