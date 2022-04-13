package com.anffercastillo.utils;

import com.anffercastillo.dto.BookRequest;
import com.anffercastillo.dto.BookResponse;
import com.anffercastillo.dto.BooksResponse;
import com.anffercastillo.dto.UserDTO;
import com.anffercastillo.models.Book;
import com.anffercastillo.models.CustomUserDetails;
import com.anffercastillo.models.User;

import java.util.ArrayList;

public class WookieBooksTestUtils {

  public static final String BOOK_TITLE = "BOOK_TITLE";
  public static final String BOOK_DESCRIPTION = "BOOK_DESCRIPTION";
  public static final String COVER_IMAGE = "COVER_IMAGE";
  public static final double PRICE = 1.00;

  public static CustomUserDetails buildCustomUserDetails(long id, String username) {
    var ud = new CustomUserDetails();
    ud.setId(id);
    ud.setUsername(username);
    return ud;
  }

  public static UserDTO buildUserDTO(long id, String author) {
    var dto = new UserDTO();
    dto.setId(id);
    dto.setName(author);
    return dto;
  }

  public static User buildUser(Long id, String name, boolean isBanned) {
    var entity = new User();
    entity.setId(id);
    entity.setBanned(isBanned);
    entity.setPseudonym(name + "_PSEUDONYM");
    entity.setLastName("LAST_" + name);
    return entity;
  }

  public static BookResponse buildBookResponseWithAuthor(long id, UserDTO author) {
    var dto = new BookResponse();
    dto.setAuthor(author);
    dto.setTitle(BOOK_TITLE);
    dto.setId(id);
    dto.setDescription(BOOK_DESCRIPTION);
    dto.setPrice(PRICE);
    return dto;
  }

  public static Book buildBookEntity(Long id, User author) {
    var book = new Book();
    book.setId(id);
    book.setTitle(BOOK_TITLE);
    book.setCoverImage(COVER_IMAGE);
    book.setPrice(PRICE);
    book.setDescription(BOOK_DESCRIPTION);
    book.setAuthor(author);
    return book;
  }

  public static BooksResponse buildBooksResponseWithAuthor(long authorId, UserDTO author) {
    var booksResponse = new BooksResponse();
    var books = new ArrayList<BookResponse>();
    books.add(buildBookResponseWithAuthor(authorId, author));
    booksResponse.setBooks(books);
    return booksResponse;
  }

  public static BookRequest buildBookRequest() {
    var bookRequest = new BookRequest();
    bookRequest.setTitle(BOOK_TITLE);
    bookRequest.setDescription(BOOK_DESCRIPTION);
    bookRequest.setCoverImage(COVER_IMAGE);
    bookRequest.setPrice(PRICE);
    return bookRequest;
  }
}
