package com.service.command;

import com.domain.Book;
import com.service.book.BookService;
import com.service.command.model.Command;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("executeCommandService")
public class ExecuteCommandsServiceImpl implements ExecuteCommandsService {

    private static final String ADD_COMMAND = "add book";
    private static final String DELETE_COMMAND = "remove";
    private static final String EDIT_COMMAND = "edit book";
    private static final String SHOW_ALL_COMMAND = "all books";
    private static final String EXIT_COMMAND = "exit";

    private static final String commandRegexp = "^(add book|remove|edit book|all books|exit)";
    private static final String bookNameRegexp = "(\".+\")$";

    @Autowired
    private BookService bookService;

    @Override
    public void excute() {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                String input = bufferedReader.readLine();
                if (StringUtils.isNotEmpty(input)) {
                    if (getCommand(input).equals(EXIT_COMMAND)) {
                        return;
                    }
                    Command command = parse(input);
                    excute(command);
                }
            } catch (ParseException ex) {
                showMessage(ex.getMessage());
            } catch (ExcuteCommandException ex) {
                showMessage(ex.getMessage());
            } catch (Exception ex) {
                showMessage("Internal error.");
            }
        }
    }

    public void excute(Command command) throws ExcuteCommandException {
        Book book = new Book(command.getBookName(), command.getBookAuthor());
        if (command.getName().equals(ADD_COMMAND)) {
            executeAddCommand(book);
        } else if(command.getName().equals(DELETE_COMMAND)) {
            executeDeleteCommand(book.getName());
        } else if(command.getName().equals(EDIT_COMMAND)) {
            executeEditCommand(book.getName());
        } else if(command.getName().equals(SHOW_ALL_COMMAND)) {
            executeShowAllCommand(book);
        } else {
            throw new ExcuteCommandException("Unknown command");
        }
    }

    private void executeAddCommand(Book book) throws ExcuteCommandException {
        try {
            bookService.save(book);
            showMessage("Book: '" + book + "' added success.");
        } catch (Exception ex) {
            throw new ExcuteCommandException("Book: '" + book + "' added error.");
        }
    }

    private void executeDeleteCommand(String bookName) throws ExcuteCommandException {
        List<Book> books = null;
        Book bookForDelete = null;
        try {
            books = bookService.findByName(bookName);
            if(books.size() > 1) {
                bookForDelete = getSelectedBook(books);
            } else {
                bookForDelete = books.get(0);
            }
            bookService.delete(bookForDelete);
            showMessage("Book: '" + bookForDelete + "' deleted success.");
        } catch (Exception ex) {
            throw new ExcuteCommandException("Book: '" + bookName + "' deleted error.");
        }
    }

    private void executeEditCommand(String bookName) throws ExcuteCommandException {
        List<Book> books = null;
        Book bookForEdit = null;
        try {
            books = bookService.findByName(bookName);
            if(books.size() > 1) {
                bookForEdit = getSelectedBook(books);
            } else {
                bookForEdit = books.get(0);
            }
            setNewValueForBook(bookForEdit);
            Book newBook = bookService.save(bookForEdit);
            showMessage("Book: '" + newBook + "' edit success.");
        } catch (Exception ex) {
            throw new ExcuteCommandException("Book: '" + bookName + "' edit error.");
        }
    }

    private void executeShowAllCommand(Book book) throws ExcuteCommandException {
        try {
            bookService.findAll().forEach(b -> showMessage(b.toString()));
        } catch (Exception ex) {
            throw new ExcuteCommandException("Book not found.");
        }
    }

    private void setNewValueForBook(Book book) throws IOException {
        showMessage("Enter:");
        showMessage("New book name:");
        String newBookName = StringUtils.trim(readLine());
        if(StringUtils.isNotEmpty(newBookName)) {
            book.setName(newBookName);
        }
        showMessage("New book author:");
        String newBookAuthor = StringUtils.trim(readLine());
        if(StringUtils.isNotEmpty(newBookAuthor)) {
            book.setAuthor(newBookAuthor);
        }
    }

    private Book getSelectedBook(List<Book> books) throws IOException {
        int index = 0;
        showMessage("We have few books with such name please choose one by typing a number of book:");
        for(Book book : books) {
            showMessage(String.valueOf(++index)+". " + book);
        }
        return books.get(Integer.valueOf(readLine())-1);
    }

    private String testRegexp(String regexp, String str) {
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(str);
        return m.find() ? m.group() : null;
    }

    private String getCommand(String input) throws ParseException {
        String command = testRegexp(commandRegexp, input);
        if (StringUtils.isEmpty(command)) {
            throw new ParseException("Command not found.");
        }
        return command;
    }

    private String getBookName(String input) throws ParseException{
        String bookName = testRegexp(bookNameRegexp, input);
        if (StringUtils.isEmpty(bookName)) {
            throw new ParseException("Book name not found.");
        }
        return StringUtils.trim(bookName);
    }

    private String getBookNameForDeleteEdit(String input, String command) throws ParseException{
        String bookName = StringUtils.substring(input, command.length(), input.length());
        if (StringUtils.isEmpty(bookName)) {
            throw new ParseException("Book name not found.");
        }
        return StringUtils.trim(bookName);
    }

    private String getAuthorName(String input, String command, String bookName) throws ParseException {
        String[] arr = StringUtils.substringsBetween(input, command, bookName);
        if (ArrayUtils.isNotEmpty(arr)) {
            return StringUtils.trim(arr[0]);
        } else {
            throw new ParseException("Book author not found.");
        }
    }

    private Command parse(String input)  throws ParseException {
        input = StringUtils.trim(input);

        String command = getCommand(input);
        if(command.equals(SHOW_ALL_COMMAND)) {
            return new Command(command);
        }
        if(command.equals(DELETE_COMMAND) || command.equals(EDIT_COMMAND)) {
            return new Command(command, getBookNameForDeleteEdit(input, command));
        }
        String bookName = getBookName(input);
        String authorName = getAuthorName(input, command, bookName);
        return new Command(command, StringUtils.strip(bookName,"\""), authorName);
    }

    private void showMessage(String message) {
        System.out.println(message);
    }

    private String readLine() throws IOException {
        return new BufferedReader(new InputStreamReader(System.in)).readLine();
    }

}
