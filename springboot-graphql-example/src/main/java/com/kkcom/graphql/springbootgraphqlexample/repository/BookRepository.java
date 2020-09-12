package com.kkcom.graphql.springbootgraphqlexample.repository;

import com.kkcom.graphql.springbootgraphqlexample.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,String>
{
}
