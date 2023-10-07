package com.raredev.theblocklogics.activities;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.DeviceUtils;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.ActivityCrashBinding;
import com.raredev.theblocklogics.utils.Constants;
import java.util.Calendar;
import java.util.Date;

public class CrashActivity extends BaseActivity {

  private static final String newLine = "\n";

  private ActivityCrashBinding binding;

  @Override
  protected View bindLayout() {
    binding = ActivityCrashBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(binding.toolbar);

    var error = new StringBuilder();

    error.append("Manufacturer: " + DeviceUtils.getManufacturer() + "\n");
    error.append("Device: " + DeviceUtils.getModel() + newLine);
    error.append(getSoftwareInfo());
    error.append(newLine).append(newLine);
    error.append(getIntent().getStringExtra(Constants.KEY_EXTRA_ERROR));
    error.append(newLine).append(newLine);
    error.append(getDate());
    error.append(newLine).append(newLine);

    binding.result.setText(error.toString());

    binding.fab.setOnClickListener(v -> ClipboardUtils.copyText(binding.result.getText()));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    var close = menu.add(getString(R.string.close));
    close.setContentDescription("Close App");
    close.setIcon(R.drawable.ic_close);
    close.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getTitle().equals(getString(R.string.close))) {
      onBackPressed();
      return true;
    }
    return false;
  }

  @Override
  public void onBackPressed() {
    finishAffinity();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  private String getSoftwareInfo() {
    return new StringBuilder("SDK: ")
        .append(Build.VERSION.SDK_INT)
        .append(newLine)
        .append("Android: ")
        .append(Build.VERSION.RELEASE)
        .append(newLine)
        .append("Model: ")
        .append(Build.VERSION.INCREMENTAL)
        .append(newLine)
        .toString();
  }

  private Date getDate() {
    return Calendar.getInstance().getTime();
  }
}
