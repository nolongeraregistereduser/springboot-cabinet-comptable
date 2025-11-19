package com.cabinet.springbootcabinetcomptablemanagement.services;

import com.cabinet.springbootcabinetcomptablemanagement.models.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface UserService {

     User createUser(User user);

     Optional<User> getUserById(Long id);

     Optional<User> getUserByEmail(String email);

     List<User> getAllUsers();

     List<User> getUserBySociete(Long societeId);

     List<User> getUserByRole(User.Role role);

     User updateUser(Long id, User user);

     void deleteUser(Long id);

     void activateUser(Long id);

     void desactivateUser(Long id);


}
