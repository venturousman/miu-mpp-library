package librarysystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionUID = 1L;

    private String memberId;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String telephone;

    public Member(String memberId, String firstName, String lastName, String street, String city, String state, String zip, String telephone) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Member ID: " + memberId + "\n" +
                "First Name: " + firstName + "\n" +
                "Last Name: " + lastName + "\n" +
                "Street: " + street + "\n" +
                "City: " + city + "\n" +
                "State: " + state + "\n" +
                "ZIP: " + zip + "\n" +
                "Telephone: " + telephone;
    }

private void loadMembers() {
    try (FileInputStream fileIn = new FileInputStream("members.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn)) {
        Member member = (Member) in.readObject();
        System.out.println(member);  // This will print out the member details
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
}
}