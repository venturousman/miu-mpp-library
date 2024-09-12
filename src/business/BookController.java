package business;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class BookController {
    //    private final DataAccess da;
    private final HashMap<String, Book> bookMap;

    public BookController() {
        DataAccess da = new DataAccessFacade();
        HashMap<String, Book> books = da.readBooksMap();
        this.bookMap = books != null ? books : new HashMap<>();
    }

    public Collection<Book> getBooks() {
//        DataAccess da = new DataAccessFacade();
//        HashMap<String, Book> bookMap = da.readBooksMap();
        return bookMap.values();
    }

    public boolean isExisted(String isbn) {
        return bookMap.containsKey(isbn);
    }
}
