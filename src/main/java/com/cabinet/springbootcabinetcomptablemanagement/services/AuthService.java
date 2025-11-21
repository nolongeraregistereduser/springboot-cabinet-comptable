package com.cabinet.springbootcabinetcomptablemanagement.services;

import com.cabinet.springbootcabinetcomptablemanagement.dtos.request.LoginRequestDTO;
import com.cabinet.springbootcabinetcomptablemanagement.dtos.response.AuthResponseDTO;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Authenticate user with email and password
     * @param loginRequest Login credentials
     * @return AuthResponseDTO containing JWT token and user information
     */
    AuthResponseDTO login(LoginRequestDTO loginRequest);
}
