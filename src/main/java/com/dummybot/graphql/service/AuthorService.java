package com.dummybot.graphql.service;

import com.dummybot.graphql.model.BookInput;
import com.dummybot.graphql.repositories.Author;
import com.dummybot.graphql.repositories.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    public Iterable<Author> allAuthors(){
        return authorRepository.findAll();
    }

    public Author authorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()){
            return author.get();
        }
        throw new IllegalArgumentException("No author found for the given id");
    }

    public Author addAuthor(Author author){
        return authorRepository.save(author);
    }

    public Author getBookAuthor(BookInput bookInput) {
        return authorRepository.findById(bookInput.getAuthorId()).orElseThrow(() -> new IllegalArgumentException("No authors found with the given author ID"));
    }
}
