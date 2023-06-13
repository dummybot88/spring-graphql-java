package com.dummybot.graphql.service;

import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.Book;
import com.dummybot.graphql.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Iterable<Book> books() {
        return bookRepository.findAll();
    }

    public List<Book> getBooksByAuthor(Author author){
        return bookRepository.findByAuthorId(author.getId());
    }

    public Map<Author, List<Book>> getBooksByAuthors(List<Author> authors){
        return authors.stream()
                .collect(
                        Collectors.toMap(
                                Function.identity(),
                                author -> bookRepository.findByAuthorId(author.getId())
                        )
                );
    }

    public Optional<Book> bookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book addBook(Book book){
        return bookRepository.save(book);
    }

}
