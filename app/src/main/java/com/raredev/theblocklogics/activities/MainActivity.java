package com.raredev.theblocklogics.activities;

import android.os.Bundle;
import android.view.View;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.ViewModelProvider;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.ActivityMainBinding;
import com.raredev.theblocklogics.dialogs.ConfigProjectDialog;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.viewmodel.MainViewModel;
import java.util.Arrays;

public class MainActivity extends BaseActivity {

  private ActivityMainBinding binding;
  private MainViewModel viewModel;

  private final OnBackPressedCallback onBackPressedCallback =
      new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
          var previousFragment = viewModel.getPreviousFragment().getValue();
          if (previousFragment != -1) {
            viewModel.setSelectedFragment(previousFragment);
          }
        }
      };

  @Override
  protected View bindLayout() {
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(binding.toolbar);

    viewModel = new ViewModelProvider(this).get(MainViewModel.class);
    viewModel.observeSelectedFragment(this, this::onSelectFragment);
    viewModel.observeNavigationSelectedItemId(
        this, (selectedItemId) -> binding.bottomNavigation.setSelectedItemId(selectedItemId));

    binding.bottomNavigation.setOnItemSelectedListener(
        (item) -> {
          var id = item.getItemId();
          if (id == R.id.menu_home) {
            viewModel.setSelectedFragment(Constants.HOME_FRAGMENT);
          }
          if (id == R.id.menu_settings) {
            viewModel.setSelectedFragment(Constants.SETTINGS_FRAGMENT);
          }
          return true;
        });

    binding.fab.setOnClickListener(
        v -> new ConfigProjectDialog(this, viewModel, getLayoutInflater()).show());

    if (viewModel.getSelectedFragment().getValue() == -1
        && viewModel.getPreviousFragment().getValue() == -1) {
      viewModel.setSelectedFragment(Constants.HOME_FRAGMENT);
    } else {
      viewModel.setSelectedFragment(viewModel.getSelectedFragment().getValue());
    }
    getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  private void onSelectFragment(int fragmentIndex) {
    FragmentContainerView selectedFragment = null;
    if (fragmentIndex == Constants.HOME_FRAGMENT) {
      selectedFragment = binding.home;
    } else if (fragmentIndex == Constants.SETTINGS_FRAGMENT) {
      selectedFragment = binding.settings;
    }

    var fragmentContainers = Arrays.asList(binding.home, binding.settings);
    for (FragmentContainerView fragmentContainer : fragmentContainers) {
      fragmentContainer.setVisibility(
          fragmentContainer == selectedFragment ? View.VISIBLE : View.GONE);
    }
    onBackPressedCallback.setEnabled(fragmentIndex != Constants.HOME_FRAGMENT);
  }
}
