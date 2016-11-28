package com.service.book;

import com.domain.Book;

import java.util.List;

public interface BookService {

    public Book save(Book entity);

    public void delete (Book entity);

    public void deleteByName(String name);

    public List<Book> findAll();

    public List<Book> findByName(String name);
}
