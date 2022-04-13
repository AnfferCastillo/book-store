package com.anffercastillo.models;

import com.anffercastillo.dto.BookRequest;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "books")
public class Book {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;
  private String description;
  private String coverImage;
  private double price;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "author")
  private User author;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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

  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public static Book buildBook(BookRequest bookRequest, User author, Long bookId) {
    var entity = new Book();
    entity.setAuthor(author);
    entity.setDescription(bookRequest.getDescription());
    entity.setPrice(bookRequest.getPrice());
    entity.setCoverImage(bookRequest.getCoverImage());
    entity.setTitle(bookRequest.getTitle());
    entity.setId(bookId);
    return entity;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Book book = (Book) o;
    return Objects.equals(title, book.title) && Objects.equals(author, book.author);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, author);
  }
}
