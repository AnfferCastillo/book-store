package com.anffercastillo.repositories;

import com.anffercastillo.models.Book;

import java.util.List;

public interface CustomBooksRepository {
  public List<Book> search(String term);
}
