package com.kkcom.graphql.springbootgraphqlexample.service;

import com.kkcom.graphql.springbootgraphqlexample.model.Book;
import com.kkcom.graphql.springbootgraphqlexample.repository.BookRepository;
import com.kkcom.graphql.springbootgraphqlexample.service.datafetcher.AllBookDataFetcher;
import com.kkcom.graphql.springbootgraphqlexample.service.datafetcher.BookDataFetcher;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService
{
  @Value("classpath:books.graphql")
  Resource resource;
  private GraphQL graphQL;

  @Autowired
  BookRepository bookRepository;
  @Autowired
  private AllBookDataFetcher allBooksDataFetcher;
  @Autowired
  private BookDataFetcher bookDataFetcher;

  @PostConstruct
  private void loadSchema () throws IOException
  {
      loadDataIntoHSQL();
      //get the schema
      File schemaFile = resource.getFile();
      //parse schema
      TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(schemaFile);
      RuntimeWiring wiring = buildRuntimeWiring();
      GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry,wiring);
      graphQL = GraphQL.newGraphQL(schema).build();
  }

    private void loadDataIntoHSQL ()
    {
        Stream.of(
            new Book("123",
                "A Scandal in Bohemia1",
                "The Strand Magazine",
                new String[] { "Sir Arthur Conan Doyle" },
                "July 1891"),
            new Book("456",
                "A Scandal in Bohemia2",
                "The Strand Magazine",
                new String[] { "Sir Arthur Conan Doyle" },
                "July 1891"),
            new Book("789",
                "A Scandal in Bohemia3",
                "The Strand Magazine",
                new String[] { "Sir Arthur Conan Doyle" },
                "July 1891")).forEach(book -> {
            bookRepository.save(book);
        });
    }

    private RuntimeWiring buildRuntimeWiring ()
    {
        return RuntimeWiring.newRuntimeWiring()
               .type("Query",typeWiring -> typeWiring
                                  .dataFetcher("allBooks",allBooksDataFetcher)
                                  .dataFetcher("book", bookDataFetcher))
               .build();
    }

    public GraphQL getGraphQL ()
    {
        return graphQL;
    }
}
