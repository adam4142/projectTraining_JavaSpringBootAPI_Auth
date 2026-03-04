package org.example.concert_booking.Security;

import java.io.IOException;

import org.example.projecttraining.Security.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import org.example.projecttraining.Model.User;
import org.example.projecttraining.Repository.UserRepository;
import org.example.projecttraining.Service.CustomerUserDetail;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private UserRepository userRepository;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Check if the Authorization header is present and starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extract the token
            if (tokenGenerator.validateToken(token)) {
                // Fetch the user associated with the token
                User user = userRepository.findByToken(token);
                if (user != null) {
                    // Load UserDetails from CustomUserDetailsService
                    CustomerUserDetail userDetails = new CustomerUserDetail(user);
                    // Set authentication in SecurityContext
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("User not found for the token");
                    return;
                }
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else if (request.getRequestURI().startsWith("/api/")
                && !request.getRequestURI().equals("/api/register")
                && !request.getRequestURI().equals("/api/login")
                && !request.getRequestURI().equals("/api/user")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token required");
            return;
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}