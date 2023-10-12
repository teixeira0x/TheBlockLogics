package com.raredev.theblocklogics.exceptions;

/*
 * Exception class for errors when loading a project
 */
public class LoadProjectErrorException extends Exception {

  public LoadProjectErrorException(String error) {
    super(error);
  }
}
