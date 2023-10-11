package com.raredev.theblocklogics.managers;

import android.text.TextUtils;
import com.blankj.utilcode.util.EncodeUtils;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.models.ProjectFile;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectDataManager {

  public static Map<String, List<ViewData>> views;
  public static List<ProjectFile> files;

  public static void clearData() {
    if (views != null) {
      views.clear();
    }

    if (files != null) {
      files.clear();
    }

    views = new LinkedTreeMap<>();
    files = new ArrayList<>();
  }

  public static void loadProject(Project project) {
    clearData();
    final var gson = new Gson();

    var filesJson =
        new String(
            EncodeUtils.base64Decode(
                FileUtil.readFile(project.getPath() + File.separator + "file")));

    if (!TextUtils.isEmpty(filesJson)) {
      var type = new TypeToken<List<ProjectFile>>() {}.getType();
      files.addAll(gson.fromJson(filesJson, type));
    } else {
      files.add(new ProjectFile(Constants.MAIN));
    }

    var viewsJson =
        new String(
            EncodeUtils.base64Decode(
                FileUtil.readFile(project.getPath() + File.separator + "view")));

    if (!TextUtils.isEmpty(viewsJson)) {
      var type = new TypeToken<Map<String, List<ViewData>>>() {}.getType();
      views.putAll(gson.fromJson(viewsJson, type));
    } else {
      views.put(Constants.MAIN, new ArrayList<>());
    }
  }

  public static void writeProjectData(String path) {
    final var gson = new Gson();

    var filesJson = EncodeUtils.base64Encode2String(gson.toJson(files).getBytes());
    FileUtil.writeFile(path + File.separator + "file", filesJson);

    var viewsJson = EncodeUtils.base64Encode2String(gson.toJson(views).getBytes());
    FileUtil.writeFile(path + File.separator + "view", viewsJson);
  }

  public static boolean constainsView(String name) {
    for (ProjectFile file : files) {
      if (file.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public static void addProjectFile(ProjectFile file) {
    addView(file.getName(), new ArrayList<>());

    files.add(file);
  }

  public static void addView(String viewName, List<ViewData> viewsData) {
    views.put(viewName, viewsData);
  }

  public static List<ViewData> getView(String viewName) {
    return views.get(viewName);
  }

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
