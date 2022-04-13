package com.anffercastillo.controllers;

import com.anffercastillo.dto.BookResponse;
import com.anffercastillo.dto.BooksResponse;
import com.anffercastillo.services.BooksService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BooksControllerTest {

  private BooksController booksController;

  private BooksService mockBookService;

  @BeforeEach
  void setup() {
    mockBookService = mock(BooksService.class);
    booksController = new BooksController(mockBookService);
  }

  @Test
  void getAllBooks_Success_Test() {
    var expected = buildMockedResponse();
    when(mockBookService.getBooks("")).thenReturn(expected);

    var actual = booksController.getBooks("");
    assertThat(actual.getBooks()).hasSameElementsAs(expected.getBooks());
    verify(mockBookService, times(1)).getBooks("");
  }

  @Test
  void getBookById_Success_Test() {
    var expected = new BookResponse();
    expected.setId(1L);
    when(mockBookService.getBook(1L)).thenReturn(expected);

    var actual = booksController.getBook(1L);

    assertEquals(expected, actual);
  }

  @Test
  void getBook_NonExsitingBook_ExceptionThrown() {
    when(mockBookService.getBook(1L)).thenThrow(new NoSuchElementException());

    assertThrows(NoSuchElementException.class, () -> booksController.getBook(1L));
  }

  private BooksResponse buildMockedResponse() {
    var booksResponse = new BooksResponse();
    var books = new ArrayList<BookResponse>();
    books.add(new BookResponse());
    books.add(new BookResponse());
    books.add(new BookResponse());

    booksResponse.setBooks(books);
    return booksResponse;
  }
}
