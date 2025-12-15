package tutothr.category;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import tutothr.common.models.Field;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Controller
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/admin/categories")
    public String getCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "/views/category/categories";
    }

    @GetMapping({ "/admin/categories/add", "/admin/categories/update/{id}" })
    public String getCreatePage(Model model, @PathVariable(required = false) Long id) {
        Category category;
        if (id != null) {
            category = categoryService.findById(id);
        } else {
            category = new Category();
        }
        model.addAttribute("category", category);
        model.addAttribute("fields", categoryService.getFields());
        return "/views/category/category";
    }

    @PostMapping({ "/admin/categories/save" })
    public String createCategory(@ModelAttribute @Valid Category category, BindingResult result) {
        // TODO: process POST request
        // categoryService

        Category existingCategory = categoryService.getCategoryByTitle(category.getTitle());
        if (existingCategory != null) {
            // Category with the same title exists, handle the error
            result.rejectValue("title", "error.category", "A category with this title already exists.");
            return "/views/category/category"; // Return to the form view with error message
        }
        if (result.hasErrors()) {
            return "/views/category/category"; // Return to the form view with validation errors
        }
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @PutMapping("/admin/categories/save/{id}")
    public String updateCategory(Model model, @ModelAttribute @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/views/category/category";
        }
        Category duplicateTitle = categoryService.getCategoryByTitle(category.getTitle());
        if (duplicateTitle != null && !duplicateTitle.getId().equals(category.getId())) {
            result.rejectValue("title", "error.category", "A category with this title already exists.");
            model.addAttribute("fields", categoryService.getFields());
            return "/views/category/category";
        }
        Category updatedCategory = (Category) categoryService.update(category);
        categoryService.save(updatedCategory);
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
