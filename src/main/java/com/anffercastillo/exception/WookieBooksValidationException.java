package com.anffercastillo.exception;

public class WookieBooksValidationException extends Exception {
  public WookieBooksValidationException() {
    super();
  }

  public WookieBooksValidationException(String message) {
    super(message);
  }

  public WookieBooksValidationException(String message, Throwable cause) {
    super(message, cause);
  }

  public WookieBooksValidationException(Throwable cause) {
    super(cause);
  }

  protected WookieBooksValidationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
