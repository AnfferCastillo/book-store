package com.anffercastillo.dto;

import com.anffercastillo.models.Book;

import java.util.Objects;

public class BookResponse {

  private long id;

  private String title;

  private String description;

  private String coverImage;

  private double price;

  private UserDTO author;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getCoverImage() {
    return coverImage;
  }

  public void setCoverImage(String coverImage) {
    this.coverImage = coverImage;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public UserDTO getAuthor() {
    return author;
  }

  public void setAuthor(UserDTO author) {
    this.author = author;
  }

  public static BookResponse buildBookResponse(Book book) {
    var response = new BookResponse();
    response.setId(book.getId());
    response.setPrice(book.getPrice());
    response.setTitle(book.getTitle());
    response.setDescription(book.getDescription());
    response.setCoverImage(book.getCoverImage());
    response.setAuthor(UserDTO.buildUserDTO(book.getAuthor()));
    return response;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BookResponse that = (BookResponse) o;
    return Objects.equals(title, that.title) && Objects.equals(author, that.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, author);
  }
}
