package business;

import java.io.Serializable;
import java.time.LocalDate;

public final class CheckoutEntry implements Serializable {
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BookCopy bookCopy;
    private Checkout checkout;

    public CheckoutEntry(Checkout checkout, BookCopy bookCopy, LocalDate checkoutDate, LocalDate dueDate) {
        this.checkout = checkout;
        this.bookCopy = bookCopy;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
    }

    public BookCopy getBookCopy() {
        return bookCopy;
    }

    public Checkout getCheckout() {
        return checkout;
    }

    public LocalDate getCheckoutDate() {
        return checkoutDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }
}
