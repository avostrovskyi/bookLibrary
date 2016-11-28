package com.service.command.model;

public class Command {

    private String name;

    private String bookName;

    private String bookAuthor;

    public Command() {

    }

    public Command(String name, String bookName, String bookAuthor) {
        this.bookAuthor = bookAuthor;
        this.bookName = bookName;
        this.name = name;
    }

    public Command(String name) {
        this.name = name;
    }

    public Command(String name, String bookName) {
        this.bookName = bookName;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
