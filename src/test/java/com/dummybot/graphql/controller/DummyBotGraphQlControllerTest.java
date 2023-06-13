package com.dummybot.graphql.controller;

import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.AuthorRepository;
import com.dummybot.graphql.repositories.BookRepository;
import com.dummybot.graphql.service.AuthorService;
import com.dummybot.graphql.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@GraphQlTest(DummyBotGraphQlController.class)
public class DummyBotGraphQlControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private BookRepository bookRepository;


    @Test
    public void testFindAllAuthors(){
        Mockito.when(authorService.allAuthors()).thenReturn(stubAuthors());
        String document = "query {authors { name }}";
        graphQlTester.document(document).execute().path("authors").entityList(Author.class).hasSize(3);
    }

    @Test
    public void testAuthorById(){
        Mockito.when(authorService.authorById(1L)).thenReturn(stubAuthorById(1L));
        String document = "query {authorById(id:1) { name }}";
        graphQlTester.document(document).execute().path("authorById").entity(Author.class).satisfies(author -> assertEquals("Steve", author.getName()));
    }

    private Author stubAuthorById(long l) {
        return new Author("Steve");
    }

    private Iterable<Author> stubAuthors() {
        return List.of(new Author("Steve"), new Author("Stephen"), new Author("Mark"));
    }

}