package hieesu.vn.service;


mport org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;


@Service
public class FileStorageService {
    private final Path root;
    private final String categorySubdir;


    public FileStorageService(
            @Value("${app.upload-dir}") String uploadDir,
            @Value("${app.category-subdir}") String categorySubdir) throws IOException {
        this.root = Path.of(uploadDir).toAbsolutePath().normalize();
        this.categorySubdir = categorySubdir;
        Files.createDirectories(root.resolve(categorySubdir));
    }


    public String saveCategoryImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) return null;
        String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String filename = UUID.randomUUID() + (ext != null ? ("." + ext.toLowerCase()) : "");
        Path target = root.resolve(categorySubdir).resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
// trả về đường dẫn tương đối để client truy cập qua /uploads/**
        return "/uploads/" + categorySubdir + "/" + filename;
    }
}