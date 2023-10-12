package com.raredev.theblocklogics.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.tasks.Task;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.loaders.ProjectsLoader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends ViewModel {

  // MainActivity

  private MutableLiveData<Integer> previousFragment = new MutableLiveData<>(-1);
  private MutableLiveData<Integer> selectedFragment = new MutableLiveData<>(-1);

  private MutableLiveData<Integer> navigationSelectedItemId = new MutableLiveData<>(R.id.menu_home);

  public MutableLiveData<Integer> getPreviousFragment() {
    return this.previousFragment;
  }

  public LiveData<Integer> getSelectedFragment() {
    return selectedFragment;
  }

  public void setSelectedFragment(int fragment) {
    var previousFragment = selectedFragment.getValue();

    if (fragment != previousFragment) {
      this.previousFragment.setValue(previousFragment);
      selectedFragment.setValue(fragment);

      if (fragment == Constants.HOME_FRAGMENT) {
        setNavigationSelectedItemId(R.id.menu_home);
      } else if (fragment == Constants.SETTINGS_FRAGMENT) {
        setNavigationSelectedItemId(R.id.menu_settings);
      }
    }
  }

  public void observeSelectedFragment(LifecycleOwner owner, Observer<Integer> observer) {
    selectedFragment.observe(owner, observer);
  }

  public LiveData<Integer> getNavigationSelectedItemId() {
    return this.navigationSelectedItemId;
  }

  public void setNavigationSelectedItemId(int selectedItemId) {
    int previousItemId = this.navigationSelectedItemId.getValue();
    if (previousItemId != selectedItemId) {
      this.navigationSelectedItemId.setValue(selectedItemId);
    }
  }

  public void observeNavigationSelectedItemId(LifecycleOwner owner, Observer<Integer> observer) {
    this.navigationSelectedItemId.observe(owner, observer);
  }

  // HomeFragment

  private MutableLiveData<List<Project>> projects = new MutableLiveData<>(new ArrayList<>());

  public LiveData<List<Project>> getProjects() {
    return this.projects;
  }

  public List<Project> getProjectsList() {
    return projects.getValue();
  }

  public void setPojectsList(List<Project> projects) {
    this.projects.setValue(projects);
  }

  public void loadProjects() {
    if (getSelectedFragment().getValue() != Constants.HOME_FRAGMENT) {
      return;
    }
    new LoadProjectsTask(this).start();
  }

  class LoadProjectsTask extends Task<List<Project>> {

    private WeakReference<MainViewModel> weakReferenceViewModel;

    public LoadProjectsTask(MainViewModel viewModel) {
      weakReferenceViewModel = new WeakReference<>(viewModel);
    }

    @Override
    public List<Project> doInBackground() throws Exception {
      return ProjectsLoader.fetchProjects();
    }

    @Override
    public void onSuccess(List<Project> result) {
      var viewModel = weakReferenceViewModel.get();
      if (viewModel != null) {
        viewModel.setPojectsList(result);
      }
    }
  }
}
