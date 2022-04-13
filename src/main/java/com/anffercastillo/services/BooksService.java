package com.anffercastillo.services;

import com.anffercastillo.dto.BookRequest;
import com.anffercastillo.dto.BookResponse;
import com.anffercastillo.dto.BooksResponse;
import com.anffercastillo.exception.WookieBooksNotFoundException;
import com.anffercastillo.exception.WookieBooksValidationException;
import com.anffercastillo.models.Book;
import com.anffercastillo.models.User;
import com.anffercastillo.repositories.BooksRepository;
import com.anffercastillo.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BooksService {

  private BooksRepository booksRepository;
  private UserRepository userRepository;

  public BooksService(BooksRepository booksRepository, UserRepository userRepository) {
    this.booksRepository = booksRepository;
    this.userRepository = userRepository;
  }

  public BooksResponse getBooks(String term) {
    var booksResponse = BooksResponse.buildEmptyResponse();
    List<BookResponse> books = null;
    if (StringUtils.hasLength(term)) {
      books = buildListOfBooksResponse(booksRepository.search(term));
      booksResponse.setBooks(books);
    } else {
      books = buildListOfBooksResponse(booksRepository.findAll());
      booksResponse.setBooks(books);
    }
    return booksResponse;
  }

  public BookResponse getBook(long id) {
    return booksRepository.findById(id).map(BookResponse::buildBookResponse).orElseThrow();
  }

  public BooksResponse getBooksByAuthor(long authorId) throws WookieBooksNotFoundException {
    var booksResponse = BooksResponse.buildEmptyResponse();
    var author = getAuthor(authorId);
    var books = buildListOfBooksResponse(booksRepository.findByAuthor(author));

    if (books.isEmpty()) {
      throw new WookieBooksNotFoundException("Books not found");
    }
    booksResponse.setBooks(books);
    return booksResponse;
  }

  public BookResponse createBook(long authorId, BookRequest bookRequest)
      throws WookieBooksValidationException, WookieBooksNotFoundException {
    var author = getAuthor(authorId);
    if (author.isBanned()) {
      throw new WookieBooksValidationException("You are not allow to publish your work");
    }
    validateIsNewBook(bookRequest.getTitle(), author);

    var book = Book.buildBook(bookRequest, author, null);

    var savedBook = booksRepository.save(book);
    return BookResponse.buildBookResponse(savedBook);
  }

  public void deleteBook(long bookId, long authorId)
      throws WookieBooksValidationException, WookieBooksNotFoundException {
    var book =
        booksRepository
            .findById(bookId)
            .orElseThrow(() -> new WookieBooksNotFoundException("Book doesn't exist"));

    validateAuthor(authorId, book);

    booksRepository.deleteById(bookId);
  }

  public BookResponse updateBook(long bookId, long authorId, BookRequest bookRequest)
      throws WookieBooksValidationException, WookieBooksNotFoundException {

    validateRequest(bookRequest);

    var currentBook =
        booksRepository
            .findById(bookId)
            .orElseThrow(() -> new WookieBooksNotFoundException("Book does not exists"));
    validateAuthor(authorId, currentBook);

    currentBook.setTitle(bookRequest.getTitle());
    currentBook.setPrice(bookRequest.getPrice());
    currentBook.setCoverImage(bookRequest.getCoverImage());
    currentBook.setDescription(bookRequest.getDescription());

    var savedBook = booksRepository.save(currentBook);
    return BookResponse.buildBookResponse(savedBook);
  }

  private void validateRequest(BookRequest request) throws WookieBooksValidationException {
    var valid =
        StringUtils.hasLength(request.getTitle())
            && StringUtils.hasLength(request.getDescription())
            && StringUtils.hasLength(request.getCoverImage())
            && request.getPrice() != null;

    if (!valid) throw new WookieBooksValidationException("Some fields are null");
  }

  private void validateIsNewBook(String title, User author) throws WookieBooksValidationException {
    var book = booksRepository.findByTitleAndAuthor(title, author);
    if (book.isPresent()) {
      throw new WookieBooksValidationException("Book already exists");
    }
  }

  private User getAuthor(long authorId) throws WookieBooksNotFoundException {
    return userRepository
        .findById(authorId)
        .orElseThrow(() -> new WookieBooksNotFoundException("Author does not exists"));
  }

  private void validateAuthor(long authorId, Book book) throws WookieBooksValidationException {
    if (book.getAuthor() != null && book.getAuthor().getId() != authorId) {
      throw new WookieBooksValidationException("Forbbiden");
    }
  }

  private List<BookResponse> buildListOfBooksResponse(List<Book> books) {
    return books.stream().map(BookResponse::buildBookResponse).collect(Collectors.toList());
  }
}
