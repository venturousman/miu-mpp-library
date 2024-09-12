package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
    public void login(String id, String password) throws LoginException;

    public void logout();
    
    public List<String> allMemberIds();

    public List<String> allBookIds();

    public List<Book> allBooks();
}
