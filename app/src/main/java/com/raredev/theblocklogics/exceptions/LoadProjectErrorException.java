package com.raredev.theblocklogics.exceptions;

/*
 * Exception class for errors when loading a project
 */
public class LoadProjectErrorException extends Exception {

  public LoadProjectErrorException(String message) {
    super(message);
  }

  public LoadProjectErrorException(String message, Throwable e) {
    super(message, e);
  }
}
