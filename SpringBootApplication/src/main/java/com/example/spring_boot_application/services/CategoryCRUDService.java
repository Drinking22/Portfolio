/**
 * Класс-сервис для обработки информации о категориях.
 * Данный Сервис получает информацию от пользователя (с сайта) и обрабатывает её, преобразуя информацию из базы данных
 * в информацию для отображения пользователю.
 */

package com.example.spring_boot_application.services;

import com.example.spring_boot_application.dto.CategoryDto;
import com.example.spring_boot_application.entity.Category;
import com.example.spring_boot_application.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryCRUDService implements CRUDService<CategoryDto> {

    private final CategoryRepository categoryRepository;

    /**
     * Метод получения информации о категории по её уникальному идентификатору.
     * Получает информацию от пользователя, проверяет наличие категории по ID в базе данных и
     * преобразует информацию из базы данных в информацию для пользователя.
     *
     * @param id Уникальный идентификатор категории.
     * @return Возвращает информацию о категории для пользователя.
     */
    @Override
    public CategoryDto getById(Long id) {
        log.info("Get by ID: " + id);
        Category category = categoryRepository.findById(id).orElseThrow();
        return mapToDto(category);
    }

    /**
     * Метод для получения информации о всех категориях из базы данных.
     *
     * @return Возвращает информацию о категориях для пользователя.
     */
    @Override
    public Collection<CategoryDto> getAll() {
        log.info("Get all");
        return categoryRepository.findAll()
                .stream()
                .map(CategoryCRUDService::mapToDto)
                .toList();
    }

    /**
     * Метод для создания категории. Получает информацию от пользователя о категории
     * и сохраняет информацию в базу данных.
     *
     * @param categoryDto Полученая информация о категории от пользователя.
     */
    @Override
    public void create(CategoryDto categoryDto) {
        categoryRepository.save(mapToEntity(categoryDto));
    }

    /**
     * Метод для обновлении информации о категории. Получает информацию о категории от пользователя,
     * проверяет наличие категории по ID в базе данных и преобразует информацию полученную от пользователя в
     * информацию для базы данных. Обновляет запись о категории в базе данных.
     *
     * @param categoryDto Полученая информация о категории от пользователя.
     */
    @Override
    public void update(CategoryDto categoryDto) {
        categoryRepository.save(mapToEntity(categoryDto));
    }

    /**
     * Метод для удаления категории из базы данных.
     *
     * @param id Уникальный идентификатор категории.
     */
    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    /**
     * Метод для преобразования информации о категории из базы данных в информацию для пользователя.
     *
     * @param category Информация о категории из базы данных.
     * @return Информацию для пользователя.
     */
    public static CategoryDto mapToDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setTitle(category.getTitle());
        categoryDto.setNews(category.getNews()
                .stream()
                .map(NewsCRUDService::mapToDto)
                .toList());
        return categoryDto;
    }

    /**
     * Метод для преобразования информации о категории от пользователя в информацию для базы данных.
     *
     * @param categoryDto Информация от пользователя.
     * @return Информацию о категории в базу данных.
     */
    public static Category mapToEntity(CategoryDto categoryDto) {
        Category category = new Category();
        category.setId(categoryDto.getId());
        category.setTitle(categoryDto.getTitle());
        category.setNews(categoryDto.getNews()
                .stream()
                .map(NewsCRUDService::mapToEntity)
                .toList());
        return category;
    }
}
