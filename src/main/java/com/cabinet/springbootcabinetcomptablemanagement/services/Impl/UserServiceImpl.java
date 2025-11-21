package com.cabinet.springbootcabinetcomptablemanagement.services.Impl;

import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.UtilisateurRepository;
import com.cabinet.springbootcabinetcomptablemanagement.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j

public class UserServiceImpl implements UserService {
    private final UtilisateurRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public User createUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActif(true);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUserBySociete(Long societeId) {
        return userRepository.findBySocieteId(societeId);
    }

    @Override
    public List<User> getUserByRole(User.Role role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User updateUser(Long id, User user) {


        User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setNomComplet(user.getNomComplet());
        existingUser.setEmail(user.getEmail());
        return userRepository.save(existingUser);


    }

    @Override
    public void deleteUser(Long id) {

        userRepository.deleteById(id);

    }

    @Override
    public void activateUser(Long id) {

        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setActif(true);
        userRepository.save(user);

    }

    @Override
    public void desactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setActif(false);
        userRepository.save(user);

    }
}
