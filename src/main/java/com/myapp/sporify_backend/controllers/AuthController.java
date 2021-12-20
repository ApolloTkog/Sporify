package com.myapp.sporify_backend.controllers;


import com.myapp.sporify_backend.models.User;
import com.myapp.sporify_backend.payload.SignUpRequest;
import com.myapp.sporify_backend.repositories.UserRepository;
import com.myapp.sporify_backend.security.jwt.JwtUtils;
import com.myapp.sporify_backend.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Ο controller που είναι υπεύθυνος για τα requests που γίνονται για signin & signup
 * Το home path που τρέχει ο server είναι το http://localhost:8081/
 * Ο συγκεκριμένος controller είναι υπεύθυνος για τα request στο path http://localhost:8081/api/auth
 *
 * Έχουμε δυο POST requests όπως θα δούμε παρακάτω  /signup & /signin
 */

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * Είναι το endpoint που είναι υπεύθυνο για την σύνδεση του χρήστη
     * @param loginRequest Το στοιχεία που στέλνει ο χρήστης στο body όταν γίνεται το request
     * @return Επιστρέφει ένα μήνυμα ~ BAD CREDENTIALS ~ αν αποτύχει με status 401 UNAUTHORIZED
     * ή 200 OK με τα στοχεία του χρήστη + το access token του χρήστη αν τα στοιχεία είναι σωστά
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication;

        // κάνει απόπειρα για σύνδεση
        try{
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        }
        // αν αποτύχει στέλνει ως response UNAUTHORIZED με μήνυμα ~ Bad credentials ~
        catch (AuthenticationException authenticationException){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse(
                    "Bad credentials!"
            ));
        }

        // κάνει set το authentication αν δεν αποτύχει
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // κάνει generate ένα token βάσει του χρήστη που έκανα login
        String jwt = jwtUtils.generateJwtToken(authentication);

        // παίρνει τα στοιχεία του χρήστη δημιουργώντας ενα αντικείμενο userDetails
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // επιστρέφει τα στοιχεία του χρήστη με την βοήθεια της κλάσης LoginResponse
        return ResponseEntity.ok(new LoginResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail()));
    }

    /**
     * Είναι το endpoint που είναι υπεύθυνο για την εγγραφή του χρήστη
     * @param signUpRequest Το στοιχεία που στέλνει ο χρήστης στο body όταν γίνεται το request
     * @return Επιστρέφει ~ Error: Username is already taken! ~ αν το username υπάρχει ήδη
     * ή επιστρέφει ~ Error: Email is already in use! ~ αν το email υπάρχει ήδη
     * ή ~ User registered successfully! ~ αν τα στοιχεία εγγραφής
     * είναι εντάξει και ο χρήστης δημιουργείται επιτυχώς.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {

        String valid = validateRegister(signUpRequest);
        if(!valid.equals("success")) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(valid));

        // check if username exists
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        // check if email exists
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        // insert user to user's collection
        userRepository.insert(user);

        return ResponseEntity.ok(
                new MessageResponse("User registered successfully!"));
    }

    private String validateRegister(SignUpRequest request){
        String message = "success";

        if(request == null)
            return "Body is empty";

        if(request.getUsername().isEmpty() || request.getUsername() == null){
            return "Username field cannot be empty!";
        }
        else if(request.getUsername().length() <= 3){
            return "Username size must be greater than 3 chars!";
        }

        if(request.getEmail().isEmpty() || request.getEmail() == null){
            return "Email field cannot be empty!";
        }
        else if(!validate(request.getEmail())){
            return "You must enter a valid email format!";
        }


        if(request.getPassword().isEmpty() || request.getPassword() == null){
            return "Password field cannot be empty!";
        }
        else if(request.getPassword().length() < 6){
            return "Password size must 6 chars or greater!";
        }

        if(request.getConfirmPassword() == null || !request.getConfirmPassword().equals(request.getPassword())){
            return "Passwords doesn't match!";
        }

        return  message;

    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
