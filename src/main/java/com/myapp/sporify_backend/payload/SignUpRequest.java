package com.myapp.sporify_backend.payload;

import javax.validation.constraints.*;


/**
 * Τα στοιχεία που στέλνει ο χρήστης όταν στέλνει ένα signup request,
 * δηλαδή username, email & password
 */

public class SignUpRequest {
    //    @NotEmpty(message = "Username may not be empty")
//    @NotBlank(message = "Username may not be blank") // να μην είναι κενό
//    @Size(min = 3, max = 20,message = "Username size must be between 3 and 20") // το μέγεθος να είναι απο 3-20 χαρακτήρες
    private String username;

    //    @NotEmpty(message = "Email may not be empty")
//    @NotBlank(message = "Email may not be empty")
//    @Size(max = 50)
//    @Email // να αναπαρτιστά email πχ το example@.com δεν περνάει
    private String email;

    //    @NotEmpty(message = "Password may not be empty")
//    @NotBlank(message = "Username may not be blank")
//    @Size(min = 6, max = 40, message = "Size must be between 6 and 40")
    private String password;

    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
