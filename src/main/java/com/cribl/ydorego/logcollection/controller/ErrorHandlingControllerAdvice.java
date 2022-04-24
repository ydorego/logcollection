package com.cribl.ydorego.logcollection.controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.cribl.ydorego.logcollection.exceptions.LogCollectorDefaultException;
import com.cribl.ydorego.logcollection.validation.ValidationErrorResponse;
import com.cribl.ydorego.logcollection.validation.Violation;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 
 * Controller advice is a spring boot general exception handler to faciliate error handling.
 * 
 * We are handling the following validation errors:
 *  - Constraints violations (For example not null or range not respected)
 *  - Missing required parameters
 *  - Invalid expected type.
 *  - Invalid arguments
 */
@ControllerAdvice
class ErrorHandlingControllerAdvice {

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ValidationErrorResponse onConstraintValidationException(
      ConstraintViolationException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    for (ConstraintViolation violation : e.getConstraintViolations()) {
      error.getViolations().add(
        new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
    }
    return error;
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ValidationErrorResponse onMissingParameterException(MissingServletRequestParameterException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    error.getViolations().add(new Violation(e.getParameterName(), e.getMessage()));
    return error;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ValidationErrorResponse onMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    error.getViolations().add(new Violation(e.getName(), e.getMessage()));
    return error;
  }

  @ExceptionHandler(LogCollectorDefaultException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ValidationErrorResponse onLogCollectorDefaultException(LogCollectorDefaultException e) {
    ValidationErrorResponse error = new ValidationErrorResponse();
    error.getViolations().add(new Violation(e.getFieldName(), e.getMessage()));
    return error;
  }

  
}