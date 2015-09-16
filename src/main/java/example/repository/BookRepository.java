package example.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import example.domain.Book;

@Repository
public class BookRepository {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void save(Book book) {
        redisTemplate.opsForValue().set(book.bookId, new Gson().toJson(book));
    }

    public Book findById(String key) {
        return new Gson().fromJson(redisTemplate.opsForValue().get(key), Book.class);
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        Set<String> keys = redisTemplate.keys("*");
        Iterator<String> it = keys.iterator();

        while (it.hasNext()) {
            books.add(findById(it.next()));
        }

        return books;
    }

    public void delete(Book b) {
        String key = b.bookId;
        redisTemplate.opsForValue().getOperations().delete(key);
    }

    public void deleteAll() {
        Set<String> keys = redisTemplate.keys("*");
        Iterator<String> it = keys.iterator();

        while (it.hasNext()) {
            Book b = new Book(it.next());
            delete(b);
        }
    }
}