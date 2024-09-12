package business;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class BookController {
    private final DataAccess da;
    private HashMap<String, Book> bookMap;

    public BookController() {
        da = new DataAccessFacade();
        HashMap<String, Book> booksMap = da.readBooksMap();
        this.bookMap = booksMap != null ? booksMap : new HashMap<>();
    }

    public void reloadData() {
        HashMap<String, Book> booksMap = da.readBooksMap();
        this.bookMap = booksMap != null ? booksMap : new HashMap<>();
    }

    public Collection<Book> getBooks() {
//        DataAccess da = new DataAccessFacade();
//        HashMap<String, Book> bookMap = da.readBooksMap();
        return bookMap.values();
    }

    public boolean isExisted(String isbn) {
        return bookMap.containsKey(isbn);
    }

    public void saveNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors) {
        Book newBook = new Book(isbn, title, maxCheckoutLength, authors);
        da.saveNewBook(newBook);
        reloadData();
    }

    public void deleteBook(String isbn) {
        da.deleteBook(isbn);
        reloadData();
    }

    public void updateBook(String oldISBN, String newISBN, String title, int maxCheckoutLength, List<Author> authors) {
        Book book = new Book(newISBN, title, maxCheckoutLength, authors);
        da.updateBook(oldISBN, book);
        reloadData();
    }
}
