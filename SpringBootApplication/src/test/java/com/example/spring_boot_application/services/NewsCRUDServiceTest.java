package com.example.spring_boot_application.services;

import com.example.spring_boot_application.dto.NewsDto;
import com.example.spring_boot_application.entity.Category;
import com.example.spring_boot_application.entity.News;
import com.example.spring_boot_application.repository.CategoryRepository;
import com.example.spring_boot_application.repository.NewsRepository;
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
public class NewsCRUDServiceTest {

    private static final long ID = 1L;

    @Mock
    private NewsRepository newsRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private NewsCRUDService newsCRUDService;

    private News news;
    private NewsDto newsDto;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(ID);
        category.setTitle("Test Category");

        news = new News();
        news.setId(ID);
        news.setTitle("Test News");
        news.setText("This is a test news text.");
        news.setDate(Instant.now());
        news.setCategory(category);
        category.setNews(List.of(news));

        newsDto = new NewsDto();
        newsDto.setId(ID);
        newsDto.setTitle("Test News");
        newsDto.setText("This is a test news text.");
        newsDto.setDate(Instant.now());
        newsDto.setCategory(category.getTitle());
    }

    @Test
    @DisplayName("Test for successfully receiving news by ID")
    void testGetNewsById_Success() {
        when(newsRepository.findById(ID)).thenReturn(Optional.of(news));

        NewsDto foundNewsDto = newsCRUDService.getById(ID);

        assertEquals(newsDto.getId(), foundNewsDto.getId());
        verify(newsRepository, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Test for the case when the news is not found")
    void testGetNewsById_NotFoundId() {
        when(newsRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> newsCRUDService.getById(ID));
        verify(newsRepository, times(1)).findById(ID);
    }

    @Test
    void testGetAll() {
        when(newsRepository.findAll()).thenReturn(List.of(news));

        List<NewsDto> listOfNews = (List<NewsDto>) newsCRUDService.getAll();

        assertEquals(1, listOfNews.size());
        assertEquals(newsDto.getId(), listOfNews.get(0).getId());
        verify(newsRepository, times(1)).findAll();
    }

    @Test
    void testCreate() {
        when(categoryRepository.findByTitle("Test Category")).thenReturn(Optional.of(category));

        newsCRUDService.create(newsDto);

        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void testUpdate() {
        when(categoryRepository.findByTitle("Test Category")).thenReturn(Optional.of(category));

        newsCRUDService.update(newsDto);

        verify(newsRepository, times(1)).save(any(News.class));
    }

    @Test
    void testDelete() {
        newsCRUDService.delete(ID);

        verify(newsRepository, times(1)).deleteById(ID);
    }


}
