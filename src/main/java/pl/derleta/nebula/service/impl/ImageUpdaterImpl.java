package pl.derleta.nebula.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.derleta.nebula.service.ImageUpdater;
import pl.derleta.nebula.util.ImageUtil;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of the {@link ImageUpdater} interface for managing user avatar images.
 * Responsible for updating, processing, and saving avatar images in a pre-defined directory path.
 */
@Service
public class ImageUpdaterImpl implements ImageUpdater {

    @Value("${image.avatar.path}")
    private String avatarPath;

    /**
     * Updates the user's avatar image by saving the provided file as a JPG.
     *
     * @param userId The ID of the user whose avatar is being updated.
     * @param image  The image file to be processed and saved as the avatar.
     * @return true if the update was successful, false otherwise.
     */
    @Override
    public boolean update(final long userId, final MultipartFile image) {
        if (!createDirectoryIfNotExists(avatarPath)) return false;
        try {
            saveImageAsJpg(image, new File(avatarPath), userId);
            return true;
        } catch (IOException e) {
            System.err.println("Error processing or saving image: " + e.getMessage());
            return false;
        }
    }

    /**
     * Creates a directory at the specified path if it does not already exist.
     *
     * @param path The path of the directory to be created.
     * @return true if the directory already exists or was successfully created, false otherwise.
     */
    protected boolean createDirectoryIfNotExists(final String path) {
        File directory = new File(path);
        return directory.exists() || directory.mkdirs();
    }

    /**
     * Saves an image file as a JPG after processing it by cropping and converting it to a standard format.
     *
     * @param image     The image file to be processed and saved.
     * @param directory The directory where the processed image will be stored.
     * @param userId    The user ID that will be used to name the resulting JPG file.
     * @throws IOException If an error occurs during the image processing or file saving process.
     */
    protected void saveImageAsJpg(final MultipartFile image, final File directory, final long userId) throws IOException {
        byte[] file = ImageUtil.cropAndConvertToJpg(image);
        String fileName = String.format("%s/%s.jpg", directory.getAbsolutePath(), userId);
        ImageUtil.saveAsJpg(file, fileName);
    }

}
