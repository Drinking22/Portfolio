package com.example.spring_boot_application.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.spring_boot_application.dto.CategoryDto;
import com.example.spring_boot_application.dto.NewsDto;
import com.example.spring_boot_application.services.CategoryCRUDService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    private static final long ID = 1L;

    @Mock
    private CategoryCRUDService categoryCRUDService;

    @InjectMocks
    private CategoryController categoryController;

    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp() {
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
    public void testGetCategoryById_Success() {
        when(categoryCRUDService.getById(ID)).thenReturn(categoryDto);

        ResponseEntity response = categoryController.getCategoryById(ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoryDto, response.getBody());
    }

    @Test
    @DisplayName("Test for the case when the category is not found")
    public void testGetCategoryById_NotFound() {
        when(categoryCRUDService.getById(ID)).thenThrow(new RuntimeException("Категория с ID " + ID + " не найдена"));

        ResponseEntity response = categoryController.getCategoryById(ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Категория с ID " + ID + " не найдена", response.getBody());
    }

    @Test
    public void testGetAllCategories() {
        List<CategoryDto> categories = Collections.singletonList(categoryDto);

        when(categoryCRUDService.getAll()).thenReturn(categories);

        Collection<CategoryDto> response = categoryController.getAllCategories();

        assertEquals(1, response.size());
        assertTrue(response.contains(categoryDto));
    }

    @Test
    public void testCreateCategory() {
        categoryController.createCategory(categoryDto);

        verify(categoryCRUDService, times(1)).create(categoryDto);
    }

    @Test
    public void testUpdateCategory() {
        categoryController.updateCategory(ID, categoryDto);

        verify(categoryCRUDService, times(1)).update(categoryDto);
    }

    @Test
    public void testDeleteCategory() {
        categoryController.deleteCategory(ID);

        verify(categoryCRUDService, times(1)).getById(ID);
    }
}
