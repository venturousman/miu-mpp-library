package business;

import java.io.Serializable;
import java.time.LocalDate;

public final class CheckoutEntry implements Serializable {
    private LocalDate checkoutDate;
    private BookCopy bookCopy;
    private Checkout checkout;

    public CheckoutEntry(Checkout checkout, BookCopy bookCopy, LocalDate checkoutDate) {
        this.checkout = checkout;
        this.bookCopy = bookCopy;
        this.checkoutDate = checkoutDate;
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
}
