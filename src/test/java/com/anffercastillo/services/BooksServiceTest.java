package com.anffercastillo.services;

import com.anffercastillo.dto.BookRequest;
import com.anffercastillo.dto.BookResponse;
import com.anffercastillo.dto.UserDTO;
import com.anffercastillo.exception.WookieBooksNotFoundException;
import com.anffercastillo.exception.WookieBooksValidationException;
import com.anffercastillo.models.Book;
import com.anffercastillo.repositories.BooksRepository;
import com.anffercastillo.repositories.UserRepository;
import com.anffercastillo.utils.WookieBooksTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BooksServiceTest {
  static final long AUTHOR_ID = 1L;
  private static final String AUTHOR_NAME = "AUTHOR_NAME";
  static final String AUTHOR_ONE_NAME = "AUTHOR_ONE_NAME";
  static final String AUTHOR_TWO_NAME = "AUTHOR_TWO_NAME";
  static final long ID = 1L;
  static final long ID_2 = 2L;

  private BooksService booksService;
  private BooksRepository mockBooksRepository;
  private UserRepository mockUserRepository;

  @BeforeEach
  void setup() {
    mockBooksRepository = mock(BooksRepository.class);
    mockUserRepository = mock(UserRepository.class);
    booksService = new BooksService(mockBooksRepository, mockUserRepository);
  }

  @ParameterizedTest
  @MethodSource("provideTerms")
  void getBooks_GetAllBooks_Succes(String term, List<BookResponse> expected, List<Book> books) {
    when(mockBooksRepository.findAll()).thenReturn(books);
    when(mockBooksRepository.search(term)).thenReturn(books);

    var actual = booksService.getBooks(term);
    assertThat(expected).hasSameSizeAs(actual.getBooks());
  }

  @Test
  void getBook_GetExistingBookId_Success() {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookEntity = WookieBooksTestUtils.buildBookEntity(ID, author);

    when(mockBooksRepository.findById(ID)).thenReturn(Optional.of(bookEntity));

    var expected =
        WookieBooksTestUtils.buildBookResponseWithAuthor(ID, UserDTO.buildUserDTO(author));
    var actual = booksService.getBook(ID);

    assertEquals(expected, actual);
    verify(mockBooksRepository, times(1)).findById(ID);
  }

  @Test
  void getBook_GetNonExistingBookId_ExceptionThrown() {
    when(mockBooksRepository.findById(ID)).thenReturn(Optional.empty());

    // TODO: Change to proper exception
    assertThrows(Exception.class, () -> booksService.getBook(ID));
    verify(mockBooksRepository, times(1)).findById(ID);
  }

  @Test
  void getBooksByAuthor_ByExistingAuthor_Succes() throws Exception {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var authorDTO = WookieBooksTestUtils.buildUserDTO(AUTHOR_ID, AUTHOR_NAME);
    var bookEntity = WookieBooksTestUtils.buildBookEntity(ID, author);

    when(mockBooksRepository.findByAuthor(author)).thenReturn(List.of(bookEntity));
    when(mockUserRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
    var expected = WookieBooksTestUtils.buildBooksResponseWithAuthor(AUTHOR_ID, authorDTO);
    var actual = booksService.getBooksByAuthor(AUTHOR_ID);

    assertEquals(expected.getBooks(), actual.getBooks());
    verify(mockBooksRepository, times(1)).findByAuthor(author);
  }

  @Test
  void getBooksByAuthor_NonExistingAuthors_ExceptionThrown() {
    when(mockBooksRepository.findById(any())).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> booksService.getBooksByAuthor(AUTHOR_ID));
    verify(mockBooksRepository, times(0)).findByAuthor(any());
    verify(mockUserRepository, times(1)).findById(any());
  }

  @Test
  void createBook_CreateBook_Succes() throws Exception {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();

    var bookToSave = WookieBooksTestUtils.buildBookEntity(null, author);
    var savedBook = WookieBooksTestUtils.buildBookEntity(ID, author);

    when(mockBooksRepository.save(bookToSave)).thenReturn(savedBook);
    when(mockUserRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));

    var expected =
        WookieBooksTestUtils.buildBookResponseWithAuthor(ID, UserDTO.buildUserDTO(author));

    var actual = booksService.createBook(AUTHOR_ID, bookRequest);

    assertEquals(expected, actual);
    verify(mockBooksRepository, times(1)).save(bookToSave);
    verify(mockUserRepository, times(1)).findById(AUTHOR_ID);
  }

  @Test
  void createBook_BannedAuthor_ExceptionThrown() {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, true);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    when(mockUserRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));

    assertThrows(
        WookieBooksValidationException.class,
        () -> booksService.createBook(AUTHOR_ID, bookRequest));

    verify(mockBooksRepository, times(0)).findByTitleAndAuthor(bookRequest.getTitle(), author);
    verify(mockUserRepository, times(1)).findById(AUTHOR_ID);
  }

  @Test
  void createBook_BookWithSameTitle_ExceptionThrown() {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookRequest = WookieBooksTestUtils.buildBookRequest();

    var savedBook = WookieBooksTestUtils.buildBookEntity(ID, author);

    when(mockUserRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));
    when(mockBooksRepository.findByTitleAndAuthor(bookRequest.getTitle(), author))
        .thenReturn(Optional.of(savedBook));

    assertThrows(
        WookieBooksValidationException.class,
        () -> booksService.createBook(AUTHOR_ID, bookRequest));
    verify(mockBooksRepository, times(1)).findByTitleAndAuthor(bookRequest.getTitle(), author);
    verify(mockUserRepository, times(1)).findById(AUTHOR_ID);
  }

  @Test
  void createBook_ForNonExistingAuthor_ExceptionThrown() {
    var bookRequest = WookieBooksTestUtils.buildBookRequest();

    when(mockUserRepository.findById(AUTHOR_ID)).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> booksService.createBook(AUTHOR_ID, bookRequest));
    verify(mockBooksRepository, times(0)).findByTitleAndAuthor(any(), any());
    verify(mockUserRepository, times(1)).findById(AUTHOR_ID);
  }

  @Test
  void deleteBook_BookByAuthor_Success() throws Exception {
    var book = WookieBooksTestUtils.buildBookEntity(ID, null);
    when(mockBooksRepository.findById(ID)).thenReturn(Optional.of(book));
    booksService.deleteBook(ID, AUTHOR_ID);
    verify(mockBooksRepository, times(1)).deleteById(ID);
  }

  @Test
  void deleteBook_NonExistingBook_ExceptionThrown() {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookEntity = WookieBooksTestUtils.buildBookEntity(ID, author);
    when(mockBooksRepository.findById(ID)).thenReturn(Optional.empty());

    assertThrows(Exception.class, () -> booksService.deleteBook(ID, AUTHOR_ID));
    verify(mockBooksRepository, times(1)).findById(ID);
    verify(mockBooksRepository, times(0)).deleteById(ID);
  }

  @Test
  void deleteBook_AnotherAuthorsBook_ExceptionThrown() {
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookEntity = WookieBooksTestUtils.buildBookEntity(ID, author);
    when(mockBooksRepository.findById(ID)).thenReturn(Optional.of(bookEntity));

    assertThrows(Exception.class, () -> booksService.deleteBook(ID, 25L));
    verify(mockBooksRepository, times(1)).findById(ID);
    verify(mockBooksRepository, times(0)).deleteById(ID);
  }

  @Test
  void updateBook_AuthorsBook_Success() throws Exception {
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    var authorDTO = WookieBooksTestUtils.buildUserDTO(AUTHOR_ID, AUTHOR_NAME);
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookEntity = WookieBooksTestUtils.buildBookEntity(ID, author);

    var expected = WookieBooksTestUtils.buildBookResponseWithAuthor(ID, authorDTO);
    when(mockBooksRepository.findById(ID)).thenReturn(Optional.of(bookEntity));
    when(mockBooksRepository.save(bookEntity)).thenReturn(bookEntity);
    when(mockUserRepository.findById(AUTHOR_ID)).thenReturn(Optional.of(author));

    var actual = booksService.updateBook(ID, AUTHOR_ID, bookRequest);

    assertEquals(expected, actual);
    verify(mockBooksRepository, times(1)).save(bookEntity);
  }

  @Test
  void updateBook_NonExistingBook_ExceptionThrown() {
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    when(mockBooksRepository.findById(ID)).thenReturn(Optional.empty());
    assertThrows(
        WookieBooksNotFoundException.class,
        () -> booksService.updateBook(ID, AUTHOR_ID, bookRequest));
  }

  @Test
  void updateBook_AnotherAuthorsBook_ExceptionThrown() {
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    var author = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_NAME, false);
    var bookEntity = WookieBooksTestUtils.buildBookEntity(ID, author);

    when(mockBooksRepository.findById(ID)).thenReturn(Optional.of(bookEntity));

    assertThrows(Exception.class, () -> booksService.updateBook(ID, 25L, bookRequest));

    verify(mockBooksRepository, times(0)).save(any());
    verify(mockUserRepository, times(0)).findById(any());
  }

  // Parametrizar para tomar en cuenta todos los fields del request
  @ParameterizedTest
  @MethodSource("provideBooksRequests")
  void updateBook_BadBookRequest_ExceptionThrown(String badFiledName, BookRequest bookRequest) {

    assertThrows(
        WookieBooksValidationException.class,
        () -> booksService.updateBook(ID, AUTHOR_ID, bookRequest),
        "Test: " + badFiledName);
    verify(mockBooksRepository, times(0)).save(any());
    verify(mockBooksRepository, times(0)).findById(any());
    verify(mockUserRepository, times(0)).findById(any());
  }

  static Stream<Arguments> provideBooksRequests() {
    var bookRequest = WookieBooksTestUtils.buildBookRequest();
    bookRequest.setTitle(null);
    var bookRequest1 = WookieBooksTestUtils.buildBookRequest();
    bookRequest1.setDescription(null);
    var bookRequest2 = WookieBooksTestUtils.buildBookRequest();
    bookRequest2.setCoverImage(null);
    var bookRequest3 = WookieBooksTestUtils.buildBookRequest();
    bookRequest3.setPrice(null);

    return Stream.of(
        Arguments.of("No Title", bookRequest),
        Arguments.of("No Description", bookRequest1),
        Arguments.of("No Cover Image", bookRequest2),
        Arguments.of("No Price", bookRequest3));
  }

  static Stream<Arguments> provideTerms() {
    var author1 = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_ONE_NAME, false);
    var author2 = WookieBooksTestUtils.buildUser(AUTHOR_ID, AUTHOR_TWO_NAME, false);
    var bookOne =
        WookieBooksTestUtils.buildBookResponseWithAuthor(ID, UserDTO.buildUserDTO(author1));
    var bookTwo =
        WookieBooksTestUtils.buildBookResponseWithAuthor(ID_2, UserDTO.buildUserDTO(author2));

    var bookEntityOne = WookieBooksTestUtils.buildBookEntity(ID, author1);
    var bookEntityTwo = WookieBooksTestUtils.buildBookEntity(ID_2, author2);

    return Stream.of(
        Arguments.of("", List.of(bookOne, bookTwo), List.of(bookEntityOne, bookEntityTwo)),
        Arguments.of("TWO", List.of(bookTwo), List.of(bookEntityTwo)),
        Arguments.of("TEST", List.of(), List.of()));
  }
}
