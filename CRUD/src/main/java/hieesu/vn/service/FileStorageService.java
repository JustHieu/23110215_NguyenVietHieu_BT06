package hieesu.vn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path root;
    private final String categorySubdir;

    public FileStorageService(
            @Value("${app.upload-dir:uploads}") String uploadDir,
            @Value("${app.category-subdir:categories}") String categorySubdir
    ) throws IOException {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        this.categorySubdir = categorySubdir;
        Files.createDirectories(root.resolve(categorySubdir));
    }

    public String storeCategoryImage(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;
        try {
            String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String newName = UUID.randomUUID() + (ext != null ? "." + ext.toLowerCase() : "");
            Path target = root.resolve(categorySubdir).resolve(newName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Trả về URL để hiển thị ảnh
            return "/uploads/" + categorySubdir + "/" + newName;
        } catch (IOException e) {
            throw new RuntimeException("Không lưu được file ảnh", e);
        }
    }
}
