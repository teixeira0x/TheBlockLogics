package com.raredev.theblocklogics.utils;

import android.content.Intent;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.raredev.theblocklogics.callback.PushCallback;

public class FilePicker {

  private final ActivityResultLauncher<Intent> picker;

  private PushCallback<Uri> callback;

  public FilePicker(AppCompatActivity activity) {
    picker =
        activity.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                if (callback != null) {
                  callback.onComplete(result.getData().getData());

                  callback = null;
                }
              }
            });
  }

  public void destroy() {
    picker.unregister();
  }

  public void launch(Intent intent, PushCallback<Uri> callback) {
    this.callback = callback;
    picker.launch(intent);
  }
}
