package pl.derleta.nebula.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.derleta.nebula.service.ImageUpdater;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.util.ImageUtil;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageControllerTest {

    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private ImageUpdater imageUpdater;

    @InjectMocks
    private ImageController imageController;

    private String validAccessToken;
    private String invalidAccessToken;
    private long userId;
    private MockMultipartFile validImageFile;
    private MockMultipartFile invalidImageFile;
    private MockMultipartFile emptyImageFile;

    @BeforeEach
    void setUp() {
        validAccessToken = "valid.access.token";
        invalidAccessToken = "invalid.access.token";
        userId = 123L;

        // Create test image files
        validImageFile = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        invalidImageFile = new MockMultipartFile(
                "file",
                "test-document.txt",
                "text/plain",
                "test document content".getBytes()
        );

        emptyImageFile = new MockMultipartFile(
                "file",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );
    }

    @Test
    void upload_withInvalidToken_returnsUnauthorized() {
        // Arrange
        when(tokenProvider.isValid(invalidAccessToken)).thenReturn(false);

        // Act
        ResponseEntity<String> response = imageController.upload(invalidAccessToken, validImageFile);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(tokenProvider, times(1)).isValid(invalidAccessToken);
        verify(tokenProvider, never()).getUserId(any());
        verify(imageUpdater, never()).update(anyLong(), any(MultipartFile.class));
    }

    @Test
    void upload_withValidTokenAndValidImage_returnsCreated() {
        // Arrange
        try (var mockedStatic = mockStatic(ImageUtil.class)) {
            when(tokenProvider.isValid(validAccessToken)).thenReturn(true);
            when(tokenProvider.getUserId(validAccessToken)).thenReturn(userId);
            mockedStatic.when(() -> ImageUtil.isValidImageFile(validImageFile)).thenReturn(true);
            when(imageUpdater.update(userId, validImageFile)).thenReturn(true);

            // Act
            ResponseEntity<String> response = imageController.upload(validAccessToken, validImageFile);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertEquals("Image has been saved", response.getBody());
            verify(tokenProvider, times(1)).isValid(validAccessToken);
            verify(tokenProvider, times(1)).getUserId(validAccessToken);
            mockedStatic.verify(() -> ImageUtil.isValidImageFile(validImageFile), times(1));
            verify(imageUpdater, times(1)).update(userId, validImageFile);
        }
    }

    @Test
    void upload_withValidTokenAndValidImageButUpdateFails_returnsForbidden() {
        // Arrange
        try (var mockedStatic = mockStatic(ImageUtil.class)) {
            when(tokenProvider.isValid(validAccessToken)).thenReturn(true);
            when(tokenProvider.getUserId(validAccessToken)).thenReturn(userId);
            mockedStatic.when(() -> ImageUtil.isValidImageFile(validImageFile)).thenReturn(true);
            when(imageUpdater.update(userId, validImageFile)).thenReturn(false);

            // Act
            ResponseEntity<String> response = imageController.upload(validAccessToken, validImageFile);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertEquals("Image has not been saved", response.getBody());
            verify(tokenProvider, times(1)).isValid(validAccessToken);
            verify(tokenProvider, times(1)).getUserId(validAccessToken);
            mockedStatic.verify(() -> ImageUtil.isValidImageFile(validImageFile), times(1));
            verify(imageUpdater, times(1)).update(userId, validImageFile);
        }
    }

    @Test
    void upload_withValidTokenAndEmptyImage_returnsBadRequest() {
        // Arrange
        when(tokenProvider.isValid(validAccessToken)).thenReturn(true);
        when(tokenProvider.getUserId(validAccessToken)).thenReturn(userId);

        // Act
        ResponseEntity<String> response = imageController.upload(validAccessToken, emptyImageFile);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Image is empty", response.getBody());
        verify(tokenProvider, times(1)).isValid(validAccessToken);
        verify(tokenProvider, times(1)).getUserId(validAccessToken);
        verify(imageUpdater, never()).update(anyLong(), any(MultipartFile.class));
    }

    @Test
    void upload_withValidTokenAndInvalidImageType_returnsBadRequest() {
        // Arrange
        try (var mockedStatic = mockStatic(ImageUtil.class)) {
            when(tokenProvider.isValid(validAccessToken)).thenReturn(true);
            when(tokenProvider.getUserId(validAccessToken)).thenReturn(userId);
            mockedStatic.when(() -> ImageUtil.isValidImageFile(invalidImageFile)).thenReturn(false);

            // Act
            ResponseEntity<String> response = imageController.upload(validAccessToken, invalidImageFile);

            // Assert
            assertNotNull(response);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertEquals("Unsupported image file, there are supported: jpg, jpeg, png", response.getBody());
            verify(tokenProvider, times(1)).isValid(validAccessToken);
            verify(tokenProvider, times(1)).getUserId(validAccessToken);
            mockedStatic.verify(() -> ImageUtil.isValidImageFile(invalidImageFile), times(1));
            verify(imageUpdater, never()).update(anyLong(), any(MultipartFile.class));
        }
    }

}
