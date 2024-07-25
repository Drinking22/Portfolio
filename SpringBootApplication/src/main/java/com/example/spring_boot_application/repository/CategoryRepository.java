/**
 * Интерфейс для настройки связей между сущностями в веб-приложении.
 */

package com.example.spring_boot_application.repository;

import com.example.spring_boot_application.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByTitle(String title);
}
