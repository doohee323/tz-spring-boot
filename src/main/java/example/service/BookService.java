package example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import example.domain.Book;
import example.repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book findById(String id) {
        return bookRepository.findById(id);
    }

    public void save(Book book) {
        bookRepository.save(book);
    }

    public void delete(Book book) {
        bookRepository.delete(book);
    }

    public void deleteAll() {
        bookRepository.deleteAll();
    }
}