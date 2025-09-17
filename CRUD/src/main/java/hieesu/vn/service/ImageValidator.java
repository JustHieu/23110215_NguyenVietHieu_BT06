package hieesu.vn.service;


import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
public class ImageValidator {
    public void validate(MultipartFile file){
        if (file == null || file.isEmpty()) return; // ảnh là tùy chọn khi create/update
        String ct = file.getContentType();
        if (ct == null || !(ct.equals("image/png") || ct.equals("image/jpeg") || ct.equals("image/jpg") || ct.equals("image/webp")))
            throw new IllegalArgumentException("Chỉ chấp nhận PNG/JPEG/WEBP");
        if (file.getSize() > 3 * 1024 * 1024)
            throw new IllegalArgumentException("Ảnh vượt quá 3MB");
    }
}