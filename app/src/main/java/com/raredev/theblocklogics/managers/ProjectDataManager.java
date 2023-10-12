package com.raredev.theblocklogics.managers;

import android.text.TextUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.exceptions.LoadProjectErrorException;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.models.ProjectFile;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.FileUtil;
import com.raredev.theblocklogics.utils.validate.FileValidator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Class to manage project data. */
public class ProjectDataManager {

  public static Map</* View name. */ String, /* Views data. */ List<ViewData>> views;
  public static List<ProjectFile> files;

  /** Reset the data. */
  public static void clearData() {
    if (views != null) {
      views.clear();
    }

    if (files != null) {
      files.clear();
    }

    views = null;
    files = null;
  }

  /**
   * Load the project files.
   *
   * @param project The project to load files.
   */
  public static void loadProjectFiles(Project project) throws LoadProjectErrorException {
    try {
      List<ProjectFile> savedFiles = null;
      File file = new File(project.getPath() + File.separator + "file");
      if (FileValidator.isValidFile(file)) {
        var filesJson = readEncodedFile(file.getAbsolutePath());

        if (!TextUtils.isEmpty(filesJson))
          savedFiles =
              new Gson().fromJson(filesJson, new TypeToken<List<ProjectFile>>() {}.getType());
      }

      if (savedFiles != null && !savedFiles.isEmpty()) {
        files = savedFiles;
      } else {
        files = new ArrayList<>();
        files.add(new ProjectFile(Constants.MAIN));
      }
    } catch (Exception e) {
      throw new LoadProjectErrorException(
          "Unable to load project files list\n\n" + ThrowableUtils.getFullStackTrace(e));
    }
  }

  /**
   * Load project Views(Layouts).
   *
   * @param project The project to load the views.
   */
  public static void loadProjectViews(Project project) throws LoadProjectErrorException {
    try {
      Map<String, List<ViewData>> savedViews = null;

      File file = new File(project.getPath() + File.separator + "view");
      if (FileValidator.isValidFile(file)) {
        var viewsJson = readEncodedFile(project.getPath() + File.separator + "view");

        if (!TextUtils.isEmpty(viewsJson))
          savedViews =
              new Gson()
                  .fromJson(viewsJson, new TypeToken<Map<String, List<ViewData>>>() {}.getType());
      }

      if (savedViews != null && !savedViews.isEmpty()) {
        views = savedViews;
      } else {
        views = new LinkedTreeMap<>();
        views.put(Constants.MAIN, new ArrayList<>());
      }
    } catch (Exception e) {
      throw new LoadProjectErrorException(
          "Unable to load project views list\n\n" + ThrowableUtils.getFullStackTrace(e));
    }
  }

  /**
   * Write current project data.
   *
   * @param path The directory path to write project data.
   */
  public static void writeProjectData(String path) {
    final var gson = new Gson();

    var filesJson = gson.toJson(files);
    writeEncodedFile(path + File.separator + "file", filesJson);

    var viewsJson = gson.toJson(views);
    writeEncodedFile(path + File.separator + "view", viewsJson);
  }

  /**
   * Read the file and decode.
   *
   * @param filePath The file path to read.
   * @return Returns the content of the file.
   */
  private static String readEncodedFile(String filePath) {
    return new String(EncodeUtils.base64Decode(FileUtil.readFile(filePath)));
  }

  /**
   * Encode and write the file.
   *
   * @param filePath The path that the file should be created.
   * @param json The text to be written to the file.
   */
  private static void writeEncodedFile(String filePath, String text) throws IllegalStateException {
    FileUtil.writeFile(filePath, EncodeUtils.base64Encode2String(text.getBytes()));
  }

  /**
   * Checks if another view with the same name already exists.
   *
   * @param name The name to check.
   * @return Returns true if contains the view.
   */
  public static boolean constainsView(String name) {
    for (ProjectFile file : files) {
      if (file.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Add a new project file.
   *
   * @param file The ProjectFile to be added.
   */
  public static void addProjectFile(ProjectFile file) {
    addView(file.getName(), new ArrayList<>());

    files.add(file);
  }

  /**
   * Add a new view or update an existing view.
   *
   * @param viewName The name of the view to be added.
   * @param viewsData List of views data.
   */
  public static void addView(String viewName, List<ViewData> viewsData) {
    views.put(viewName, viewsData);
  }

  /**
   * Get list of views data.
   *
   * @param viewName The name of the view(layout) to get the list of data.
   * @return View data list.
   */
  public static List<ViewData> getView(String viewName) {
    return views.get(viewName);
  }

  /**
   * Gets the name of the activity from the given layout.
   *
   * @param layout Layout name to get activity name.
   * @return Returns the name of the layout activity.
   */
  public static String getLayoutActivityName(String layout) {
    if (layout.endsWith(".xml")) {
      layout = layout.replace(".xml", "");
    }
    String[] words = layout.split("_");
    StringBuilder className = new StringBuilder("Activity.java");

    for (String word : words) {
      className.insert(0, capitalize(word));
    }

    return className.toString();
  }

  public static String capitalize(String word) {
    return word.isEmpty() ? word : Character.toUpperCase(word.charAt(0)) + word.substring(1);
  }
}
