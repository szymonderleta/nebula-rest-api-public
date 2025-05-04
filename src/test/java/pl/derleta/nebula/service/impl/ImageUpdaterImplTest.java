package pl.derleta.nebula.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class ImageUpdaterImplTest {

    @Autowired
    private ImageUpdaterImpl imageUpdater;

    private MultipartFile mockImage;
    private final String testAvatarPath = "/tmp/test-avatars";
    private final long testUserId = 123L;

    @BeforeEach
    void setUp() {
        // Set the avatar path using reflection since we can't use @Value in tests
        ReflectionTestUtils.setField(imageUpdater, "avatarPath", testAvatarPath);

        // Create a mock MultipartFile
        mockImage = mock(MultipartFile.class);
    }


    @Test
    void update_shouldReturnFalse_whenDirectoryCreationFails() {
        // Arrange
        ImageUpdaterImpl spyUpdater = Mockito.spy(imageUpdater);
        doReturn(false).when(spyUpdater).createDirectoryIfNotExists(testAvatarPath);

        // Act
        boolean result = spyUpdater.update(testUserId, mockImage);

        // Assert
        assertFalse(result);
        verify(spyUpdater, times(1)).createDirectoryIfNotExists(testAvatarPath);
    }

    @Test
    void update_shouldReturnTrue_whenImageProcessingAndSavingSucceeds() throws IOException {
        // Arrange
        ImageUpdaterImpl spyUpdater = Mockito.spy(imageUpdater);
        doReturn(true).when(spyUpdater).createDirectoryIfNotExists(testAvatarPath);
        doNothing().when(spyUpdater).saveImageAsJpg(mockImage, new File(testAvatarPath), testUserId);

        // Act
        boolean result = spyUpdater.update(testUserId, mockImage);

        // Assert
        assertTrue(result);
        verify(spyUpdater, times(1)).createDirectoryIfNotExists(testAvatarPath);
        verify(spyUpdater, times(1)).saveImageAsJpg(mockImage, new File(testAvatarPath), testUserId);
    }

    @Test
    void update_shouldReturnFalse_whenSaveImageThrowsIOException() throws IOException {
        // Arrange
        ImageUpdaterImpl spyUpdater = Mockito.spy(imageUpdater);
        doReturn(true).when(spyUpdater).createDirectoryIfNotExists(testAvatarPath);
        doThrow(new IOException("Test exception")).when(spyUpdater)
                .saveImageAsJpg(mockImage, new File(testAvatarPath), testUserId);

        // Act
        boolean result = spyUpdater.update(testUserId, mockImage);

        // Assert
        assertFalse(result);
        verify(spyUpdater, times(1)).createDirectoryIfNotExists(testAvatarPath);
        verify(spyUpdater, times(1)).saveImageAsJpg(mockImage, new File(testAvatarPath), testUserId);
    }

}
