package com.raredev.theblocklogics.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.raredev.theblocklogics.app.TheBlockLogicsApp;
import java.io.IOException;
import java.io.InputStream;

public class UriUtils {

  public static Drawable convertUriToDrawable(Uri uri) {
    InputStream is = null;
    try {
      ContentResolver contentResolver = TheBlockLogicsApp.getInstance().getContentResolver();
      is = contentResolver.openInputStream(uri);

      if (is != null) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return new BitmapDrawable(TheBlockLogicsApp.getInstance().getResources(), bitmap);
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (is != null) is.close();
      } catch (IOException e) {
      }
    }

    return null;
  }
}
