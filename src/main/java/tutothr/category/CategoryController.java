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

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;



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
        return "/category/categories";
    }
    @GetMapping({"/admin/categories/add", "/admin/categories/add/{id}"})
    public String getCreatePage(Model model, @PathVariable(required = false) Long id) {
        System.out.println("getCreatePage called with id=" + id);
        Category category;
        if (id != null) {
            System.out.println("editing category id=" + id);
            category = categoryService.getCategoryById(id);
        } else {
            System.out.println("creating new category");
            category = new Category();
        }
        model.addAttribute("category", category);
        System.out.println(category.getId());
        return "/category/category";
    }
    

    @PostMapping({"/admin/categories/add/process"})
    public String createCategory(@ModelAttribute @Valid Category category, BindingResult result) {
        //TODO: process POST request
        // categoryService
        
        Optional<Category> existingCategory = categoryService.getCategoryByTitle(category.getTitle());
        if (existingCategory.isPresent()) {
            // Category with the same title exists, handle the error
            result.rejectValue("title", "error.category", "A category with this title already exists.");
            return "/category/category"; // Return to the form view with error message
        }
        if (result.hasErrors()) {
            return "/category/category"; // Return to the form view with validation errors
        }
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/update/process")
    public String updateCategory(@ModelAttribute @Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/category/category";
        }
        Optional<Category> existingCategory = categoryService.getCategoryByTitle(category.getTitle());
        if (existingCategory.isPresent() && !existingCategory.get().getId().equals(category.getId())) {
            result.rejectValue("title", "error.category", "A category with this title already exists.");
            return "/category/category";
        }
        System.out.println(category.getId());
        categoryService.saveCategory(category);
        return "redirect:/admin/categories";
    }

    @DeleteMapping("/admin/categories/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(required = true) Long id) {
        System.out.println("Deleting category id=" + id);
        if(id == null) {
            return ResponseEntity.badRequest().build();
        }
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok().build();
    }
    
    
    
    
}
