package dataaccess;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

import business.Book;
import business.BookCopy;
import business.Checkout;
import business.LibraryMember;

public class DataAccessFacade implements DataAccess {

    enum StorageType {
        BOOKS, MEMBERS, USERS, CHECKOUTS
    }
    // Windows user can use
	
	/*public static final String OUTPUT_DIR = System.getProperty("user.dir") 
			+ "\\src\\dataaccess\\storage";*/

    // For Mac Users path can use /
    public static final String OUTPUT_DIR = System.getProperty("user.dir")
            + "/src/dataaccess/storage";

    public static final String DATE_PATTERN = "MM/dd/yyyy";

    //implement: other save operations
    public void saveNewMember(LibraryMember member) {
        HashMap<String, LibraryMember> members = readMemberMap();
        String memberId = member.getMemberId();
        members.put(memberId, member);
        saveToStorage(StorageType.MEMBERS, members);
    }

    public void saveNewCheckout(Checkout checkout) {
        HashMap<String, Checkout> checkouts = readCheckoutMap();
        String checkoutId = checkout.getId();
        checkouts.put(checkoutId, checkout);
        saveToStorage(StorageType.CHECKOUTS, checkouts);
    }

    public void saveNewBook(Book book) {
        HashMap<String, Book> books = readBooksMap();
        String bookId = book.getIsbn();
        books.put(bookId, book);
        saveToStorage(StorageType.BOOKS, books);
    }

    public void deleteBook(String isbn) {
        HashMap<String, Book> books = readBooksMap();
        books.remove(isbn);
        saveToStorage(StorageType.BOOKS, books);
    }

    public void updateBook(String isbn, Book book) {
        HashMap<String, Book> books = readBooksMap();
        String bookId = book.getIsbn();
        if (bookId.equalsIgnoreCase(isbn)) {
            books.replace(isbn, book);
        } else {
            books.remove(isbn);
            books.put(bookId, book);
        }
        saveToStorage(StorageType.BOOKS, books);
    }

    @Override
    public void updateBookCopyAvailability(String isbn, int copyNum) {
        HashMap<String, Book> books = readBooksMap();
        var book = books.get(isbn);
        var bookCopy = book.getCopy(copyNum);
        if (bookCopy != null) {
            bookCopy.changeAvailability();
            saveToStorage(StorageType.BOOKS, books);
        }
    }

    @Override
    public void deleteMember(String memberId) {
        HashMap<String, LibraryMember> members = readMemberMap();
        members.remove(memberId);
        saveToStorage(StorageType.MEMBERS, members);
    }

    @Override
    public void updateMember(String memberId, LibraryMember member) {
        HashMap<String, LibraryMember> members = readMemberMap();
        String newMemberId = member.getMemberId();
        if (newMemberId.equalsIgnoreCase(memberId)) {
            members.replace(memberId, member);
        } else {
            members.remove(memberId);
            members.put(newMemberId, member);
        }
        saveToStorage(StorageType.MEMBERS, members);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Book> readBooksMap() {
        //Returns a Map with name/value pairs being isbn -> Book
        return (HashMap<String, Book>) readFromStorage(StorageType.BOOKS);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, LibraryMember> readMemberMap() {
        //Returns a Map with name/value pairs being memberId -> LibraryMember
        return (HashMap<String, LibraryMember>) readFromStorage(StorageType.MEMBERS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public HashMap<String, Checkout> readCheckoutMap() {
        return (HashMap<String, Checkout>) readFromStorage(StorageType.CHECKOUTS);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, User> readUserMap() {
        //Returns a Map with name/value pairs being userId -> User
        return (HashMap<String, User>) readFromStorage(StorageType.USERS);
    }

    /////load methods - these place test data into the storage area
    ///// - used just once at startup

    static void loadBookMap(List<Book> bookList) {
        HashMap<String, Book> books = new HashMap<String, Book>();
        bookList.forEach(book -> books.put(book.getIsbn(), book));
        saveToStorage(StorageType.BOOKS, books);
    }

    static void loadUserMap(List<User> userList) {
        HashMap<String, User> users = new HashMap<String, User>();
        userList.forEach(user -> users.put(user.getId(), user));
        saveToStorage(StorageType.USERS, users);
    }

    static void loadMemberMap(List<LibraryMember> memberList) {
        HashMap<String, LibraryMember> members = new HashMap<String, LibraryMember>();
        memberList.forEach(member -> members.put(member.getMemberId(), member));
        saveToStorage(StorageType.MEMBERS, members);
    }

    static void loadCheckout(List<Checkout> checkoutList) {
        HashMap<String, Checkout> checkouts = new HashMap<>();
        saveToStorage(StorageType.CHECKOUTS, checkouts);
    }

    static void saveToStorage(StorageType type, Object ob) {
        ObjectOutputStream out = null;
        try {
            Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
            out = new ObjectOutputStream(Files.newOutputStream(path));
            out.writeObject(ob);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                }
            }
        }
    }

    static Object readFromStorage(StorageType type) {
        ObjectInputStream in = null;
        Object retVal = null;
        try {
            Path path = FileSystems.getDefault().getPath(OUTPUT_DIR, type.toString());
            in = new ObjectInputStream(Files.newInputStream(path));
            retVal = in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                }
            }
        }
        return retVal;
    }

    final static class Pair<S, T> implements Serializable {

        S first;
        T second;

        Pair(S s, T t) {
            first = s;
            second = t;
        }

        @Override
        public boolean equals(Object ob) {
            if (ob == null) return false;
            if (this == ob) return true;
            if (ob.getClass() != getClass()) return false;
            @SuppressWarnings("unchecked")
            Pair<S, T> p = (Pair<S, T>) ob;
            return p.first.equals(first) && p.second.equals(second);
        }

        @Override
        public int hashCode() {
            return first.hashCode() + 5 * second.hashCode();
        }

        @Override
        public String toString() {
            return "(" + first.toString() + ", " + second.toString() + ")";
        }

        private static final long serialVersionUID = 5399827794066637059L;
    }
}
