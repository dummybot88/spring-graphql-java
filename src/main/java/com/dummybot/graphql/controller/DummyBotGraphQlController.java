package com.dummybot.graphql.controller;

import com.dummybot.graphql.model.AuthorInput;
import com.dummybot.graphql.model.BookInput;
import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.Book;
import com.dummybot.graphql.service.AuthorService;
import com.dummybot.graphql.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class DummyBotGraphQlController {

    private final AuthorService authorService;
    private final BookService bookService;

    public DummyBotGraphQlController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @QueryMapping
    public Iterable<Author> authors() {
        log.info("Fetching all authors..");
        return authorService.allAuthors();
    }

 /*   @SchemaMapping
    public List<Book> books(Author author){
        log.info("Fetching books written by author {} ", author.getName());
        return bookService.getBooksByAuthor(author);
    }*/

    @BatchMapping
    public Map<Author, List<Book>> books(List<Author> authors){
        log.info("Fetching books written by authors {} ", authors);
        return bookService.getBooksByAuthors(authors);
    }

    @QueryMapping
    public Optional<Author> authorById(@Argument Long id) {
        return authorService.authorById(id);
    }

    @MutationMapping
    public Author addAuthor(@Argument AuthorInput authorInput){
        return authorService.addAuthor(new Author((authorInput.getName())));
    }

    @QueryMapping
    public Iterable<Book> allBooks() {
        return bookService.books();
    }

    @QueryMapping
    public Optional<Book> bookById(@Argument Long id) {
        return bookService.bookById(id);
    }

    @MutationMapping
    public Book addBook(@Argument BookInput bookInput){
        Author author = authorService.getBookAuthor(bookInput);
        return bookService.addBook(new Book(bookInput.getTitle(), bookInput.getPublisher(), author));
    }

    @MutationMapping
    public List<Book> addBooks(@Argument List<BookInput> books){
        return books.stream()
                .map(bookInput ->
                        bookService.addBook(new Book(bookInput.getTitle(), bookInput.getPublisher(), authorService.getBookAuthor(bookInput))))
                .collect(Collectors.toList());
    }
}