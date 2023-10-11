package com.raredev.theblocklogics.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.color.MaterialColors;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.dialogs.ProgressDialogBuilder;
import com.raredev.theblocklogics.utils.FilePicker;

public abstract class BaseActivity extends AppCompatActivity {

  private ProgressDialogBuilder progressDlgBuilder;
  private AlertDialog progressDlg;
  private FilePicker filePicker;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setStatusBarColor(getStatusBarColor());
    getWindow().setNavigationBarColor(getNavigationBarColor());
    setContentView(bindLayout());

    progressDlgBuilder = new ProgressDialogBuilder(this);
    progressDlgBuilder.setMessage(R.string.please_wait);
    progressDlg = progressDlgBuilder.create();
    filePicker = new FilePicker(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    filePicker.destroy();
  }

  public void showNonCancelableProgress() {
    if (!progressDlg.isShowing()) {
      progressDlg.setCancelable(false);
      progressDlg.show();
    }
  }

  public void dismissProgress() {
    if (progressDlg.isShowing()) {
      progressDlg.dismiss();
    }
  }

  protected abstract View bindLayout();

  public int getStatusBarColor() {
    return MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurfaceInverse, 0);
  }

  public int getNavigationBarColor() {
    return MaterialColors.getColor(this, com.google.android.material.R.attr.colorOnSurfaceInverse, 0);
  }

  public AlertDialog getProgressDialog() {
    return progressDlg;
  }

  public FilePicker getFilePicker() {
    return filePicker;
  }
}
