/**
 * Данное веб-приложение разработано для управления (получения и обработки) новостей, а так же для
 * хранения данных из новостной ленты.
 *
 * @author John Doe
 * @version 26.06.2024
 */

package com.example.spring_boot_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
