package com.dummybot.graphql;

import com.dummybot.graphql.repositories.*;
import graphql.scalars.ExtendedScalars;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.util.List;

@SpringBootApplication
public class SpringGraphqlJava11Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringGraphqlJava11Application.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(AuthorRepository authorRepository, BookRepository bookRepository, TasklistRepository tasklistRepository){
		return args -> {
			Author authorOne = authorRepository.save(new Author("Steve"));
			Author authorTwo = authorRepository.save(new Author("Stephen"));
			Author authorThree = authorRepository.save(new Author("Mark"));
			bookRepository.saveAll(
					List.of(new Book("A Study in Pink","HBO",authorOne),
							new Book("The Blind Banker","HBO",authorTwo),
							new Book("The Great Game","HBO",authorThree),
							new Book("A Scandal in Belgravia","HBO",authorOne),
							new Book("The Hounds of Baskerville","HBO",authorThree),
							new Book("The Sign of Three","HBO",authorThree)));
			tasklistRepository.saveAll(
					List.of(new Tasklist("Reminder Process", "para10201", "Reminder: Copyright transfer letter sent", LocalDate.now().minusMonths(1), LocalDate.now(), "Sravan"),
							new Tasklist("Item Process", "para10201", "Login metadata", LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(1), "Sravan"),
							new Tasklist("Issue Process", "para10201", "aagro 15/2: Compile issue", LocalDate.now().minusMonths(3), LocalDate.now().plusMonths(2), "Sravan"),
							new Tasklist("Issue Process", "para10201", "Physically compile issue", LocalDate.now().minusMonths(4), LocalDate.now().plusMonths(3), "Sravan"))
			);
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(final CorsRegistry registry) {
				registry.addMapping("/graphql/**")
						.allowedOrigins(CorsConfiguration.ALL)
						.allowedHeaders(CorsConfiguration.ALL)
						.allowedMethods(CorsConfiguration.ALL);
			}
		};
	}

	@Bean
	public RuntimeWiringConfigurer runtimeWiringConfigurer() {
		return wiringBuilder -> wiringBuilder.scalar(ExtendedScalars.Date);
	}

}
