package com.cabinet.springbootcabinetcomptablemanagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 * This filter intercepts every HTTP request and validates the JWT token
 * If valid, it sets the authentication in the SecurityContext
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Filter method that is executed once per request
     * 1. Extracts JWT token from Authorization header
     * 2. Validates the token
     * 3. Loads user details
     * 4. Sets authentication in SecurityContext
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            // Step 1: Extract JWT from request header
            String jwt = getJwtFromRequest(request);

            // Step 2: Validate token and set authentication
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Step 3: Get email from token
                String email = tokenProvider.getEmailFromToken(jwt);

                // Step 4: Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // Step 5: Create authentication object
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                    );

                // Step 6: Set request details
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 7: Set authentication in SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            System.err.println("Could not set user authentication in security context: " + ex.getMessage());
        }

        // Continue filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Extract JWT token from Authorization header
     * Header format: "Authorization: Bearer <token>"
     *
     * @param request HTTP request
     * @return JWT token string or null if not found
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // Check if Authorization header exists and starts with "Bearer "
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            // Extract token after "Bearer " (7 characters)
            return bearerToken.substring(7);
        }

        return null;
    }
}

