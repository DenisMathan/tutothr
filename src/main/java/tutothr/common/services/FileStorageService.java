package tutothr.common.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final Path uploadLocation;

    public FileStorageService() {
        this.uploadLocation = Paths.get("src/main/resources/static/uploads");
        try {
            Files.createDirectories(this.uploadLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public String store(MultipartFile file, Long chapterId) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        // Generate a unique filename to avoid conflicts
        String uniqueFilename = UUID.randomUUID().toString() + "_" + filename;
        Path targetDir = this.uploadLocation.resolve("chapters").resolve(String.valueOf(chapterId));
        
        try {
            Files.createDirectories(targetDir);
            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetDir.resolve(uniqueFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }

        return "/uploads/chapters/" + chapterId + "/" + uniqueFilename;
    }

    public void delete(String fileUrl) {
        try {
            if (fileUrl != null && fileUrl.startsWith("/uploads/")) {
                String relativePath = fileUrl.substring("/uploads/".length());
                // Prevent directory traversal attacks
                if (relativePath.contains("..")) {
                     throw new RuntimeException("Invalid file path");
                }
                Path filePath = this.uploadLocation.resolve(relativePath);
                Files.deleteIfExists(filePath);
            }
        } catch (IOException e) {
            System.err.println("Could not delete file: " + fileUrl);
            e.printStackTrace();
        }
    }
}
