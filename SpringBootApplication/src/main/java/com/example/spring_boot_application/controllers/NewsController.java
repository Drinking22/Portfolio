/**
 * Класс Контроллер Веб-приложения для управления новостной лентой.
 * Этот Контроллер предоставляет методы для получения информации о новости и передачи данной информации
 * на обработку в Сервис. Так же данный Контроллер передает ответы на запросы в формате HTTP.
 */

package com.example.spring_boot_application.controllers;

import com.example.spring_boot_application.dto.NewsDto;
import com.example.spring_boot_application.services.NewsCRUDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/news")
public class NewsController {

    private final NewsCRUDService newsCRUDService;

    @Autowired
    public NewsController(NewsCRUDService newsCRUDService) {
        this.newsCRUDService = newsCRUDService;
    }

    /**
     * Метод для получения информации о новости по её уникальному идентификатору.
     *
     * @param id Уникальный идентификатор новости.
     * @return Получение ответа с информацией о новости и кодом ответа HTTP.
     */
    @GetMapping("/{id}")
    public ResponseEntity getNewsById(@PathVariable Long id) {
        try {
            NewsDto newsDto = newsCRUDService.getById(id);
            return new ResponseEntity(newsCRUDService.getById(id), HttpStatus.OK);
        } catch (Exception e) {
            String message = "Категория с ID " + id + " не найдена";
            return new ResponseEntity(message, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Метод для получения списка всех новостей.
     *
     * @return Получения списка всех новостей.
     */
    @GetMapping
    public Collection<NewsDto> getAllNews() {
        return newsCRUDService.getAll();
    }

    /**
     * Метод для создания новости.
     *
     * @param newsDto Полученые данные о новости.
     */
    @PostMapping
    public void createNews(@RequestBody NewsDto newsDto) {
        newsCRUDService.create(newsDto);
    }

    /**
     * Метод для обновления новости.
     *
     * @param id      Уникальный идентификатор новости.
     * @param newsDto Полученые данные о новости.
     */
    @PutMapping("/{id}")
    public void updateNews(@PathVariable Long id, @RequestBody NewsDto newsDto) {
        newsDto.setId(id);
        newsCRUDService.update(newsDto);
    }

    /**
     * Метод для удаления новости.
     *
     * @param id Уникальный идентификатор новости.
     */
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsCRUDService.delete(id);
    }
}
