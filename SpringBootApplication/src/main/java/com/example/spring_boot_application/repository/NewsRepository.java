/**
 * Интерфейс для настройки связей между сущностями в веб-приложении.
 */

package com.example.spring_boot_application.repository;

import com.example.spring_boot_application.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
}
