package com.cabinet.springbootcabinetcomptablemanagement.config;

import com.cabinet.springbootcabinetcomptablemanagement.models.Societe;
import com.cabinet.springbootcabinetcomptablemanagement.models.User;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.SocieteRepository;
import com.cabinet.springbootcabinetcomptablemanagement.repositories.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Database Seeder - Populates database with test data
 * Runs automatically on application startup
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UtilisateurRepository utilisateurRepository;
    private final SocieteRepository societeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (utilisateurRepository.count() > 0) {
            System.out.println("✅ Database already seeded. Skipping...");
            return;
        }

        System.out.println("🌱 Seeding database with test data...");

        // Create Societies
        Societe societe1 = createSociete(
            "TechnoMaroc SARL",
            "002567891234567",
            "Zone Industrielle Sidi Moumen, Casablanca",
            "+212522345678",
            "contact@technomaroc.ma"
        );

        Societe societe2 = createSociete(
            "Atlas Import Export",
            "002789123456789",
            "Boulevard Mohamed V, Rabat",
            "+212537654321",
            "info@atlasimportexport.ma"
        );

        Societe societe3 = createSociete(
            "Maghreb Services",
            "002891234567890",
            "Avenue Hassan II, Marrakech",
            "+212524987654",
            "contact@maghrebservices.ma"
        );

        Societe societe4 = createSociete(
            "Digital Solutions Maroc",
            "002912345678901",
            "Quartier des Affaires, Tanger",
            "+212539876543",
            "hello@digitalsolutions.ma"
        );

        Societe societe5 = createSociete(
            "Sahara Trading",
            "003012345678912",
            "Zone Franche, Agadir",
            "+212528765432",
            "contact@saharatrading.ma"
        );

        System.out.println("✅ Created 5 societies");

        // Create Comptables (Accountants) - Not linked to any société
        createComptable(
            "mohamed@comptable.com",
            "123456789",
            "Mohamed Alami",
            null
        );

        createComptable(
            "fatima@comptable.com",
            "123456789",
            "Fatima Benani",
            null
        );

        createComptable(
            "youssef@comptable.com",
            "123456789",
            "Youssef Tazi",
            null
        );

        System.out.println("✅ Created 3 comptables");

        // Create Société Users (Company representatives) - Each linked to a société
        createSocieteUser(
            "mohamed@societe.com",
            "123456789",
            "Mohamed Benjelloun",
            societe1
        );

        createSocieteUser(
            "ahmed@societe.com",
            "123456789",
            "Ahmed Tazi",
            societe2
        );

        createSocieteUser(
            "sara@societe.com",
            "123456789",
            "Sara Alaoui",
            societe3
        );

        createSocieteUser(
            "karim@societe.com",
            "123456789",
            "Karim El Fassi",
            societe4
        );

        createSocieteUser(
            "leila@societe.com",
            "123456789",
            "Leila Chraibi",
            societe5
        );

        // Create additional société user for TechnoMaroc (société can have multiple users)
        createSocieteUser(
            "omar@societe.com",
            "123456789",
            "Omar Benkirane",
            societe1
        );

        System.out.println("✅ Created 6 société users");

        System.out.println("\n" +
            "╔══════════════════════════════════════════════════════════════════╗\n" +
            "║                    🎉 Database Seeded Successfully! 🎉           ║\n" +
            "╠══════════════════════════════════════════════════════════════════╣\n" +
            "║                                                                  ║\n" +
            "║  📊 Test Data Summary:                                          ║\n" +
            "║  • 5 Sociétés                                                   ║\n" +
            "║  • 3 Comptables (Accountants)                                   ║\n" +
            "║  • 6 Société Users (Company representatives)                    ║\n" +
            "║                                                                  ║\n" +
            "║  🔐 Test Credentials:                                           ║\n" +
            "║                                                                  ║\n" +
            "║  👨‍💼 COMPTABLE (Accountant):                                    ║\n" +
            "║     Email: mohamed@comptable.com                                ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║                                                                  ║\n" +
            "║     Email: fatima@comptable.com                                 ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║                                                                  ║\n" +
            "║     Email: youssef@comptable.com                                ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║                                                                  ║\n" +
            "║  🏢 SOCIETE (Company):                                          ║\n" +
            "║     Email: mohamed@societe.com                                  ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║     Company: TechnoMaroc SARL                                   ║\n" +
            "║                                                                  ║\n" +
            "║     Email: ahmed@societe.com                                    ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║     Company: Atlas Import Export                                ║\n" +
            "║                                                                  ║\n" +
            "║     Email: sara@societe.com                                     ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║     Company: Maghreb Services                                   ║\n" +
            "║                                                                  ║\n" +
            "║     Email: karim@societe.com                                    ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║     Company: Digital Solutions Maroc                            ║\n" +
            "║                                                                  ║\n" +
            "║     Email: leila@societe.com                                    ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║     Company: Sahara Trading                                     ║\n" +
            "║                                                                  ║\n" +
            "║     Email: omar@societe.com                                     ║\n" +
            "║     Password: 123456789                                         ║\n" +
            "║     Company: TechnoMaroc SARL                                   ║\n" +
            "║                                                                  ║\n" +
            "║  🧪 How to Test:                                                ║\n" +
            "║  1. Start application: mvn spring-boot:run                      ║\n" +
            "║  2. Login with any credentials above                            ║\n" +
            "║  3. Use JWT token for authenticated requests                    ║\n" +
            "║                                                                  ║\n" +
            "║  📍 Endpoints:                                                  ║\n" +
            "║  • POST /api/auth/login                                         ║\n" +
            "║  • View H2 Console: http://localhost:8080/h2-console            ║\n" +
            "║                                                                  ║\n" +
            "╚══════════════════════════════════════════════════════════════════╝\n"
        );
    }

    /**
     * Create a Société (Company)
     */
    private Societe createSociete(String raisonSociale, String ice, String adresse,
                                   String telephone, String emailContact) {
        Societe societe = new Societe();
        societe.setRaisonSociale(raisonSociale);
        societe.setIce(ice);
        societe.setAdresse(adresse);
        societe.setTelephone(telephone);
        societe.setEmailContact(emailContact);
        societe.setActif(true);
        societe.setCreatedAt(LocalDateTime.now());

        return societeRepository.save(societe);
    }

    /**
     * Create a Comptable user (Accountant role)
     */
    private User createComptable(String email, String password, String nomComplet, Societe societe) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNomComplet(nomComplet);
        user.setRole(User.Role.COMPTABLE);
        user.setSociete(societe); // Comptables are not linked to any société
        user.setActif(true);
        user.setCreatedAt(LocalDateTime.now());

        return utilisateurRepository.save(user);
    }

    /**
     * Create a Société user (Company representative role)
     */
    private User createSocieteUser(String email, String password, String nomComplet, Societe societe) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setNomComplet(nomComplet);
        user.setRole(User.Role.SOCIETE);
        user.setSociete(societe); // Link to the société
        user.setActif(true);
        user.setCreatedAt(LocalDateTime.now());

        return utilisateurRepository.save(user);
    }
}

