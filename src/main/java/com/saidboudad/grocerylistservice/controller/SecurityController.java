package com.saidboudad.grocerylistservice.controller;

import com.saidboudad.grocerylistservice.entity.Client;
import com.saidboudad.grocerylistservice.entity.PasswordResetToken;
import com.saidboudad.grocerylistservice.repository.PasswordResetTokenRepo;
import com.saidboudad.grocerylistservice.service.client.ClientService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Controller
public class SecurityController {


    private ClientService clientService;
    private PasswordEncoder passwordEncoder;
    private JavaMailSender javaMailSender; // Spring's JavaMailSender for sending emails
    private PasswordResetTokenRepo passwordResetTokenRepo;

    public SecurityController(ClientService clientService, PasswordEncoder passwordEncoder, JavaMailSender javaMailSender, PasswordResetTokenRepo passwordResetTokenRepo) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
        this.passwordResetTokenRepo = passwordResetTokenRepo;
    }

    @GetMapping("/notAuthorized")
    public String getNotAuthorizedPage() {
        return "notAuthorized";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    // Handle GET request for the password reset page
    @GetMapping("/password-reset")
    public String showPasswordResetPage() {
        return "password-reset";
    }

    // Handle POST request for password reset request
    @PostMapping("/password-reset")
    public String passwordResetRequest(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            RedirectAttributes redirectAttributes
    ) {
        // Check if the username and email match in the database
        Client client = clientService.findByClientNameAndEmail(username.toLowerCase(), email);

        if (client != null) {
            // Generate a unique token (e.g., UUID)
            String resetToken = UUID.randomUUID().toString();

            // Store the reset token in the database
            PasswordResetToken tokenEntity = new PasswordResetToken();
            tokenEntity.setToken(resetToken);
            tokenEntity.setClient(client);
            // Set an expiration time (e.g., one hour from now)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR, 1);
            tokenEntity.setExpirationTime(cal.getTime());
            passwordResetTokenRepo.save(tokenEntity);

            //Send an email to the user with a link containing the reset token
            sendPasswordResetEmail(client.getEmail(), resetToken);

            // Redirect to a confirmation page
            redirectAttributes.addFlashAttribute("successMessage",
                    "Password reset email has been sent \n." +
                            "please check your email \n" +
                            "You have 1Hour to update it");
            return "redirect:/password-reset";
        } else {
            // Invalid username or email
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or email. Please try again.");
            return "redirect:/password-reset";
        }
    }

    // Send a password reset email to the user
    private void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(toEmail);
        mailMessage.setSubject("Password Reset Request");
        mailMessage.setText("Thank you for using our Services, \n" +
                "To reset your password, click on the following link: "
                + "http://ec2-35-176-117-222.eu-west-2.compute.amazonaws.com:5000/password-reset/" + resetToken);

        javaMailSender.send(mailMessage);
    }

    // Handle GET request for password reset form
    @GetMapping("/password-reset/{token}")
    public String showPasswordResetForm(
            @PathVariable("token") String token,
            Model model
    ) {
        // Retrieve the token from the database
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByToken(token);

        if (isValidToken(passwordResetToken)) {
            model.addAttribute("token", token);
            return "password-reset-form";
        } else {
            return "redirect:/login";
        }
    }

    private boolean isValidToken(PasswordResetToken token) {
        return token != null && !isTokenExpired(token.getExpirationTime());
    }

    private boolean isTokenExpired(Date expirationTime) {
        Date currentTime = new Date();
        return expirationTime.before(currentTime);
    }

    // Handle POST request for password reset form
    @PostMapping("/password-reset/{token}")
    public String resetPassword(
            @PathVariable("token") String token,
            @RequestParam("newPassword") String newPassword,
            RedirectAttributes redirectAttributes
    ) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepo.findByToken(token);
        if (isValidToken(passwordResetToken)) {
            Client existingClient = passwordResetToken.getClient();
            clientService.updateClientPassword(existingClient, newPassword);

            // Delete the used token from the database
            passwordResetTokenRepo.delete(passwordResetToken);

            // Add a success message
            redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
        } else {
            // Add an error message
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid token or token has expired.");
        }

        return "redirect:/login";
    }
}


