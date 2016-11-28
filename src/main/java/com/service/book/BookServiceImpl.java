package com.service.book;

import com.domain.Book;
import com.repository.BookRepository;
import com.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("bookService")
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    @Transactional
    public Book save(Book entity) {
        return bookRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete (Book entity) {
        bookRepository.delete(entity);
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        bookRepository.deleteByName(name);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> findByName(String name) {
        return bookRepository.findByName(name);
    }
}
