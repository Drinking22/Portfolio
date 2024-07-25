package com.example.spring_boot_application.controllers;

import com.example.spring_boot_application.dto.CategoryDto;
import com.example.spring_boot_application.dto.NewsDto;
import com.example.spring_boot_application.services.NewsCRUDService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NewsControllerTest {

    private static final long ID = 1L;

    @Mock
    private NewsCRUDService newsCRUDService;

    @InjectMocks
    private NewsController newsController;

    private NewsDto newsDto;

    @BeforeEach
    void setUp() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(ID);
        categoryDto.setTitle("Test Category");

        newsDto = new NewsDto();
        newsDto.setId(ID);
        newsDto.setTitle("Test News");
        newsDto.setText("This is a test news text.");
        newsDto.setDate(Instant.now());
        newsDto.setCategory(categoryDto.getTitle());
        categoryDto.setNews(List.of(newsDto));
    }

    @Test
    @DisplayName("Test for successfully receiving news by ID")
    public void testGetNewsById_Success() {
        when(newsCRUDService.getById(ID)).thenReturn(newsDto);

        ResponseEntity response = newsController.getNewsById(ID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(newsDto, response.getBody());
    }

    @Test
    @DisplayName("Test for the case when the news is not found")
    public void testGetNewsBiID_NotFound() {
        when(newsCRUDService.getById(ID)).thenThrow(new RuntimeException("Категория с ID " + ID + " не найдена"));

        ResponseEntity response = newsController.getNewsById(ID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Категория с ID " + ID + " не найдена", response.getBody());
    }

    @Test
    public void testGetAllNews() {
        List<NewsDto> news = Collections.singletonList(newsDto);

        when(newsController.getAllNews()).thenReturn(news);

        Collection<NewsDto> response = newsController.getAllNews();

        assertEquals(1, news.size());
        assertTrue(response.contains(newsDto));
    }

    @Test
    public void testCreateNews() {
        newsController.createNews(newsDto);

        verify(newsCRUDService, times(1)).create(newsDto);
    }

    @Test
    public void testUpdateNews() {
        newsController.updateNews(ID, newsDto);

        verify(newsCRUDService, times(1)).update(newsDto);
    }

    @Test
    public void testDeleteNews() {
        newsController.deleteNews(ID);

        verify(newsCRUDService, times(1)).delete(ID);
    }
}
