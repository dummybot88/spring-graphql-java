package com.dummybot.graphql.controller;

import com.dummybot.graphql.model.AuthorInput;
import com.dummybot.graphql.model.BookInput;
import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.AuthorRepository;
import com.dummybot.graphql.repositories.Book;
import com.dummybot.graphql.repositories.BookRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class DummyBotGraphQlController {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    public DummyBotGraphQlController(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @QueryMapping
    public Iterable<Author> authors() {
        return authorRepository.findAll();
    }

    @QueryMapping
    public Optional<Author> authorById(@Argument Long id) {
        return authorRepository.findById(id);
    }

    @MutationMapping
    public Author addAuthor(@Argument AuthorInput authorInput){
        return authorRepository.save(new Author((authorInput.getName())));
    }

    @QueryMapping
    public Iterable<Book> books() {
        return bookRepository.findAll();
    }

    @QueryMapping
    public Optional<Book> bookById(@Argument Long id) {
        return bookRepository.findById(id);
    }

    @MutationMapping
    public Book addBook(@Argument BookInput bookInput){
        Author author = getBookAuthor(bookInput);
        return bookRepository.save(new Book(bookInput.getTitle(), bookInput.getPublisher(), author));
    }

    @MutationMapping
    public List<Book> addBooks(@Argument List<BookInput> books){
        return books.stream()
                .map(bookInput -> new Book(bookInput.getTitle(), bookInput.getPublisher(), getBookAuthor(bookInput)))
                .map(bookRepository::save)
                .collect(Collectors.toList());
    }

    private Author getBookAuthor(BookInput bookInput) {
        return authorRepository.findById(bookInput.getAuthorId()).orElseThrow(() -> new IllegalArgumentException("No authors found with the given author ID"));
    }
}
