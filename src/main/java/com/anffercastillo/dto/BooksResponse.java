package com.anffercastillo.dto;

import java.util.List;

public class BooksResponse {

  private List<BookResponse> books;

  public List<BookResponse> getBooks() {
    return books;
  }

  public void setBooks(List<BookResponse> books) {
    this.books = books;
  }

  public static BooksResponse buildEmptyResponse() {
    return new BooksResponse();
  }
}
