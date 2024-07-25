/**
 * Интерфейс для предоставления методов для обработки информации в Сервисе
 */

package com.example.spring_boot_application.services;

import java.util.Collection;

public interface CRUDService<T> {
    T getById(Long id);
    Collection<T> getAll();
    void create(T item);
    void update(T item);
    void delete(Long id);
}
