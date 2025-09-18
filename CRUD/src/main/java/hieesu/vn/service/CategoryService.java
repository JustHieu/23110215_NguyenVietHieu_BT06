package hieesu.vn.service;

import hieesu.vn.entity.Category;
import hieesu.vn.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService; // bạn đã có service này
    private final ImageValidator imageValidator;         // bạn đã có validator này

    public Page<Category> search(String q, int page, int size, String sort) {
        String[] s = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(s.length > 1 ? s[1] : "desc"), s[0]);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(order));
        if (q == null || q.isBlank()) return categoryRepository.findAll(pageable);
        return categoryRepository.findByNameContainingIgnoreCase(q, pageable);
    }

    public Category get(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
    }

    @Transactional
    public Category create(String name, String description, MultipartFile image) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("Tên danh mục không được để trống");
        }
        Category c = new Category();
        c.setName(name.trim());
        c.setSlug(slugify(name));
        c.setDescription(description);

        if (image != null && !image.isEmpty()) {
            imageValidator.ensureValidImage(image);
            String path = fileStorageService.storeCategoryImage(image);
            c.setImageUrl(path);
        }
        return categoryRepository.save(c);
    }


    @Transactional
    public Category update(Long id, String name, String description, MultipartFile image) {
        Category c = get(id);

        if (StringUtils.hasText(name) && !name.trim().equalsIgnoreCase(c.getName())) {
            ensureNameNotExists(name);
            c.setName(name.trim());
            c.setSlug(slugify(name));
        }
        c.setDescription(StringUtils.hasText(description) ? description.trim() : null);

        if (image != null && !image.isEmpty()) {
            imageValidator.ensureValidImage(image);
            String path = fileStorageService.storeCategoryImage(image);
            c.setImageUrl(path);
        }
        return categoryRepository.save(c);
    }

    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) throw new NoSuchElementException("Category not found");
        categoryRepository.deleteById(id); // nếu muốn soft-delete: thêm trường 'deleted' rồi update thay vì delete
    }

    // -------- helpers --------
    private void ensureNameNotBlank(String name) {
        if (!StringUtils.hasText(name)) throw new IllegalArgumentException("Name is required");
    }
    private void ensureNameNotExists(String name) {
        boolean exists = categoryRepository
                .findByNameContainingIgnoreCase(name, PageRequest.of(0,1))
                .stream().anyMatch(c -> c.getName().equalsIgnoreCase(name));
        if (exists) throw new IllegalArgumentException("Category name already exists");
    }
    private String slugify(String input) {
        String nowhitespace = Pattern.compile("\\s+").matcher(input.trim()).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+","");
        return normalized.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9\\-]", "");
    }
}
