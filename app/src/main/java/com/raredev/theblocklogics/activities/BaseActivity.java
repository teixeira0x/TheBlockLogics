package com.raredev.theblocklogics.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.elevation.SurfaceColors;
import com.raredev.theblocklogics.utils.FilePicker;

public abstract class BaseActivity extends AppCompatActivity {

  private FilePicker filePicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setStatusBarColor(getStatusBarColor());
    getWindow().setNavigationBarColor(getNavigationBarColor());
    setContentView(bindLayout());

    filePicker = new FilePicker(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    filePicker.destroy();
  }

  protected abstract View bindLayout();

  public int getStatusBarColor() {
    return SurfaceColors.SURFACE_0.getColor(this);
  }

  public int getNavigationBarColor() {
    return SurfaceColors.SURFACE_0.getColor(this);
  }

  public FilePicker getFilePicker() {
    return filePicker;
  }
}
