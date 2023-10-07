package com.raredev.theblocklogics.utils.validate;

import java.io.File;

public class FileValidator {

  public static boolean isValidDir(File file) {
    if (!file.exists()) {
      return false;
    }

    if (!file.isDirectory()) {
      return false;
    }
    return true;
  }

  public static boolean isValidFile(File file) {
    if (!file.exists()) {
      return false;
    }

    if (!file.isFile()) {
      return false;
    }
    return true;
  }
}
