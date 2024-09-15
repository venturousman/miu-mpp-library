package business;

import java.util.List;

import business.Book;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public interface ControllerInterface {
    public void login(String id, String password) throws LoginException;

    public void logout();

    public List<LibraryMember> allMembers();

    public List<String> allMemberIds();

    public List<String> allBookIds();

    public List<Book> allBooks();

    public List<Checkout> allCheckouts();

//    public List<Author> allAuthors();

    public Book getBookById(String isbn);

    public LibraryMember getMemberById(String memberID);

    public void saveNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors);

    public void deleteBook(String isbn);

    public void updateBook(String oldISBN, String newISBN, String title, int maxCheckoutLength, List<Author> authors);

    public void saveNewCheckout(String isbn, String memberID);

    public void saveNewMember(String memberID, String firstName, String lastName, String telephone, String street, String city, String state, String zip);

    public void updateMember(String oldMemberID, String newMemberID, String firstName, String lastName, String telephone, String street, String city, String state, String zip);

    public void deleteMember(String memberID);

    public void saveNewCopy(String isbn);
}
