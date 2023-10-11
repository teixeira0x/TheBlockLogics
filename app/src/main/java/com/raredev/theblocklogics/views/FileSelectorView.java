package com.raredev.theblocklogics.views;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.activities.ProjectActivity;
import com.raredev.theblocklogics.adapters.ProjectFileAdapter;
import com.raredev.theblocklogics.databinding.DialogAddFileBinding;
import com.raredev.theblocklogics.databinding.DialogFileSelectorBinding;
import com.raredev.theblocklogics.databinding.LayoutFileSelectorBinding;
import com.raredev.theblocklogics.managers.ProjectDataManager;
import com.raredev.theblocklogics.models.ProjectFile;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.OnTextChangedWatcher;
import com.raredev.theblocklogics.viewmodel.ProjectViewModel;

public class FileSelectorView extends LinearLayout {

  private LayoutFileSelectorBinding binding;
  private LayoutInflater layoutInflater;

  private ProjectViewModel projectViewModel;

  private String layoutName = Constants.UNDEFINED;
  private String activityName = Constants.UNDEFINED;

  private int selectedPos = ProjectActivity.VIEW_FRAGMENT_POS;

  public FileSelectorView(Context context) {
    this(context, null);
  }

  public FileSelectorView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public FileSelectorView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.layoutInflater = LayoutInflater.from(context);

    binding = LayoutFileSelectorBinding.inflate(layoutInflater, this, true);

    setOnClickListener(v -> showViewSelectorDialog());
  }

  public void setSelectedTabPos(int pos) {
    this.selectedPos = pos;
    updateSelectedFileName();
  }

  public void setProjectViewModel(ProjectViewModel viewModel) {
    this.projectViewModel = viewModel;
  }

  public void setSelectedFileName(String name) {
    if (name != null) {
      layoutName = name + ".xml";
      activityName = ProjectDataManager.getLayoutActivityName(name);
      updateSelectedFileName();
    }
  }

  private void updateSelectedFileName() {
    if (selectedPos <= ProjectActivity.VIEW_FRAGMENT_POS) {
      binding.selectedFileName.setText(layoutName);
    } else {
      binding.selectedFileName.setText(activityName);
    }
  }

  public void showViewSelectorDialog() {
    var binding = DialogFileSelectorBinding.inflate(layoutInflater);

    var builder = new MaterialAlertDialogBuilder(getContext());
    builder.setView(binding.getRoot());

    if (selectedPos == ProjectActivity.VIEW_FRAGMENT_POS) {
      binding.title.setText(R.string.available_views);
      binding.addFile.setVisibility(View.VISIBLE);
    } else {
      binding.title.setText(R.string.available_java_files);
      binding.addFile.setVisibility(View.GONE);
    }

    var dialog = builder.create();
    dialog.setOnShowListener(
        (unused) -> {
          var fileAdapter =
              new ProjectFileAdapter(
                  layoutInflater,
                  selectedPos > ProjectActivity.VIEW_FRAGMENT_POS
                      ? ProjectFile.TYPE_JAVA
                      : ProjectFile.TYPE_XML);
          fileAdapter.setFiles(ProjectDataManager.files);

          fileAdapter.setProjectFileListener(
              new ProjectFileAdapter.ProjectFileListener() {

                @Override
                public void onFileClick(ProjectFile file) {
                  projectViewModel.setSelectedFileName(file.getName());
                  dialog.dismiss();
                }

                @Override
                public boolean onFileLongClick(ProjectFile file) {
                  return true;
                }
              });

          binding.rvViews.setLayoutManager(new LinearLayoutManager(getContext()));
          binding.rvViews.setAdapter(fileAdapter);

          binding.addFile.setOnClickListener(
              v -> {
                showNewLayoutDialog();
                dialog.dismiss();
              });
        });

    dialog.show();
  }

  private void showNewLayoutDialog() {
    var binding = DialogAddFileBinding.inflate(layoutInflater);
    var builder = new MaterialAlertDialogBuilder(getContext());
    builder.setTitle(R.string.new_activity);
    builder.setView(binding.getRoot());
    builder.setPositiveButton(R.string.add, null);
    builder.setNegativeButton(R.string.cancel, null);

    var dialog = builder.create();
    dialog.setOnShowListener(
        (unused) -> {
          var positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
          var negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

          positive.setEnabled(false);
          binding.tieFileName.addTextChangedListener(
              new OnTextChangedWatcher() {
                @Override
                public void afterTextChanged(Editable editable) {
                  verifyName(editable, binding.tilFileName, positive);
                }
              });

          positive.setOnClickListener(
              v -> {
                var addedFileName = binding.tieFileName.getText().toString();
                ProjectDataManager.addProjectFile(new ProjectFile(addedFileName));
                projectViewModel.setSelectedFileName(addedFileName);
                dialog.dismiss();
              });
        });
    dialog.show();
  }

  public void verifyName(Editable editable, TextInputLayout tilName, Button button) {
    var name = editable.toString().trim();

    button.setEnabled(false);
    if (name.length() <= 0) {
      tilName.setError(getContext().getString(R.string.error_field_empty));
    } else if (!name.matches("^[a-zA-Z0-9 ]+$")) {
      tilName.setError(getContext().getString(R.string.error_invalid_characters));
    } else if (ProjectDataManager.constainsView(name)) {
      tilName.setError(getContext().getString(R.string.error_invalid_name));
    }else {
      tilName.setErrorEnabled(false);
      button.setEnabled(true);
    }
  }
}
