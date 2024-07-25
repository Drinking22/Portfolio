/**
 * Класс-сервис для обработки информации о новостях.
 * Данный Сервис получает информацию от пользователя (с сайта) и обрабатывает её, преобразуя информацию из базы данных
 * в информацию для отображения пользователю.
 */

package com.example.spring_boot_application.services;

import com.example.spring_boot_application.dto.NewsDto;
import com.example.spring_boot_application.entity.Category;
import com.example.spring_boot_application.entity.News;
import com.example.spring_boot_application.repository.CategoryRepository;
import com.example.spring_boot_application.repository.NewsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
@Slf4j
public class NewsCRUDService implements CRUDService<NewsDto> {

    private final NewsRepository newsRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public NewsCRUDService(NewsRepository newsRepository, CategoryRepository categoryRepository) {
        this.newsRepository = newsRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Метод получения информации о новости по её уникальному идентификатору.
     * Получает информацию от пользователя, проверяет наличие категории по ID в базе данных и
     * преобразует информацию из базы данных в информацию для пользователя.
     *
     * @param id Уникальный идентификатор новости.
     * @return Возвращает информацию о новости для пользователя.
     */
    @Override
    public NewsDto getById(Long id) {
        log.info("Get by ID: " + id);
        News news = newsRepository.findById(id).orElseThrow();
        return mapToDto(news);
    }

    /**
     * Метод для получения информации о всех новостях из базы данных.
     *
     * @return Возвращает информацию о новостях для пользователя.
     */
    @Override
    public Collection<NewsDto> getAll() {
        log.info("Get all");
        return newsRepository.findAll()
                .stream()
                .map(NewsCRUDService::mapToDto)
                .toList();
    }

    /**
     * Метод для создания новости. Получает информацию от пользователя о новости
     * и сохраняет информацию в базу данных.
     *
     * @param newsDto Полученая информация о новости от пользователя.
     */
    @Override
    public void create(NewsDto newsDto) {
        log.info("Create ");
        News news = mapToEntity(newsDto);
        String categoryName = newsDto.getCategory();
        Optional<Category> category = categoryRepository.findByTitle(categoryName);
        news.setCategory(category.get());
        newsRepository.save(news);
    }

    /**
     * Метод для обновлении информации о новости. Получает информацию о новости от пользователя,
     * проверяет наличие новости по ID в базе данных и преобразует информацию полученную от пользователя в
     * информацию для базы данных. Обновляет запись о новости в базе данных.
     *
     * @param newsDto Полученая информация о новости от пользователя.
     */
    @Override
    public void update(NewsDto newsDto) {
        log.info("Update ");
        News news = mapToEntity(newsDto);
        String categoryName = newsDto.getCategory();
        Optional<Category> category = categoryRepository.findByTitle(categoryName);
        news.setCategory(category.get());
        newsRepository.save(news);
    }

    /**
     * Метод для удаления новости из базы данных.
     *
     * @param id Уникальный идентификатор новости.
     */
    @Override
    public void delete(Long id) {
        log.info("Delete " + id);
        newsRepository.deleteById(id);
    }

    /**
     * Метод для преобразования информации о новости из базы данных в информацию для пользователя.
     *
     * @param news Информация о новости из базы данных.
     * @return Информацию для пользователя.
     */
    public static NewsDto mapToDto(News news) {
        NewsDto newsDto = new NewsDto();
        newsDto.setId(news.getId());
        newsDto.setTitle(news.getTitle());
        newsDto.setText(news.getText());
        newsDto.setDate(news.getDate());
        newsDto.setCategory(news.getCategory().getTitle());
        return newsDto;
    }

    /**
     * Метод для преобразования информации о новости от пользователя в информацию для базы данных.
     *
     * @param newsDto Информация от пользователя.
     * @return Информацию о новости в базу данных.
     */
    public static News mapToEntity(NewsDto newsDto) {
        News news = new News();
        news.setId(newsDto.getId());
        news.setTitle(newsDto.getTitle());
        news.setText(newsDto.getText());
        return news;
    }
}
