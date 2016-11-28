package com.repository;

import com.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("Delete from Book b where b.name = :name")
    public void deleteByName(@Param("name") String name);

    @Query("SELECT b from Book b where b.name = :name")
    public List<Book> findByName(@Param("name") String name);
}
