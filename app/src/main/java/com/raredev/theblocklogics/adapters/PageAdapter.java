package com.raredev.theblocklogics.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.ArrayList;
import java.util.List;

public class PageAdapter extends FragmentStateAdapter {

  private List<Fragment> fragments;

  public PageAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
    fragments = new ArrayList<>();
  }

  @Override
  public int getItemCount() {
    return fragments.size();
  }

  @Override
  public Fragment createFragment(int position) {
    return fragments.get(position);
  }

  public void addFragment(@NonNull Fragment fragment) {
    fragments.add(fragment);
  }

  @Nullable
  public Fragment getFragment(int position) {
    return fragments.get(position);
  }
}
