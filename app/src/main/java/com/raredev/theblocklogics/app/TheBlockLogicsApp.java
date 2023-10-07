package com.raredev.theblocklogics.app;

import android.app.Application;
import android.content.Intent;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.android.material.color.DynamicColors;
import com.raredev.theblocklogics.activities.CrashActivity;
import com.raredev.theblocklogics.utils.Constants;

public class TheBlockLogicsApp extends Application implements Thread.UncaughtExceptionHandler {

  private static TheBlockLogicsApp instance;

  private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

  @Override
  public void onCreate() {
    instance = this;
    uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this);
    super.onCreate();

    DynamicColors.applyToActivitiesIfAvailable(this);
  }

  @Override
  public void uncaughtException(Thread thread, Throwable th) {
    try {
      var intent = new Intent(this, CrashActivity.class);

      // Add the error message
      intent.putExtra(Constants.KEY_EXTRA_ERROR, ThrowableUtils.getFullStackTrace(th));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      // Start the crash activity
      startActivity(intent);
      if (uncaughtExceptionHandler != null) {
        uncaughtExceptionHandler.uncaughtException(thread, th);
      }
      System.exit(1);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }

  public static TheBlockLogicsApp getInstance() {
    return instance;
  }
}
