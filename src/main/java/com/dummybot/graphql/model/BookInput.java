package com.dummybot.graphql.model;

import lombok.Data;

@Data
public class BookInput {

    private String title;
    private String publisher;
    private Long authorId;
}
