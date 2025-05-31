package pl.derleta.nebula.repository;

import jakarta.persistence.EntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.UserEntity;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    void updateUserUpdatedAt_validUserId_shouldUpdateTimestamp() {
        // Arrange
        long userId = 15L;
        Instant beforeUpdate = userRepository.findById(userId).orElseThrow().getUpdatedAt();

        // Act
        userRepository.updateUserUpdatedAt(userId);
        userRepository.flush();
        entityManager.clear();
        UserEntity updatedUser = userRepository.findById(userId).orElseThrow();

        // Assert
        assertNotNull(updatedUser.getUpdatedAt());
        assertTrue(updatedUser.getUpdatedAt().isAfter(beforeUpdate),
                "Updated timestamp should be after the original timestamp");
    }

    @Test
    @Transactional
    void updateUserDetails_validData_shouldUpdateUserDetails() {
        // Arrange
        long userId = 1000L;
        Instant beforeUpdate = userRepository.findById(userId).orElseThrow().getUpdatedAt();
        String currentTimestamp = String.valueOf(System.currentTimeMillis());
        String newFirstName = "John_" + currentTimestamp;
        String newLastName = "Doe_" + currentTimestamp;
        Date birthDate = Date.valueOf(LocalDate.of(1985, 5, 5));
        int nationalityId = 1;
        int genderId = 1;

        // Act & Assert
        userRepository.updateUserDetails(userId, newFirstName, newLastName, birthDate, nationalityId, genderId);
        userRepository.flush();
        entityManager.clear();
        UserEntity updatedUser = userRepository.findById(userId).orElseThrow();

        // Assert
        assertNotNull(updatedUser);
        assertEquals(newFirstName, updatedUser.getFirstName());
        assertEquals(newLastName, updatedUser.getLastName());
        assertEquals(birthDate, updatedUser.getBirthDate());
        assertTrue(updatedUser.getUpdatedAt().isAfter(beforeUpdate),
                "Updated timestamp should be after the original timestamp");
    }

    @Test
    @Transactional
    void updateUserDetails_invalidNationalityId_shouldThrowDataIntegrityViolationException() {
        // Arrange
        long userId = 1000L;
        String currentTimestamp = String.valueOf(System.currentTimeMillis());
        String newFirstName = "John_" + currentTimestamp;
        String newLastName = "Doe_" + currentTimestamp;
        Date birthDate = Date.valueOf(LocalDate.of(1985, 5, 5));
        int invalidNationalityId = 999;
        int genderId = 1;

        // Act & Assert
        assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    userRepository.updateUserDetails(userId, newFirstName, newLastName, birthDate, invalidNationalityId, genderId);
                    userRepository.flush();
                },
                "Expected updateUserDetails to throw DataIntegrityViolationException due to invalid nationality_id, but it didn't"
        );
    }

    @Test
    @Transactional
    void updateUserDetails_invalidUserId_shouldThrowNoSuchElementException() {
        // Arrange
        long invalidUserId = 1002L;
        String currentTimestamp = String.valueOf(System.currentTimeMillis());
        String newFirstName = "John_" + currentTimestamp;
        String newLastName = "Doe_" + currentTimestamp;
        Date birthDate = Date.valueOf(LocalDate.of(1985, 5, 5));
        int validNationalityId = 1;
        int genderId = 1;

        // Act & Assert
        assertThrows(
                NoSuchElementException.class,
                () -> {
                    userRepository.updateUserDetails(invalidUserId, newFirstName, newLastName, birthDate, validNationalityId, genderId);
                    userRepository.flush();
                    userRepository.findById(invalidUserId).orElseThrow();
                },
                "Expected NoSuchElementException to be thrown for invalid user ID, but it didn't"
        );
    }

}
