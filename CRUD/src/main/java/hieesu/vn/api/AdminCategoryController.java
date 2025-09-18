package hieesu.vn.api;

import hieesu.vn.entity.Category;
import hieesu.vn.repository.CategoryRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public Page<Category> list(@RequestParam(defaultValue = "") String q,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "id,desc") String sort) {
        String[] s = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(s.length > 1 ? s[1] : "desc"), s[0]);
        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size), Sort.by(order));
        return (q == null || q.isBlank())
                ? categoryRepository.findAll(pageable)
                : categoryRepository.findByNameContainingIgnoreCase(q, pageable);
    }

    @GetMapping("/{id}")
    public Category get(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category create(@Valid @RequestBody Category c) {
        c.setId(null);
        return categoryRepository.save(c);
    }

    @PutMapping("/{id}")
    public Category update(@PathVariable Long id, @Valid @RequestBody Category body) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Category not found"));
        c.setName(body.getName());
        c.setSlug(body.getSlug());
        c.setDescription(body.getDescription());
        c.setImageUrl(body.getImageUrl());
        return categoryRepository.save(c);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        if (!categoryRepository.existsById(id)) throw new NoSuchElementException("Category not found");
        categoryRepository.deleteById(id);
    }
}
