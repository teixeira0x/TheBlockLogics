package com.raredev.theblocklogics.activities;

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.databinding.ActivityMainBinding
import com.raredev.theblocklogics.dialogs.ConfigProjectDialog
import com.raredev.theblocklogics.utils.Constants
import com.raredev.theblocklogics.viewmodel.MainViewModel

public class MainActivity: BaseActivity() {

  private val viewModel by viewModels<MainViewModel>()

  private var _binding: ActivityMainBinding? = null
  private val binding: ActivityMainBinding
    get() = checkNotNull(_binding)

  private val onBackPressedCallback = object: OnBackPressedCallback(true) {
      override fun handleOnBackPressed() {
        val previousFragment = viewModel.previousFragment.value
        if (previousFragment != null) {
          viewModel.setFragment(previousFragment)
        }
      }
    }

  override fun bindLayout(): View {
    _binding = ActivityMainBinding.inflate(layoutInflater)
    return binding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setSupportActionBar(binding.toolbar)

    viewModel.selectedFragment.observe(this, this::onChangeSelectedFragment)
    viewModel.navSelectedItemId.observe(this) {
      binding.bottomNavigation.setSelectedItemId(it)
    }

    binding.bottomNavigation.setOnItemSelectedListener() {
      val id = it.itemId
      if (id == R.id.menu_home) {
        viewModel.setFragment(Constants.HOME_FRAGMENT)
      } else if (id == R.id.menu_settings) {
        viewModel.setFragment(Constants.SETTINGS_FRAGMENT)
      }
      true
    }

    binding.fab.setOnClickListener() { ConfigProjectDialog.newInstance(null).show(supportFragmentManager, null) }

    if (viewModel.selectedFragment.value == -1 && viewModel.previousFragment.value == -1) {
      viewModel.setFragment(Constants.HOME_FRAGMENT)
    } else {
      onChangeSelectedFragment(viewModel.selectedFragment.value)
    }
    onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
  }

  override fun onDestroy() {
    super.onDestroy()
    _binding = null
  }

  private fun onChangeSelectedFragment(fragmentIndex: Int?) {
    var selectedFragment = when (fragmentIndex) {
      Constants.HOME_FRAGMENT -> binding.home
      Constants.SETTINGS_FRAGMENT -> binding.settings
      else -> throw IllegalArgumentException("Invalid fragment: $fragmentIndex")
    }

    for (fragment in arrayOf(binding.home, binding.settings)) {
      fragment.isVisible = fragment == selectedFragment
    }
    onBackPressedCallback.isEnabled = fragmentIndex != Constants.HOME_FRAGMENT
  }
}
