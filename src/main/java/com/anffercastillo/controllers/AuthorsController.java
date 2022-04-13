package com.anffercastillo.controllers;

import com.anffercastillo.dto.BookRequest;
import com.anffercastillo.dto.BookResponse;
import com.anffercastillo.dto.BooksResponse;
import com.anffercastillo.exception.WookieBooksNotFoundException;
import com.anffercastillo.exception.WookieBooksValidationException;
import com.anffercastillo.models.CustomUserDetails;
import com.anffercastillo.services.BooksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.security.Principal;

@RestController
@RequestMapping("/authors")
public class AuthorsController {

  private BooksService bookService;

  public AuthorsController(BooksService bookService) {
    this.bookService = bookService;
  }

  @GetMapping(
      value = "/{authorId}/books",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public BooksResponse getBooks(@PathVariable long authorId, Principal principal)
      throws WookieBooksNotFoundException {
    validateUser(authorId, principal);

    return bookService.getBooksByAuthor(authorId);
  }

  @PostMapping(
      value = "/{authorId}/books",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public BookResponse publishBook(
      @PathVariable long authorId, @RequestBody BookRequest book, Principal principal)
      throws WookieBooksNotFoundException, WookieBooksValidationException {
    validateUser(authorId, principal);
    return bookService.createBook(authorId, book);
  }

  @DeleteMapping(value = "/{authorId}/books/{bookId}")
  public void unpublishBook(
      @PathVariable long authorId, @PathVariable long bookId, Principal principal)
      throws WookieBooksNotFoundException, WookieBooksValidationException {
    validateUser(authorId, principal);

    bookService.deleteBook(bookId, authorId);
  }

  @PutMapping(
      value = "/{authorId}/books/{bookId}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public BookResponse updateBook(
      @PathVariable long authorId,
      @PathVariable long bookId,
      @RequestBody BookRequest bookRequest,
      Principal principal)
      throws WookieBooksNotFoundException, WookieBooksValidationException {

    validateUser(authorId, principal);

    return bookService.updateBook(bookId, authorId, bookRequest);
  }

  private void validateUser(long authorId, Principal principal) throws HttpServerErrorException {
    var userDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
    if (userDetails != null && userDetails.getId() != authorId) {
      throw new HttpServerErrorException(HttpStatus.FORBIDDEN);
    }
  }
}
