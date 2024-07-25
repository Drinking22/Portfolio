/**
 * Класс-сущность для отображения новостей, полученных от пользователей.
 */

package com.example.spring_boot_application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class NewsDto {

    private Long id;
    private String title;
    private String text;
    private Instant date;
    private String category;

    public NewsDto(Long id, String title, String text, Instant date, String category) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.date = Instant.now();
        this.category = category;
    }

    public NewsDto() {

    }
}
