/**
 * Класс-сущность для хранения информации о новостях в базе данных.
 */

package com.example.spring_boot_application.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private Instant date;

    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
}
