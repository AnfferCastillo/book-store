package com.anffercastillo.repositories;

import com.anffercastillo.models.Book;
import com.anffercastillo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long>, CustomBooksRepository {
  public List<Book> findByAuthor(User author);

  public Optional<Book> findByTitleAndAuthor(String title, User authorId);
}
