package business;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BookController {
    public Collection<Book> getBooks() {
        DataAccess da = new DataAccessFacade();
        //List<Book> books = new ArrayList<Book>();
        return da.readBooksMap().values();
    }
}
