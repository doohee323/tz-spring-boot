package example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import example.domain.Book;
import example.repository.BookRepository;

@RestController
@RequestMapping(value = "/")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    // http://localhost:8088/rest/book/{"bookId":"1","bookName":"bookName1","user":"user1","password":"password1"}
    @RequestMapping(method = RequestMethod.GET, value = "/book/{parms}")
    public Book getBook(@PathVariable String parms) {
        JsonObject obj = (JsonObject) new JsonParser().parse(parms);

        Book book = new Book(obj.get("bookId").getAsString());
        book.bookName = obj.get("bookName").getAsString();
        bookRepository.save(book);

        book = bookRepository.findById(book.bookId);
        if(book == null) {
        	book = new Book();
        }
        return book;
    }

}
