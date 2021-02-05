package org.example.delivery.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * NoDataFoundException class.
 *
 * @author hugo
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoDataFoundException extends RuntimeException {

  private static final long serialVersionUID = 8304312798939557342L;

  public NoDataFoundException() {
    super();
  }
}
