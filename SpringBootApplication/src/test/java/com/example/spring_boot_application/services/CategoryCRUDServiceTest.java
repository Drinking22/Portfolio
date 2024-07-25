package com.example.spring_boot_application.services;

import com.example.spring_boot_application.dto.CategoryDto;
import com.example.spring_boot_application.dto.NewsDto;
import com.example.spring_boot_application.entity.Category;
import com.example.spring_boot_application.entity.News;
import com.example.spring_boot_application.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryCRUDServiceTest {

    private static final long ID = 1L;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryCRUDService categoryCRUDService;

    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(ID);
        category.setTitle("Test Category");

        News news = new News();
        news.setId(ID);
        news.setTitle("Test News");
        news.setText("This is a test news text.");
        news.setDate(Instant.now());
        news.setCategory(category);
        category.setNews(List.of(news));

        categoryDto = new CategoryDto();
        categoryDto.setId(ID);
        categoryDto.setTitle("Test Category");

        NewsDto newsDto = new NewsDto();
        newsDto.setId(ID);
        newsDto.setTitle("Test News");
        newsDto.setText("This is a test news text.");
        newsDto.setDate(Instant.now());
        newsDto.setCategory(categoryDto.getTitle());
        categoryDto.setNews(List.of(newsDto));
    }

    @Test
    @DisplayName("Test for successfully receiving category by ID")
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));

        CategoryDto foundCategory = categoryCRUDService.getById(ID);

        assertEquals(categoryDto.getId(), foundCategory.getId());
        verify(categoryRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Test for the case when the category is not found")
    void testGetCategoryById_NotFound() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> categoryCRUDService.getById(ID));
        verify(categoryRepository, times(1)).findById(ID);
    }

    @Test
    void testGetAll() {
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDto> categories = (List<CategoryDto>) categoryCRUDService.getAll();

        assertEquals(1, categories.size());
        assertEquals(categoryDto.getId(), categories.get(0).getId());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void testCreate() {
        categoryCRUDService.create(categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testUpdate() {
        categoryCRUDService.update(categoryDto);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void testDelete() {
        categoryCRUDService.delete(ID);

        verify(categoryRepository, times(1)).deleteById(ID);
    }
}
