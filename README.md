# Sample GraphQL Spring Boot Java 11 Application

A Spring Jave 11 GraphQL example to understand the core concepts of GraphQL and solve N + 1 problem.

### Getting Started

#### GraphQL schema
###### author.graphqls
```graphql
type Query {
    authors: [Author]
    authorById(id: ID!): Author
}

type Mutation {
    addAuthor(authorInput: AuthorInput!): Author
}

input AuthorInput {
    name: String
}

type Author {
    id: ID!
    name: String!
    books: [Book]
}
```

###### book.graphqls
```graphql
type Book {
    id: ID!
    title: String!
    publisher: String
}
```
####GraphQL queries
###### Retrieve all authors
* Query
    ```graphql
    query{
        authors {
            id
            name
            books {
                id
                title
                publisher
            }
        }
    }
    ```
* Expected result
  ```json
  {
    "data": {
      "authors": [
        {
          "id": "1",
          "name": "Steve",
          "books": [
            {
              "id": "4",
              "title": "A Study in Pink",
              "publisher": "HBO"
            },
            {
              "id": "7",
              "title": "A Scandal in Belgravia",
              "publisher": "HBO"
            }
          ]
        },
        {
          "id": "2",
          "name": "Stephen",
          "books": [
            {
              "id": "5",
              "title": "The Blind Banker",
              "publisher": "HBO"
            }
          ]
        },
        {
          "id": "3",
          "name": "Mark",
          "books": [
            {
              "id": "6",
              "title": "The Great Game",
              "publisher": "HBO"
            },
            {
              "id": "8",
              "title": "The Hounds of Baskerville",
              "publisher": "HBO"
            },
            {
              "id": "9",
              "title": "The Sign of Three",
              "publisher": "HBO"
            }
          ]
        }
      ]
    }
  }
  ```
###### Retrieve author by ID
* Query
  ```graphql
  query{
      authorById(id: 1){
          id
          name
          books{
              title
          }
      }
  }
  ```
* Expected result
  ```json
  {
    "data": {
      "authorById": {
        "id": "1",
        "name": "Steve",
        "books": [
          {
            "title": "A Study in Pink"
          },
          {
            "title": "A Scandal in Belgravia"
          }
        ]
      }
    }
  }
  ```

###### Retrieve all books
* Query
  ```graphql
  query{
      books {
          id
          title
          publisher
      }
  }
  ```
* Expected result
  ```json
  {
    "data": {
      "books": [
        {
          "id": "4",
          "title": "A Study in Pink",
          "publisher": "HBO"
        },
        {
          "id": "5",
          "title": "The Blind Banker",
          "publisher": "HBO"
        },
        {
          "id": "6",
          "title": "The Great Game",
          "publisher": "HBO"
        },
        {
          "id": "7",
          "title": "A Scandal in Belgravia",
          "publisher": "HBO"
        },
        {
          "id": "8",
          "title": "The Hounds of Baskerville",
          "publisher": "HBO"
        },
        {
          "id": "9",
          "title": "The Sign of Three",
          "publisher": "HBO"
        }
      ]
    }
  }
  ```
###### Retrieve book by ID
* Query
  ```graphql
  query{
      bookById(id: 4){
          title
          publisher
      }
  }
  ```
* Expected result
  ```json
  {
    "data": {
      "bookById": {
        "title": "A Study in Pink",
        "publisher": "HBO"
      }
    }
  }
  ```


#### Mutations 
###### Add an author
* Mutation Query
  ```graphql
  mutation {
      addAuthor(authorInput: {name: "Arthur Conan"}){id name}
  }
  ```
* Expected result
  ```json
  {
    "data": {
      "addAuthor": {
        "id": "10",
        "name": "Arthur Conan"
      }
    }
  }
  ```
  
###### Add a book
* Mutation Query
  ```graphql
  mutation {
      addBook(bookInput: {title: "His Last Vow", publisher: "HBO", authorId: 10}){
          id
          title
          publisher
      }
  }
  ```
* Expected Result
  ```json
  {
    "data": {
      "addBook": {
        "id": "11",
        "title": "His Last Vow",
        "publisher": "HBO"
      }
    }
  }
  ```
###### Add books
* Mutation Query
  ```graphql
  mutation {
      addBooks(
          books: [
              {title: "The Abominable Bride", publisher: "HBO", authorId: 3}
              {title: "The Lying Detective", publisher: "HBO", authorId: 1}
              {title: "The Final Problem", publisher: "HBO", authorId: 1}
          ]){
          id
          title
          publisher
      }
  }
  ```
* Expected Result
  ```json
  {
    "data": {
      "addBooks": [
        {
          "id": "10",
          "title": "The Abominable Bride",
          "publisher": "HBO"
        },
        {
          "id": "11",
          "title": "The Lying Detective",
          "publisher": "HBO"
        },
        {
          "id": "12",
          "title": "The Final Problem",
          "publisher": "HBO"
        }
      ]
    }
  }
  ```
#### Solving N + 1 
###### What is N + 1 problem?
```markdown
N + 1 problem means that the GraphQL server executes many unnecessary round trips to fetch nested data.
```
Before we discuss the solution, let's take a look at the problem with one of our GraphQL schemas
```graphql
type Query {
    authors: [Author]
}

type Author {
    id: ID!
    name: String!
    books: [Book]
}

type Book {
  id: ID!
  title: String!
  publisher: String
}
```
and on the service side, it is implemented as:
```java
@QueryMapping
public Iterable<Author> authors() {
    log.info("Fetching all authors..");
    return authorRepository.findAll();
}

@SchemaMapping
public List<Book> books(Author author){
    log.info("Fetching books written by author {} ", author.getName());
    return bookService.getBooksByAuthor(author);
}
```
In the above code, Two `Data Fetcher`'s were defined:
1. `authors()` for field authors of the GraphQL object type `Author`
2. `books(..)` for field books of the type `Book`

In general, whenever the GraphQL service executes a query, it calls a Data Fetcher for every `field`.

Let's invoke a GraphQL request with this setup to fetch the following data:
```graphql
query {
    authors {
        name
        books {
            title
        }
    }
}
```

in the background, the GraphQL runtime engine will perform the below steps:
1. Parse the request and validates it against the schema.
2. Then calls the `author` Data Fetcher (handler method `authors()`) to fetch the author information once.
3. And, then calls the `books` Data Fetcher for each book.

Now, the possibility is `Authors` and `Books` may belong to different databases or microservices resulting in 1 + N network calls.

![](/Users/kadiums1/work/spring-graphql-java/Nplus1graphQL.png)

If we look at the service logs, it can be observed that `books(..)` Data Fetcher is called sequentially for every call to author.
```shell
[nio-8080-exec-1] c.d.g.c.DummyBotGraphQlController        : Fetching all authors..
[nio-8080-exec-1] c.d.g.c.DummyBotGraphQlController        : Fetching books written by author Steve 
[nio-8080-exec-1] c.d.g.c.DummyBotGraphQlController        : Fetching books written by author Stephen 
[nio-8080-exec-1] c.d.g.c.DummyBotGraphQlController        : Fetching books written by author Mark 
```
In Spring for GraphQL, this problem can be solved using `@BatchMapping` annotation. 

Let's modify the `books(..)` handler that takes `List<Author>` and returns a `Map` of `Author` and `List<Book>`

```java
@BatchMapping
public Map<Author, List<Book>> books(List<Author> authors){
    log.info("Fetching books written by authors {} ", authors);
    return bookService.getBooksByAuthors(authors);
}
```

> `@Batchmapping` batches the call to the `book` Data Fetcher.

If we now run the abover query, it can be observed that the GraphQL engine batches the call to the `books` field.

```shell
[nio-8080-exec-1] c.d.g.c.DummyBotGraphQlController        : Fetching all authors..
[nio-8080-exec-1] c.d.g.c.DummyBotGraphQlController        : Fetching books written by authors [com.dummybot.graphql.repositories.Author@16e3d7f4, com.dummybot.graphql.repositories.Author@26228ce9, com.dummybot.graphql.repositories.Author@45108db7] 
```


#### Reference Documentation
For further reference, please consider the following sections:

* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.12/maven-plugin/reference/html/)
* [Spring for GraphQL](https://docs.spring.io/spring-boot/docs/2.7.12/reference/html/web.html#web.graphql)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.12/reference/htmlsingle/#data.sql.jpa-and-spring-data)

#### Guides
The following guides illustrate how to use some features concretely:

* [Building a GraphQL service](https://spring.io/guides/gs/graphql-server/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

