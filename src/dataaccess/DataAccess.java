package dataaccess;

import java.util.HashMap;

import business.*;
import dataaccess.DataAccessFacade.StorageType;

public interface DataAccess {
    public HashMap<String, Book> readBooksMap();

    public HashMap<String, User> readUserMap();

    public HashMap<String, LibraryMember> readMemberMap();

    public HashMap<String, Checkout> readCheckoutMap();

    public void saveNewCheckout(Checkout checkout);

    public void saveNewMember(LibraryMember member);

    public void saveNewBook(Book book);

    public void deleteBook(String isbn);

    public void updateBook(String isbn, Book book);

    public void updateBookCopyAvailability(String isbn, int copyNum);

    public void deleteMember(String memberId);

    public void updateMember(String memberId, LibraryMember member);
}
