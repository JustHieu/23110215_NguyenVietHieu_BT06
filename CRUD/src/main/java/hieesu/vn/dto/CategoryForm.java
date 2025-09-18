package hieesu.vn.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
public class CategoryForm {
    private Long id;

    @NotBlank(message = "Tên danh mục bắt buộc")
    private String name;

    private String description;

    // upload ảnh (tùy chọn)
    private MultipartFile image;
}
