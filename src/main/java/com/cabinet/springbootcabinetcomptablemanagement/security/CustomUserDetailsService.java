package com.cabinet.springbootcabinetcomptablemanagement.security;

import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Custom UserDetailsService implementation for Spring Security
 * Loads user details from database and converts them to Spring Security's UserDetails format
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Find user in database by email
        User user = utilisateurRepository.findByEmail(email)
                .orElseThrow(() ->
                    new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        // Check if user is active
        if (user.getActif() == null || !user.getActif()) {
            throw new org.springframework.security.core.userdetails.UsernameNotFoundException(
                    "Utilisateur désactivé: " + email);
        }

        // Convert our User entity to Spring Security's UserDetails
        // Role is prefixed with "ROLE_" as required by Spring Security
        // Account is enabled based on user's actif status
        boolean isEnabled = user.getActif() != null && user.getActif();
        
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!isEnabled) // Account is enabled if user is active
                .build();
    }
}

