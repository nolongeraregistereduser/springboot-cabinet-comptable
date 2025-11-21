package com.cabinet.springbootcabinetcomptablemanagement.services.Impl;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.request.LoginRequestDTO;
import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.AuthResponseDTO;
import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.UtilisateurRepository;
import com.cabinet.springbootcabinetcomptablemanagement.security.JwtTokenProvider;
import com.cabinet.springbootcabinetcomptablemanagement.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of AuthService
 * Handles user authentication and JWT token generation
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    /**
     * Authenticate user with email and password
     *
     * Process:
     * 1. Find user by email
     * 2. Validate password using BCrypt
     * 3. Generate JWT token
     * 4. Build and return response with token and user info
     *
     * @param loginRequest Contains email and password
     * @return AuthResponseDTO with JWT token and user information
     * @throws BadCredentialsException if credentials are invalid
     */
    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequest) {
        // Step 1: Find user by email
        User user = utilisateurRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                    new BadCredentialsException("Email ou mot de passe incorrect"));

        // Step 2: Validate password
        // passwordEncoder.matches() compares the raw password with the encrypted password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Email ou mot de passe incorrect");
        }

        // Step 3: Generate JWT token
        String token = tokenProvider.generateToken(user.getEmail());

        // Step 4: Build and return AuthResponseDTO
        AuthResponseDTO response = new AuthResponseDTO();
        response.setToken(token);
        response.setType("Bearer");
        response.setUserId(user.getId());
        response.setEmail(user.getEmail());
        response.setNomComplet(user.getNomComplet());
        response.setRole(user.getRole().name());

        // If user is SOCIETE role, include société information
        if (user.getSociete() != null) {
            response.setSocieteId(user.getSociete().getId());
            response.setSocieteRaisonSociale(user.getSociete().getRaisonSociale());
        }

        return response;
    }
}

