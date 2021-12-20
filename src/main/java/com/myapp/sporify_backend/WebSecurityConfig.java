package com.myapp.sporify_backend;


import com.myapp.sporify_backend.security.jwt.AuthTokenFilter;
import com.myapp.sporify_backend.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Ειναι η κλάση που κάνει configure όλο το spring boot security
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    // Θέτει ως authentication jwt token filter
    // το δικό μας AuthTokenFilter
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // θέτει ως userDetailsService το service
    // που έχει δημιουργηθεί και default password encoder το BCrypt
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // ορίζουμε το τύπου του password encode μας
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // κάνουμε disable το csrf
        // είναι μια ασφάλεια για την υποβολή φορμών, ώστε να μπορούμε να κάνουμε post απο το API μας
        // Disable csrf
        http.csrf().disable();

        // δεν δημιουργούμε sessions στην εφαρμογή μας
        // No session will be created or used by spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // καθορίζομουμε σε ποια endpoints μπορεί να έχει access ο χρήστης
        // Entry points
        http.authorizeRequests().antMatchers("/api/auth/**").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/api/user/**").permitAll()
                .anyRequest().authenticated();


        // Exception handling
        http.exceptionHandling().authenticationEntryPoint(unauthorizedHandler);

        // Add Authentication filter before each request
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
