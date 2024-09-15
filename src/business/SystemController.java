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
    public List<LibraryMember> allMembers() {
        DataAccess da = new DataAccessFacade();
        return new ArrayList<>(da.readMemberMap().values());
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

//    @Override
//    public List<Author> allAuthors() {
//        DataAccess da = new DataAccessFacade();
//        var books = da.readBooksMap().values();
//        List<Author> authors = new ArrayList<>();
//        for (Book book : books) {
//            var bookAuthors = book.getAuthors();
//            for (Author author : bookAuthors) {
//                if (!authors.contains(author)) {
//                    authors.add(author);
//                }
//            }
//        }
//        return authors;
//    }

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
        LibraryMember member = getMemberById(memberID);
        Book book = getBookById(isbn);
        if (book == null) return;
        int maxCheckoutLength = book.getMaxCheckoutLength();
        BookCopy bookCopy = book.getNextAvailableCopy();
        if (bookCopy == null) return;

        // Get the current date
        LocalDate checkoutDate = LocalDate.now();
        // Add N days to the current date
        LocalDate dueDate = checkoutDate.plusDays(maxCheckoutLength);

        Checkout newCheckout = new Checkout(member, bookCopy, checkoutDate, dueDate);
        DataAccess da = new DataAccessFacade();
        da.saveNewCheckout(newCheckout);
//        da.updateBookCopyAvailability(isbn, bookCopy.getCopyNum());
        // 2nd way:
         bookCopy.changeAvailability();
         book.updateCopies(bookCopy);
         da.updateBook(isbn, book);
    }

    @Override
    public void saveNewMember(String memberID, String firstName, String lastName, String telephone, String street, String city, String state, String zip) {
        var address = new Address(street, city, state, zip);
        var member = new LibraryMember(memberID, firstName, lastName, telephone, address);
        DataAccess da = new DataAccessFacade();
        da.saveNewMember(member);
    }

    @Override
    public void updateMember(String oldMemberID, String newMemberID, String firstName, String lastName, String telephone, String street, String city, String state, String zip) {
        var address = new Address(street, city, state, zip);
        var member = new LibraryMember(newMemberID, firstName, lastName, telephone, address);
        DataAccess da = new DataAccessFacade();
        da.updateMember(oldMemberID, member);
    }

    @Override
    public void deleteMember(String memberID) {
        DataAccess da = new DataAccessFacade();
        da.deleteMember(memberID);
    }

    @Override
    public void saveNewCopy(String isbn) {
        Book book = getBookById(isbn);
        if (book == null) return;
        book.addCopy();
        DataAccess da = new DataAccessFacade();
        da.updateBook(isbn, book);
    }
}
