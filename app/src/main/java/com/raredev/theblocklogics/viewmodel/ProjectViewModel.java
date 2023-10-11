package com.raredev.theblocklogics.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.raredev.theblocklogics.models.Project;

public class ProjectViewModel extends ViewModel {

  private MutableLiveData<Project> openedProject = new MutableLiveData<>(null);

  private MutableLiveData<String> previousSelectedFileName = new MutableLiveData<>(null);
  private MutableLiveData<String> selectedFileName = new MutableLiveData<>(null);

  public LiveData<Project> getOpenedProject() {
    return this.openedProject;
  }

  public void setOpenedProject(Project project) {
    this.openedProject.setValue(project);
  }

  public LiveData<String> getSelectedFileNameLiveData() {
    return this.selectedFileName;
  }

  public String getPreviousSelectedFileName() {
    return this.previousSelectedFileName.getValue();
  }

  public String getSelectedFileName() {
    return getSelectedFileNameLiveData().getValue();
  }

  public void setSelectedFileName(String name) {
    var previousFile = selectedFileName.getValue();
    if (!name.equals(previousFile)) {
      previousSelectedFileName.setValue(previousFile);
      selectedFileName.setValue(name);
    }
  }
}
