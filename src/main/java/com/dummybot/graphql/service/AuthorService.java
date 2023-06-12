package com.dummybot.graphql.service;

import com.dummybot.graphql.model.BookInput;
import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public Iterable<Author> allAuthors(){
        return authorRepository.findAll();
    }

    public Optional<Author> authorById(Long id) {
        return authorRepository.findById(id);
    }

    public Author addAuthor(Author author){
        return authorRepository.save(author);
    }

    public Author getBookAuthor(BookInput bookInput) {
        return authorRepository.findById(bookInput.getAuthorId()).orElseThrow(() -> new IllegalArgumentException("No authors found with the given author ID"));
    }
}
