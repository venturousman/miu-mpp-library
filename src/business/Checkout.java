package business;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public final class Checkout implements Serializable {

    //    private static final long serialVersionUID = 1L;
    private String checkoutId;
    private CheckoutEntry[] entries;
    private LibraryMember member;

    //    private double fineFee;
//    private LocalDate paidDate;

//    public Checkout(LibraryMember member, List<CheckoutEntry> entries) {
//        this.member = member;
//        this.entries = entries;
//    }

    public Checkout(LibraryMember member, BookCopy bookCopy, LocalDate checkoutDate, LocalDate dueDate) {
        // Generate a new UUID
        UUID uuid = UUID.randomUUID();
        this.checkoutId = uuid.toString();
        this.member = member;
        this.entries = new CheckoutEntry[]{new CheckoutEntry(this, bookCopy, checkoutDate, dueDate)};
    }

    public CheckoutEntry[] getEntries() {
        return entries;
    }

    public LibraryMember getMember() {
        return member;
    }

    public String getId() {
        return checkoutId;
    }

    public int getNumEntries() {
        if (entries == null) return 0;
        return entries.length;
    }
}
