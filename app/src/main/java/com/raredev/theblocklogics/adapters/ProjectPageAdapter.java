package com.raredev.theblocklogics.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.activities.ProjectActivity;
import com.raredev.theblocklogics.fragments.BlankFragment;
import com.raredev.theblocklogics.fragments.project.ViewFragment;

public class ProjectPageAdapter extends FragmentStateAdapter {

  public ProjectPageAdapter(@NonNull FragmentActivity fragmentActivity) {
    super(fragmentActivity);
  }

  @NonNull
  @Override
  public Fragment createFragment(int position) {
    if (position == 0) {
      return ViewFragment.newInstance();
    }
    return BlankFragment.newInstance();
  }

  @Override
  public int getItemCount() {
    return 3;
  }

  public String getTitle(Context context, int position) {
    switch (position) {
      case ProjectActivity.VIEW_FRAGMENT_POS:
        return context.getString(R.string.view);
      case ProjectActivity.EVENT_FRAGMENT_POS:
        return context.getString(R.string.event);
      case ProjectActivity.COMPONENT_FRAGMENT_POS:
        return context.getString(R.string.component);
    }
    return null;
  }
}
