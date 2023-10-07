package com.raredev.theblocklogics.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.activities.BaseActivity;
import com.raredev.theblocklogics.activities.ProjectActivity;
import com.raredev.theblocklogics.databinding.DialogNewProjectBinding;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.FilePicker;
import com.raredev.theblocklogics.utils.FileUtil;
import com.raredev.theblocklogics.utils.Logger;
import com.raredev.theblocklogics.utils.OnTextChangedWatcher;
import com.raredev.theblocklogics.utils.UriUtils;
import com.raredev.theblocklogics.viewmodel.MainViewModel;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigProjectDialog {

  private static final int APP_ICON_RESULT_CODE = 55;
  private static final String APP_NAME_PATTERN = "^[a-zA-Z0-9 ]+$";
  private static final String APP_PACKAGE_PATTERN = "^[a-z-0-9.]+$";

  private final Logger logger = Logger.newInstance("ConfigProjectDialog");
  private final Context context;
  private final MainViewModel mainViewModel;
  private final Project project;

  private DialogNewProjectBinding binding;
  private AlertDialog dialog;
  private FilePicker filePicker;

  private boolean advancedOptionsShowing;

  public ConfigProjectDialog(
      BaseActivity activity, MainViewModel mainViewModel, LayoutInflater layoutInflater) {
    this(activity, mainViewModel, layoutInflater, null);
  }

  public ConfigProjectDialog(
      BaseActivity activity,
      MainViewModel mainViewModel,
      LayoutInflater layoutInflater,
      Project project) {
    this.context = activity;
    this.mainViewModel = mainViewModel;
    this.project = project;
    this.filePicker = activity.getFilePicker();

    this.filePicker.addCallback(
        APP_ICON_RESULT_CODE,
        (uri) -> {
          var drawable = UriUtils.convertUriToDrawable(uri);
          binding.chooseIcon.setImageDrawable(drawable);
        });

    createDialog(layoutInflater);
  }

  private void createDialog(LayoutInflater layoutInflater) {
    binding = DialogNewProjectBinding.inflate(layoutInflater);
    var builder = new MaterialAlertDialogBuilder(context);
    builder.setView(binding.getRoot());
    builder.setTitle(project != null ? R.string.edit_project : R.string.new_project);
    builder.setPositiveButton(project != null ? R.string.save : R.string.create, null);
    builder.setNegativeButton(R.string.cancel, null);

    builder.setOnDismissListener(this::onCancelAndDismiss);
    builder.setOnCancelListener(this::onCancelAndDismiss);

    var dialog = builder.create();
    dialog.setCancelable(false);

    dialog.setOnShowListener(
        (unused) -> {
          setListeners(dialog);
          setProjectDetails();
          addTextWatchers();
        });
    setAdvancedOptionsState(false);

    this.dialog = dialog;
  }

  private void onCancelAndDismiss(DialogInterface di) {
    filePicker.removeCallback(APP_ICON_RESULT_CODE);
  }

  public void show() {
    dialog.show();
  }

  private void setListeners(AlertDialog dialog) {
    var positive = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
    var negative = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

    positive.setOnClickListener(v -> dialog.dismiss());
    negative.setOnClickListener(
        v -> {
          try {
            if (!isValidDetails()) {
              return;
            }
            writeProject(
                binding.tieName.getText().toString(),
                binding.tiePackage.getText().toString(),
                Integer.valueOf(binding.tieVersionCode.getText().toString()),
                binding.tieVersionName.getText().toString());

            dialog.dismiss();
            if (mainViewModel.getSelectedFragment().getValue() != Constants.HOME_FRAGMENT) {
              mainViewModel.setSelectedFragment(Constants.HOME_FRAGMENT);
              return;
            }
            mainViewModel.loadProjects();
          } catch (JSONException e) {
            e.printStackTrace();
          }
        });

    binding.chooseIcon.setOnClickListener(
        v -> {
          var intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
          filePicker.launch(APP_ICON_RESULT_CODE, intent);
        });

    binding.advancedOptionsToggle.setOnClickListener(
        v -> setAdvancedOptionsState(!advancedOptionsShowing));
  }

  private void setProjectDetails() {
    if (project != null) {
      binding.tieName.setText(project.getAppName());
      binding.tiePackage.setText(project.getAppPackage());
      binding.tieVersionCode.setText(String.valueOf(project.getVersionCode()));
      binding.tieVersionName.setText(project.getVersionName());
      binding.chooseIcon.setImageDrawable(Drawable.createFromPath(project.getAppIconPath()));
    }
  }

  private void setAdvancedOptionsState(boolean showing) {
    advancedOptionsShowing = showing;

    int visibility = -1;
    if (advancedOptionsShowing) {
      visibility = View.VISIBLE;
    } else {
      visibility = View.GONE;
    }
    binding.advancedOptions.setVisibility(visibility);
  }

  private void writeProject(String appName, String appPackage, int versionCode, String versionName)
      throws JSONException {
    var projectsDir = new File(Constants.PROJECTS_DIR_PATH);
    var projectName =
        project != null ? project.getProjectDirName() : getNewProjectName(projectsDir);
    var projectDir = new File(projectsDir, File.separator + projectName);
    if (!projectDir.exists()) {
      projectDir.mkdirs();
    }

    var configJson = new JSONObject();
    configJson.put(Constants.PROJECT_NAME_KEY, appName);
    configJson.put(Constants.PROJECT_PACKAGE_KEY, appPackage);
    configJson.put(Constants.PROJECT_VERSION_CODE_KEY, versionCode);
    configJson.put(Constants.PROJECT_VERSION_NAME_KEY, versionName);

    FileUtil.writeBitmapDrawableImage(
        projectDir.getAbsolutePath() + File.separator + Constants.PROJECT_ICON_FILE_NAME,
        binding.chooseIcon.getDrawable());

    FileUtil.writeFile(
        projectDir.getAbsolutePath() + File.separator + Constants.PROJECT_CONFIG_FILE_NAME,
        configJson.toString());

    if (project == null) {
      startProject(projectDir.getAbsolutePath());
    }
  }

  private int getNewProjectName(File projectsDir) {
    int maxCount = 600;

    var projectDirsList = projectsDir.listFiles((f, unused) -> f.isDirectory());
    if (projectDirsList != null) {
      for (File file : projectDirsList) {
        var name = file.getName();
        if (TextUtils.isDigitsOnly(name)) {
          int nameInt = Integer.valueOf(name);
          maxCount = Math.max(maxCount, nameInt);
        }
      }
    }

    return maxCount + 1;
  }

  private void startProject(String projectPath) {
    var appName = binding.tieName.getText().toString();
    var appPackage = binding.tiePackage.getText().toString();
    var versionCode = Integer.parseInt(binding.tieVersionCode.getText().toString());
    var versionName = binding.tieVersionName.getText().toString();

    var project = new Project(projectPath, appName, appPackage, versionCode, versionName);

    var intent = new Intent(context, ProjectActivity.class);
    intent.putExtra(Constants.KEY_EXTRA_PROJECT, project);
    context.startActivity(intent);
  }

  private void addTextWatchers() {
    binding.tieName.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            verifyName(editable);
          }
        });
    binding.tiePackage.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            verifyPackage(editable);
          }
        });
    binding.tieVersionCode.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            verifyVersionCode(editable);
          }
        });
    binding.tieVersionName.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            verifyVersionName(editable);
          }
        });
  }

  private boolean isValidDetails() {
    if (binding.tilName.isErrorEnabled()) {
      return false;
    }

    if (binding.tilPackage.isErrorEnabled()) {
      return false;
    }

    if (binding.tilVersionCode.isErrorEnabled()) {
      return false;
    }

    if (binding.tilVersionName.isErrorEnabled()) {
      return false;
    }

    return true;
  }

  public void verifyName(Editable editable) {
    var appName = editable.toString().trim();

    if (appName.length() <= 0) {
      binding.tilName.setError(context.getString(R.string.error_field_empty));
    } else if (!appName.matches(APP_NAME_PATTERN)) {
      binding.tilName.setError(context.getString(R.string.error_invalid_characters));
    } else {
      binding.tilName.setErrorEnabled(false);
    }
  }

  public void verifyPackage(Editable editable) {
    var appPackage = editable.toString().trim();

    if (appPackage.length() <= 0) {
      binding.tilPackage.setError(context.getString(R.string.error_field_empty));
    } else if (!appPackage.matches(APP_PACKAGE_PATTERN) || appPackage.endsWith(".")) {
      binding.tilPackage.setError(context.getString(R.string.error_invalid_characters));
    } else {
      binding.tilPackage.setErrorEnabled(false);
    }
  }

  public void verifyVersionCode(Editable editable) {
    var versionCode = editable.toString().trim();

    if (versionCode.length() <= 0) {
      binding.tilVersionCode.setError(context.getString(R.string.error_field_empty));
    } else if (versionCode.length() > 4) {
      binding.tilVersionCode.setError(
          context.getString(R.string.error_field_exceeds_character_limit));
    } else if (versionCode.contains(".")) {
      binding.tilVersionCode.setError(context.getString(R.string.error_invalid_characters));
    } else {
      binding.tilVersionCode.setErrorEnabled(false);
    }
  }

  public void verifyVersionName(Editable editable) {
    var versionName = editable.toString().trim();

    if (versionName.length() <= 0) {
      binding.tilVersionName.setError(context.getString(R.string.error_field_empty));
    } else if (versionName.endsWith(".")) {
      binding.tilVersionName.setError(context.getString(R.string.error_invalid_characters));
    } else {
      binding.tilVersionName.setErrorEnabled(false);
    }
  }
}
