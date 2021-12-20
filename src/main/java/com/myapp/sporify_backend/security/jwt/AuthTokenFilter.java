package com.myapp.sporify_backend.security.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Είναι η κλάση απο την οποία όταν στέλνουμε ένα request κάνει το φιλτράρισμα και κοιτάει
 * άν μπορεί να επιστρέψει δεδομένα. Κοιτάει αν το token του χρήστη είναι έγκυρο ή αν υπάρχει γενικότερα
 * και κάνει τις ανάλογες ενέργειες.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // προσπέλασε το token απο το request
            // και βάλτο στο string jwt
            String jwt = parseJwt(request);

            // αν το token είναι έγκυρο και περνάει το validation
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                // τότε πάρε το username του χρήστη απο το token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // βρές το χρήστη και τα details του απο username που είναι μοναδικό
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // δημιούργησε ένα authentication token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // αποθήκευσε το authentication token στο security context
                // ώστε να έχουμε access από όλο το application
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {0}", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     *
     * @param request Το request
     * @return Επιστρέφει το token αν υπάρχει αλλίως null
     */
    private String parseJwt(HttpServletRequest request) {
        // παίρνει το authorization header
        String headerAuth = request.getHeader("Authorization");

        // αν υπάρχει το authorization header και αρχίζει απο bearer
        // δηλαδή το token είναι τύπου Bearer
        // κόψε τα πρώτα 7 γράμματα δηλαδή το Bearer και το κενό
        // δηλαδή μένει το token
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        // επέστρεψε τίποτα αλλιώς
        return null;
    }
}
