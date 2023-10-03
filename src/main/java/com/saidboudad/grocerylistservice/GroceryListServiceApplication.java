package com.saidboudad.grocerylistservice;

import com.saidboudad.grocerylistservice.security.Service.AccountService;
import org.springframework.boot.CommandLineRunner;
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







    // Step1 : unsecured app
    //For adding users to my DB via Repository directly.
    //@Bean
//    CommandLineRunner commandLineRunnerRepoDirect(ClientRepository clientRepo) {
//        Client client = new Client(1l, "moooda", "mooooda@gmail.com", "123456", new List<Item>(),new List<ShoppingList>());
//        return args -> {
//            clientRepo.save(client);
//        };
//    }



    // Step2 : secured using JdbcUserDetailsManager
    //For adding Users with roles and authorities to the Jdbc Tables for security purpose
//    @Bean
//    CommandLineRunner commandLineRunnerJdbcUserManager(JdbcUserDetailsManager jdbcUserDetailsManager){
//        PasswordEncoder passwordEncoder=passwordEncoder();
//        return args ->{
//            jdbcUserDetailsManager.createUser(
//                    User.withUsername("said").password(passwordEncoder.encode("111")).roles("USER","ADMIN").build()
//            );
//            jdbcUserDetailsManager.createUser(
//                    User.withUsername("lina").password(passwordEncoder.encode("222")).roles("USER").build()
//            );
//            jdbcUserDetailsManager.createUser(
//                    User.withUsername("anir").password(passwordEncoder.encode("222")).authorities("READ").build()
//            );
//            jdbcUserDetailsManager.createUser(
//                    User.withUsername("third").password(passwordEncoder.encode("222")).roles("USER").authorities("READ").build()
//            );
//        };
//    }


    //Step3 : secured using UserDetailsService
    //For adding Users with roles and authorities to the AppUser and AppRole Tables for security purpose
//    @Bean
    CommandLineRunner commandLineRunnerUserDetailsService(AccountService accountService){
        return args->{

            accountService.addNewRole("USER");
            accountService.addNewRole("ADMIN");
            accountService.addNewUser("said", passwordEncoder().encode("1111"),"said@gmail.com");
            accountService.addNewUser("lina",passwordEncoder().encode("2222"),"lina@gmail.com");
            accountService.addNewUser("samira",passwordEncoder().encode("2222"),"samira@gmail.com");

            accountService.addRoleToUser("said","ADMIN");
            accountService.addRoleToUser("said","USER");
            accountService.addRoleToUser("lina","USER");
            accountService.addRoleToUser("samira","ADMIN");
        };
    }



}
