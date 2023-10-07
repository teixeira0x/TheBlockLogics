package com.raredev.theblocklogics.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtil {
  
  public static String readFile(String path) {
    return readFile(new File(path));
  }

  public static String readFile(File file) {
    StringBuilder sb = new StringBuilder();

    if (!file.exists()) {
      return sb.toString();
    }

    FileReader fr = null;
    try {
      fr = new FileReader(file);

      char[] buff = new char[1024];
      int length = 0;

      // Read the contents of the file and append them to the StringBuilder
      while ((length = fr.read(buff)) > 0) {
        sb.append(new String(buff, 0, length));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (fr != null) {
        try {
          fr.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    // Return the contents of the file
    return sb.toString();
  }

  public static void writeFile(String path, String str) {
    createNewFile(path);

    FileWriter fileWriter = null;
    try {
      // Create a filewriter object with given path
      // and false to overwrite the existing file.
      fileWriter = new FileWriter(new File(path), false);
      // Write the given string in file.
      fileWriter.write(str);
      // Flush the filewriter object.
      fileWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        // Close the filewriter object.
        if (fileWriter != null) fileWriter.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void writeBitmapDrawableImage(String path, Drawable drawable) {
    if (drawable instanceof BitmapDrawable) {
      Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, fos);
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (fos != null) fos.close();
        } catch (IOException e) {
        }
      }
    }
  }

  private static void createNewFile(String path) {
    // Get the last index of the file separator
    int lastSep = path.lastIndexOf(File.separator);
    // If there is a path, call makeDir to create the directory
    if (lastSep > 0) {
      String dirPath = path.substring(0, lastSep);
      makeDir(dirPath);
    }

    // Create a new file in the specified path
    File file = new File(path);

    try {
      // Only create the file if it does not already exist
      if (!file.exists()) file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void makeDir(String path) {
    File file = new File(path);
    if (!file.exists()) {
      file.mkdirs();
    }
  }

  public static String getLastSegmentFromPath(String path) {
    if (path == null) return "";
    return path.substring(path.lastIndexOf(File.separator) + 1, path.length());
  }
}
