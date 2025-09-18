package hieesu.vn.controller;

import hieesu.vn.dto.CategoryForm;
import hieesu.vn.entity.Category;
import hieesu.vn.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryMvcController {

    private final CategoryService categoryService;

    @GetMapping
    public String index(@RequestParam(defaultValue = "") String q,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id,desc") String sort,
                        Model model) {
        Page<Category> data = categoryService.search(q, page, size, sort);
        model.addAttribute("data", data);
        model.addAttribute("q", q);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
        return "admin/category/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("form", new CategoryForm());
        model.addAttribute("mode", "create");
        model.addAttribute("action", "/admin/categories/create"); // ✅
        return "admin/category/form";
    }

    @PostMapping("/create")
    public String createSubmit(@Valid @ModelAttribute("form") CategoryForm form,
                               BindingResult binding,
                               RedirectAttributes ra) {
        if (binding.hasErrors()) return "admin/category/form";
        MultipartFile image = form.getImage();
        categoryService.create(form.getName(), form.getDescription(), image);
        ra.addFlashAttribute("msg", "Tạo danh mục thành công");
        return "redirect:/admin/categories";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category c = categoryService.get(id);
        CategoryForm form = new CategoryForm();
        form.setId(c.getId());
        form.setName(c.getName());
        form.setDescription(c.getDescription());
        model.addAttribute("form", form);
        model.addAttribute("mode", "edit");
        model.addAttribute("category", c);
        model.addAttribute("action", "/admin/categories/" + id + "/edit"); // ✅
        return "admin/category/form";
    }

    @PostMapping("/{id}/edit")
    public String editSubmit(@PathVariable Long id,
                             @Valid @ModelAttribute("form") CategoryForm form,
                             BindingResult binding,
                             RedirectAttributes ra) {
        if (binding.hasErrors()) return "admin/category/form";
        MultipartFile image = form.getImage();
        categoryService.update(id, form.getName(), form.getDescription(), image);
        ra.addFlashAttribute("msg", "Cập nhật danh mục thành công");
        return "redirect:/admin/categories";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        categoryService.delete(id);
        ra.addFlashAttribute("msg", "Đã xóa danh mục");
        return "redirect:/admin/categories";
    }
}
