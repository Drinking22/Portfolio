/**
 * Класс-сущность для отображения категорий новостей, полученных от пользователей.
 */

package com.example.spring_boot_application.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryDto {

    private Long id;
    private String title;
    private List<NewsDto> news = new ArrayList<>();
}
