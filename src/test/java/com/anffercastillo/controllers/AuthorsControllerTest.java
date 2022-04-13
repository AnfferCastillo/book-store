package com.anffercastillo.controllers;

import com.anffercastillo.services.BooksService;
import com.anffercastillo.utils.WookieBooksTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpStatusCodeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthorsControllerTest {

  public static final String BOOK_TITLE = "BOOK_TITLE";
  public static final String AUTHOR_NAME = "AUTHOR_NAME";
  public static final long AUTHOR_ID = 1L;
  public static final long BOOK_ID = 1L;

  private AuthorsController authorsController;

  private BooksService mockBooksService;

  @BeforeEach
  public void setup() {
    mockBooksService = mock(BooksService.class);
    authorsController = new AuthorsController(mockBooksService);
  }

  @Test
  void getBooks_SameAuthorBooks_Success() throws Exception {
    var id = AUTHOR_ID;
    var author = "DUMMY AUTHOR NAME";

    var authorDTO = WookieBooksTestUtils.buildUserDTO(AUTHOR_ID, author);
    var expected = WookieBooksTestUtils.buildBooksResponseWithAuthor(id, authorDTO);
    when(mockBooksService.getBooksByAuthor(AUTHOR_ID)).thenReturn(expected);

    var actual =
        authorsController.getBooks(
            id, WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, author));

    assertEquals(expected, actual);
    verify(mockBooksService, times(1)).getBooksByAuthor(AUTHOR_ID);
  }

  @Test
  void getBooks_OtherAuthorsBook_ExceptionThrown() throws Exception {
    assertThrows(
        Exception.class,
        () ->
            authorsController.getBooks(
                AUTHOR_ID, WookieBooksTestUtils.buildCustomUserDetails(2L, "DIFFERENT_ATUHOR")));

    verify(mockBooksService, times(0)).getBooksByAuthor(2L);
  }

  @Test
  void publishBook_PublishBook_Success() throws Exception {
    var author = WookieBooksTestUtils.buildUserDTO(AUTHOR_ID, AUTHOR_NAME);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();

    var expected = WookieBooksTestUtils.buildBookResponseWithAuthor(BOOK_ID, author);
    when(mockBooksService.createBook(AUTHOR_ID, bookRequest)).thenReturn(expected);

    var actual =
        authorsController.publishBook(
            AUTHOR_ID,
            bookRequest,
            WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, AUTHOR_NAME));

    assertEquals(author, actual.getAuthor());
    assertEquals(BOOK_ID, actual.getId());
    assertEquals(BOOK_TITLE, actual.getTitle());

    verify(mockBooksService, times(1)).createBook(AUTHOR_ID, bookRequest);
  }

  @Test
  void publishBook_PublishOtherAuthorBook_ExceptionThrown() throws Exception {
    var userDetails = WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, AUTHOR_NAME);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    assertThrows(
        Exception.class, () -> authorsController.publishBook(2L, bookRequest, userDetails));
  }

  @Test
  void unpublishBook_DeleteBook_Success() throws Exception {
    authorsController.unpublishBook(
        AUTHOR_ID, BOOK_ID, WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, AUTHOR_NAME));
    verify(mockBooksService, times(1)).deleteBook(BOOK_ID, AUTHOR_ID);
  }

  @Test
  void unpublishBook_DeleteOtherAuthorBook_ExceptionThrown() throws Exception {
    var userDetails = WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, AUTHOR_NAME);
    assertThrows(
        HttpStatusCodeException.class,
        () -> authorsController.unpublishBook(2L, BOOK_ID, userDetails));
  }

  @Test
  void updateBook_UpdateAuthorBook_Success() throws Exception {
    var author = WookieBooksTestUtils.buildUserDTO(AUTHOR_ID, AUTHOR_NAME);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    var expected = WookieBooksTestUtils.buildBookResponseWithAuthor(BOOK_ID, author);
    when(mockBooksService.updateBook(BOOK_ID, AUTHOR_ID, bookRequest)).thenReturn(expected);

    var actual =
        authorsController.updateBook(
            AUTHOR_ID,
            BOOK_ID,
            bookRequest,
            WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, AUTHOR_NAME));

    assertEquals(expected, actual);
    verify(mockBooksService, times(1)).updateBook(BOOK_ID, AUTHOR_ID, bookRequest);
  }

  @Test
  void updateBook_UpdateOtherAuthorBook_ExceptionThrown() {
    var userDetails = WookieBooksTestUtils.buildCustomUserDetails(AUTHOR_ID, AUTHOR_NAME);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    assertThrows(
        HttpStatusCodeException.class,
        () -> authorsController.updateBook(2L, BOOK_ID, bookRequest, userDetails));
  }
}
