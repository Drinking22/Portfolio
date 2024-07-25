/**
 * Класс Контроллер Веб-приложения для управления категориями новостей.
 * Этот Контроллер предоставляет методы для получения информации о категориях новостей и передачи данной информации
 * на обработку в Сервис. Так же данный Контроллер передает ответы на запросы в формате HTTP.
 */

package com.example.spring_boot_application.controllers;

import com.example.spring_boot_application.dto.CategoryDto;
import com.example.spring_boot_application.services.CategoryCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryCRUDService categoryCRUDService;

    /**
     * Метод для получения информации о категории по её уникальному идентификатору.
     *
     * @param id Уникальный идентификатор категории.
     * @return Получение ответа с информацией о категории и кодом ответа HTTP.
     */
    @GetMapping("/{id}")
    public ResponseEntity getCategoryById(@PathVariable Long id) {
        try {
            CategoryDto category = categoryCRUDService.getById(id);
            return new ResponseEntity(category, HttpStatus.OK);
        } catch (Exception e) {
            String message = "Категория с ID " + id + " не найдена";
            return new ResponseEntity(message, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Метод для получения списка всех категорий.
     *
     * @return Получения списка всех категорий.
     */
    @GetMapping
    public Collection<CategoryDto> getAllCategories() {
        return categoryCRUDService.getAll();
    }

    /**
     * Метод для создания категории.
     *
     * @param categoryDto Полученые данные о категории.
     */
    @PostMapping
    public void createCategory(@RequestBody CategoryDto categoryDto) {
        categoryCRUDService.create(categoryDto);
    }

    /**
     * Метод для обновления категории.
     *
     * @param id          Уникальный идентификатор категории.
     * @param categoryDto Полученые данные о категории.
     */
    @PutMapping("/{id}")
    public void updateCategory(@PathVariable Long id, @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(id);
        categoryCRUDService.update(categoryDto);
    }

    /**
     * Метод для удаления категории.
     *
     * @param id Уникальный идентификатор категории.
     */
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
       categoryCRUDService.getById(id);
    }
}
