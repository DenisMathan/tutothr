package tutothr.category;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    @DisplayName("Should render categories list page with all categories")
    void getCategories_shouldRenderList() throws Exception {
        Category c1 = new Category();
        c1.setTitle("Math");
        Category c2 = new Category();
        c2.setTitle("Science");
        List<Category> categories = Arrays.asList(c1, c2);
        given(categoryService.getAllCategories()).willReturn(categories);

        mockMvc.perform(get("/admin/categories"))
            .andExpect(status().isOk())
            .andExpect(view().name("/category/categories"))
            .andExpect(model().attributeExists("categories"));
    }

    @Test
    @DisplayName("Should return create page with empty category when id is absent")
    void getCreatePage_withoutId_shouldReturnEmptyCategory() throws Exception {
        mockMvc.perform(get("/admin/categories/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("/category/category"))
            .andExpect(model().attributeExists("category"));
    }

    @Test
    @DisplayName("Should return edit page with category loaded when id is present")
    void getCreatePage_withId_shouldLoadCategory() throws Exception {
        Category c = new Category();
        c.setTitle("Math");
        given(categoryService.getCategoryById(1L)).willReturn(c);

        mockMvc.perform(get("/admin/categories/add/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("/category/category"))
            .andExpect(model().attributeExists("category"));
    }

    @Test
    @DisplayName("Should reject duplicate title on create and return form view")
    void createCategory_duplicateTitle_shouldReject() throws Exception {
        Category existing = new Category();
        existing.setTitle("Math");
        given(categoryService.getCategoryByTitle(eq("Math"))).willReturn(Optional.of(existing));

        mockMvc.perform(put("/admin/categories/add/process")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Math")
                .param("description", "desc"))
            .andExpect(status().isOk())
            .andExpect(view().name("/category/category"));
    }

    @Test
    @DisplayName("Should create category when valid and unique, then redirect to list")
    void createCategory_validUnique_shouldSaveAndRedirect() throws Exception {
        given(categoryService.getCategoryByTitle(eq("Physics"))).willReturn(Optional.empty());
        doNothing().when(categoryService).saveCategory(any(Category.class));

        mockMvc.perform(put("/admin/categories/add/process")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("title", "Physics")
                .param("description", "desc"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryService).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should reject update when another category has same title")
    void updateCategory_duplicateTitle_shouldReject() throws Exception {
        Category other = new Category();
        // simulate existing category with different id
        given(categoryService.getCategoryByTitle(eq("Math"))).willReturn(Optional.of(other));
        // Bind the id of the form object to 2 (so not equal to other.getId which is null)

        mockMvc.perform(post("/admin/categories/update/process")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "2")
                .param("title", "Math")
                .param("description", "desc"))
            .andExpect(status().isOk())
            .andExpect(view().name("/category/category"));
    }

    @Test
    @DisplayName("Should update category when valid and unique, then redirect")
    void updateCategory_valid_shouldSaveAndRedirect() throws Exception {
        given(categoryService.getCategoryByTitle(eq("Chemistry"))).willReturn(Optional.empty());

        mockMvc.perform(post("/admin/categories/update/process")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1")
                .param("title", "Chemistry")
                .param("description", "desc"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryService).saveCategory(any(Category.class));
    }

    @Test
    @DisplayName("Should delete category by id and return 200")
    void deleteCategory_shouldReturnOk() throws Exception {
        doNothing().when(categoryService).deleteCategoryById(1L);

        mockMvc.perform(delete("/admin/categories/delete/1"))
            .andExpect(status().isOk());

        verify(categoryService).deleteCategoryById(1L);
    }
}
