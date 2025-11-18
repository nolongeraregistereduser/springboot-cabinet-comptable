package com.cabinet.springbootcabinetcomptablemanagement.repositories;

import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
