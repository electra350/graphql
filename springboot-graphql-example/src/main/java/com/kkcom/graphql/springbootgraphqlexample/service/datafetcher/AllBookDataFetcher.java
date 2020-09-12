package com.kkcom.graphql.springbootgraphqlexample.service.datafetcher;

import com.kkcom.graphql.springbootgraphqlexample.model.Book;
import com.kkcom.graphql.springbootgraphqlexample.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AllBookDataFetcher implements DataFetcher<List<Book>>
{

    @Autowired
    BookRepository bookRepository;

    @Override
    public List<Book> get (DataFetchingEnvironment dataFetchingEnvironment) throws
        Exception
    {
        return bookRepository.findAll();
    }
}
