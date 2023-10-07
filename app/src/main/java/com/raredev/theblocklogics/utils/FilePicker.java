package com.raredev.theblocklogics.utils;

import android.content.Intent;
import android.net.Uri;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.raredev.theblocklogics.callback.PushCallback;
import java.util.HashMap;
import java.util.Map;

public class FilePicker {

  private Map<Integer, PushCallback<Uri>> callbacks = new HashMap<>();
  private ActivityResultLauncher<Intent> picker;

  private int CURRENT_RESULT_CODE = -1;

  public FilePicker(AppCompatActivity activity) {
    picker =
        activity.registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == AppCompatActivity.RESULT_OK) {
                callbacks.get(CURRENT_RESULT_CODE).onComplete(result.getData().getData());
              }
            });
  }

  public void destroy() {
    picker.unregister();
    callbacks.clear();
  }

  public void addCallback(int resultCode, PushCallback<Uri> callback) {
    callbacks.put(resultCode, callback);
  }

  public void removeCallback(int resultCode) {
    callbacks.remove(resultCode);
  }

  public void launch(int resultCode, Intent intent) {
    CURRENT_RESULT_CODE = resultCode;
    picker.launch(intent);
  }
}