package com.dummybot.graphql;

import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.AuthorRepository;
import com.dummybot.graphql.repositories.Book;
import com.dummybot.graphql.repositories.BookRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringGraphqlJava11Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringGraphqlJava11Application.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(AuthorRepository authorRepository, BookRepository bookRepository){
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
		};
	}

}
