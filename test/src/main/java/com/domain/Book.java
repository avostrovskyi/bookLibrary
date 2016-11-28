package com.domain;

import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

@Entity
@Table(name = "book", uniqueConstraints=@UniqueConstraint(columnNames={"author", "name"}))
public class Book extends AbstractPersistable<Long> {

    @Version
    private int version;

    private String author;

    private String name;

    public Book() {

    }

    public Book(String name, String author) {
        this.author = author;
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Author - " + this.author + ", " + "Name - " + this.name;

    }
}
