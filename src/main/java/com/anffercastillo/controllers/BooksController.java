package com.anffercastillo.controllers;

import com.anffercastillo.dto.BookResponse;
import com.anffercastillo.dto.BooksResponse;
import com.anffercastillo.services.BooksService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BooksController {

  private BooksService booksService;

  public BooksController(BooksService booksService) {
    this.booksService = booksService;
  }

  @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public BooksResponse getBooks(@RequestParam(required = false, defaultValue = "") String term) {
    return booksService.getBooks(term);
  }

  @GetMapping(
      value = "/{id}",
      produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  public BookResponse getBook(@PathVariable long id) {
    return booksService.getBook(id);
  }
}
