package pl.derleta.nebula.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUpdater {

    boolean update(long userId, MultipartFile image);

}
