package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.GenderEntity;
import pl.derleta.nebula.domain.entity.NationalityEntity;
import pl.derleta.nebula.domain.entity.UserEntity;
import pl.derleta.nebula.domain.mapper.NebulaUserMapper;
import pl.derleta.nebula.domain.model.Gender;
import pl.derleta.nebula.domain.model.Nationality;
import pl.derleta.nebula.domain.model.NebulaUser;
import pl.derleta.nebula.domain.model.Region;
import pl.derleta.nebula.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class UserProviderImplTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserProviderImpl userProvider;

    private UserEntity testUserEntity;
    private NebulaUser testNebulaUser;
    private final Long userId = 123L;

    @BeforeEach
    void setUp() {
        // Create a test gender entity
        GenderEntity genderEntity = new GenderEntity();
        genderEntity.setId(1);
        genderEntity.setName("Male");

        // Create a test region entity
        Region region = new Region(1, "Europe");

        // Create a test nationality entity
        NationalityEntity nationalityEntity = new NationalityEntity();
        nationalityEntity.setId(1);
        nationalityEntity.setName("United States");
        nationalityEntity.setCode("USA");

        // Create a test user entity
        testUserEntity = new UserEntity();
        testUserEntity.setId(userId);
        testUserEntity.setLogin("testUser");
        testUserEntity.setEmail("test@example.com");
        testUserEntity.setFirstName("John");
        testUserEntity.setLastName("Doe");
        testUserEntity.setAge(30);
        testUserEntity.setBirthDate(Date.valueOf(LocalDate.of(1993, 1, 1)));
        testUserEntity.setGender(genderEntity);
        testUserEntity.setNationality(nationalityEntity);
        testUserEntity.setGames(Collections.emptyList());
        testUserEntity.setAchievements(Collections.emptyList());

        // Create test NebulaUser
        Gender gender = new Gender(1, "Male");
        Nationality nationality = new Nationality(1, "United States", "USA", region);

        testNebulaUser = new NebulaUser(
                userId,
                "testUser",
                "test@example.com",
                "John",
                "Doe",
                30,
                Date.valueOf(LocalDate.of(1993, 1, 1)),
                gender,
                nationality,
                null,
                Collections.emptyList(),
                Collections.emptyList()
        );
    }

    @Test
    void get_shouldReturnNebulaUser_whenUserExists() {
        // Arrange
        when(repository.findById(userId)).thenReturn(Optional.of(testUserEntity));

        try (MockedStatic<NebulaUserMapper> mockedMapper = Mockito.mockStatic(NebulaUserMapper.class)) {
            mockedMapper.when(() -> NebulaUserMapper.toUser(testUserEntity)).thenReturn(testNebulaUser);

            // Act
            NebulaUser result = userProvider.get(userId);

            // Assert
            assertNotNull(result);
            assertEquals(testNebulaUser.id(), result.id());
            assertEquals(testNebulaUser.login(), result.login());
            assertEquals(testNebulaUser.email(), result.email());
            assertEquals(testNebulaUser.firstName(), result.firstName());
            assertEquals(testNebulaUser.lastName(), result.lastName());
            assertEquals(testNebulaUser.age(), result.age());
            assertEquals(testNebulaUser.birthDate(), result.birthDate());

            verify(repository, times(1)).findById(userId);
            mockedMapper.verify(() -> NebulaUserMapper.toUser(testUserEntity), times(1));
        }
    }

    @Test
    void get_shouldReturnNull_whenUserDoesNotExist() {
        // Arrange
        when(repository.findById(userId)).thenReturn(Optional.empty());

        // Act
        NebulaUser result = userProvider.get(userId);

        // Assert
        assertNull(result);
        verify(repository, times(1)).findById(userId);
    }

}
