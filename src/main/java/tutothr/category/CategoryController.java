package tutothr.category;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;


import jakarta.validation.Valid;
import tutothr.common.interfaces.MapperI;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService, MapperI mapper) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getAllDTOs();
        model.addAttribute("categories", categories);
        return "/views/category/categories";
    }

    @GetMapping({ "/admin/categories/add", "/admin/categories/update/{id}" })
    public String getCreatePage(Model model, @PathVariable(required = false) Long id) {
        CategoryDTO category;
        if (id != null) {
            category = categoryService.findDTOById(id);
        } else {
            category = new CategoryDTO();
        }
        model.addAttribute("category", category);
        return "/views/category/category";
    }

    @PostMapping({ "/admin/categories/save" })
    public String createCategory(Model model, @ModelAttribute @Valid CategoryDTO category, BindingResult result) {
        Category duplicateTitle = categoryService.findByTitle(category.getTitle());
        if (duplicateTitle != null) {
            result.rejectValue("title", "error.category", "A category with this title already exists.");
        }
        if (result.hasErrors()) {
            category = categoryService.handleValidationErrors(category, result.getFieldErrors());
            model.addAttribute("category", category);
            return "/views/category/category"; // Return to the form view with validation errors
        }
        categoryService.save(categoryService.mapToEntity(category));
        return "redirect:/admin/categories";
    }

    @PutMapping("/admin/categories/save/{id}")
    public String updateCategory(Model model, @ModelAttribute @Valid CategoryDTO category, BindingResult result) {
        Category duplicateTitle = categoryService.findByTitle(category.getTitle());
        if (duplicateTitle != null && !duplicateTitle.getId().equals(category.getId())) {
            result.rejectValue("title", "error.category", "A category with this title already exists.");
            category = categoryService.findDTOById(category.getId());
        }
        if (result.hasErrors()) {
            category = categoryService.handleValidationErrors(category, result.getFieldErrors());
            model.addAttribute("category", category);
            return "/views/category/category";
        }
        categoryService.update(category);
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable(required = true) Long id) {
        if (id == null) {
            return "/views/category/category";
        }
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }

}
