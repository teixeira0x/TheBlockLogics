package com.raredev.theblocklogics.utils;

import android.util.Log;
import java.util.Map;
import java.util.WeakHashMap;

public class Logger {

  private static final Map<String, Logger> map = new WeakHashMap<>();

  public static synchronized Logger newInstance(String tag) {
    var logger = map.get(tag);
    if (logger == null) {
      logger = new Logger(tag);
      map.put(tag, logger);
    }
    return logger;
  }

  private final String tag;

  private Logger(String tag) {
    this.tag = tag;
  }

  public void d(String message) {
    Log.d(tag, message);
  }

  public void w(String message) {
    Log.w(tag, message);
  }

  public void e(String message, Throwable e) {
    Log.e(tag, message, e);
  }

  public void e(Throwable e) {
    Log.e(tag, tag, e);
  }

  public void e(String message) {
    Log.e(tag, message);
  }

  public void i(String message) {
    Log.i(tag, message);
  }

  public void v(String message) {
    Log.v(tag, message);
  }
}
