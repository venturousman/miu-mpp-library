package business;

import java.io.Serializable;

final public class Author extends Person implements Serializable {
    private String bio;
    private Boolean credentials; // Author is expert in that area and having qualification

    public String getBio() {
        return bio;
    }

    public Boolean getCredentials() {
        return credentials;
    }

    public Author(String f, String l, String t, Address a, String bio) {
        super(f, l, t, a);
        this.bio = bio;
    }

    private static final long serialVersionUID = 7508481940058530471L;
}
