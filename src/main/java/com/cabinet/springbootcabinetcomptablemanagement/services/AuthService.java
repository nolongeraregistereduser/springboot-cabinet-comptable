package com.cabinet.springbootcabinetcomptablemanagement.services;

public interface AuthService {

     boolean authentificate(String email, String password);
     void logout(String token);
     boolean validateToken(String token);
     String refreshToken(String token);


}
