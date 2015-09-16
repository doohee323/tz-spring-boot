package example.domain;

import java.io.Serializable;

public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    public String bookId;

    public String bookName;

    public Book(String bookId) {
        this.bookId = bookId;
    }

    public Book() {
    }
}