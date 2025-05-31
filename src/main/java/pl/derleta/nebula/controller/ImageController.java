package pl.derleta.nebula.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.derleta.nebula.service.ImageUpdater;
import pl.derleta.nebula.service.TokenProvider;
import pl.derleta.nebula.util.ImageUtil;

/**
 * A REST controller responsible for handling image upload operations.
 * It verifies user authentication through JWT tokens and processes the uploaded images.
 */
@RestController
@CrossOrigin(origins = {"https://milkyway.local:8555", "https://localhost:3000"}, maxAge = 3600)
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public final class ImageController {

    public static final String DEFAULT_PATH = "image";

    private final TokenProvider tokenProvider;
    private final ImageUpdater imageUpdater;

    /**
     * Handles the upload of a multipart image file by verifying the user's JWT token for authentication
     * and forwarding the file for further processing based on the authenticated user's ID.
     *
     * @param accessToken The JWT token extracted from the user's cookies, used for authentication checks.
     * @param image       The multipart image file being uploaded by the user.
     * @return A {@code ResponseEntity<String>} containing a message and an appropriate HTTP status code:
     * - 401 UNAUTHORIZED if the JWT token is invalid.
     * - The result of further processing through the {@code getImageResponse} method if the token is valid.
     */
    @PostMapping(value = DEFAULT_PATH, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> upload(@CookieValue("accessToken") String accessToken,
                                         @RequestPart("file") MultipartFile image) {
        if (tokenProvider.isValid(accessToken)) {
            long userId = tokenProvider.getUserId(accessToken);
            return getImageResponse(userId, image);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    /**
     * Processes the uploaded image for the given user and returns an appropriate HTTP response
     * based on the success or failure of the image handling.
     *
     * @param userId The unique identifier of the user uploading the image.
     * @param image  The multipart image file uploaded by the user.
     * @return A {@code ResponseEntity<String>} containing a message and HTTP status code:
     * - 400 BAD REQUEST if the image is empty or unsupported.
     * - 201 CREATED if the image is successfully saved.
     * - 403 FORBIDDEN if saving the image fails for an undefined reason.
     */
    private ResponseEntity<String> getImageResponse(long userId, MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body("Image is empty");
        } else {
            if (!ImageUtil.isValidImageFile(image)) {
                return ResponseEntity.badRequest().body("Unsupported image file, there are supported: jpg, jpeg, png");
            } else {
                if (imageUpdater.update(userId, image)) {
                    return ResponseEntity.status(HttpStatus.CREATED).body("Image has been saved");
                }
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Image has not been saved");
            }
        }
    }

}
