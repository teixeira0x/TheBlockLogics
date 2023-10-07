package com.raredev.theblocklogics.activities;

import static com.google.android.material.R.attr;

import android.os.Bundle;
import android.view.View;
import com.google.android.material.color.MaterialColors;
import com.raredev.theblocklogics.databinding.ActivityProjectBinding;

public class ProjectActivity extends BaseActivity {

  private ActivityProjectBinding binding;

  @Override
  protected View bindLayout() {
    binding = ActivityProjectBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(binding.toolbar);
  }

  @Override
  public int getStatusBarColor() {
    return MaterialColors.getColor(this, attr.colorOnSurfaceInverse, 0);
  }

  @Override
  public int getNavigationBarColor() {
    return MaterialColors.getColor(this, attr.colorOnSurfaceInverse, 0);
  }
}
