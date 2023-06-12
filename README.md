# Sample GraphQL Spring Boot Java 11 Application
### GraphQL schema
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
###GraphQL queries
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


### Mutations 
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

### Reference Documentation
For further reference, please consider the following sections:

* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.12/maven-plugin/reference/html/)
* [Spring for GraphQL](https://docs.spring.io/spring-boot/docs/2.7.12/reference/html/web.html#web.graphql)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.12/reference/htmlsingle/#data.sql.jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a GraphQL service](https://spring.io/guides/gs/graphql-server/)
* [Building a Reactive RESTful Web Service](https://spring.io/guides/gs/reactive-rest-service/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

