package com.raredev.theblocklogics.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.models.Project
import com.raredev.theblocklogics.utils.Constants
import com.raredev.theblocklogics.utils.loaders.ProjectsLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

  // MainActivity

  private val _previousFragment = MutableLiveData(-1)
  private val _selectedFragment = MutableLiveData(-1)

  val previousFragment: LiveData<Int> = _previousFragment
  val selectedFragment: LiveData<Int> = _selectedFragment

  private val _navSelectedItemId = MutableLiveData(R.id.menu_home)

  val navSelectedItemId: LiveData<Int> = _navSelectedItemId

  fun setFragment(fragment: Int) {
    val previous = _selectedFragment.value

    if (fragment != previous) {
      _previousFragment.value = previous
      _selectedFragment.value = fragment

      val navItemId = when (fragment) {
        Constants.HOME_FRAGMENT -> R.id.menu_home
        Constants.SETTINGS_FRAGMENT -> R.id.menu_settings
        else -> throw IllegalArgumentException("Invalid fragment: $fragment")
      }
      setNavigationSelectedItem(navItemId)
    }
  }

  fun setNavigationSelectedItem(itemId: Int) {
    val previousItemId = _navSelectedItemId.value
    if (itemId != previousItemId) {
      _navSelectedItemId.value = itemId
    }
  }

  // HomeFragment

  private val _projects = MutableLiveData<List<Project>?>(null)

  val projects: LiveData<List<Project>?> = _projects

  fun setProjects(projects: List<Project>) {
    this._projects.value = projects
  }

  fun loadProjects() {
    if (_selectedFragment.value == Constants.HOME_FRAGMENT) {
      CoroutineScope(Dispatchers.IO).launch {
        val projects = ProjectsLoader.fetchProjects()

        withContext(Dispatchers.Main) {
          setProjects(projects)
        }
      }
    }
  }
}