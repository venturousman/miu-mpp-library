package business;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dataaccess.Auth;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;
import dataaccess.User;

public class SystemController implements ControllerInterface {
    public static Auth currentAuth = null;

    public void login(String id, String password) throws LoginException {
        DataAccess da = new DataAccessFacade();
        HashMap<String, User> map = da.readUserMap();
        if (!map.containsKey(id)) {
            throw new LoginException("ID " + id + " not found");
        }
        String passwordFound = map.get(id).getPassword();
        if (!passwordFound.equals(password)) {
            throw new LoginException("Password incorrect");
        }
        currentAuth = map.get(id).getAuthorization();
    }

    @Override
    public void logout() {
        currentAuth = null; // reset
    }

    @Override
    public List<String> allMemberIds() {
        DataAccess da = new DataAccessFacade();
        return new ArrayList<>(da.readMemberMap().keySet());
    }

    @Override
    public List<String> allBookIds() {
        DataAccess da = new DataAccessFacade();
        return new ArrayList<>(da.readBooksMap().keySet());
    }

    @Override
    public List<Book> allBooks() {
        DataAccess da = new DataAccessFacade();
        return new ArrayList<>(da.readBooksMap().values());
    }

    @Override
    public List<Checkout> allCheckouts() {
        DataAccess da = new DataAccessFacade();
        return new ArrayList<>(da.readCheckoutMap().values());
    }

    @Override
    public Book getBookById(String isbn) {
        DataAccess da = new DataAccessFacade();
        var books = da.readBooksMap();
        return books.get(isbn);
    }

    @Override
    public LibraryMember getMemberById(String memberID) {
        DataAccess da = new DataAccessFacade();
        var members = da.readMemberMap();
        return members.get(memberID);
    }

    @Override
    public void saveNewBook(String isbn, String title, int maxCheckoutLength, List<Author> authors) {
        Book newBook = new Book(isbn, title, maxCheckoutLength, authors);
        DataAccess da = new DataAccessFacade();
        da.saveNewBook(newBook);
    }

    @Override
    public void deleteBook(String isbn) {
        DataAccess da = new DataAccessFacade();
        da.deleteBook(isbn);
    }

    @Override
    public void updateBook(String oldISBN, String newISBN, String title, int maxCheckoutLength, List<Author> authors) {
        Book book = new Book(newISBN, title, maxCheckoutLength, authors);
        DataAccess da = new DataAccessFacade();
        da.updateBook(oldISBN, book);
    }

    @Override
    public void saveNewCheckout(String isbn, String memberID) {
        DataAccess da = new DataAccessFacade();
        LibraryMember member = getMemberById(memberID);
        Book book = getBookById(isbn);
        int maxCheckoutLength = book.getMaxCheckoutLength();
        BookCopy bookCopy = book.getNextAvailableCopy();

        // Get the current date
        LocalDate currentDate = LocalDate.now();
        // Add 5 days to the current date
        LocalDate dueDate = currentDate.plusDays(maxCheckoutLength);

        Checkout newCheckout = new Checkout(member, bookCopy, dueDate);
        da.saveNewCheckout(newCheckout);
    }
}
