package com.raredev.theblocklogics.utils.loaders;

import static com.raredev.theblocklogics.utils.Constants.PROJECTS_DIR_PATH;
import static com.raredev.theblocklogics.utils.Constants.PROJECT_CONFIG_FILE_NAME;
import static com.raredev.theblocklogics.utils.Constants.PROJECT_ICON_FILE_NAME;
import static com.raredev.theblocklogics.utils.Constants.PROJECT_NAME_KEY;
import static com.raredev.theblocklogics.utils.Constants.PROJECT_PACKAGE_KEY;
import static com.raredev.theblocklogics.utils.Constants.PROJECT_VERSION_CODE_KEY;
import static com.raredev.theblocklogics.utils.Constants.PROJECT_VERSION_NAME_KEY;

import android.text.TextUtils;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.utils.FileUtil;
import com.raredev.theblocklogics.utils.Logger;
import com.raredev.theblocklogics.utils.validate.FileValidator;
import com.raredev.theblocklogics.utils.validate.ProjectValidator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectsLoader {

  private static final Logger logger = Logger.newInstance("ProjectsLoader");

  public static List<Project> fetchProjects() {
    List<Project> projects = new ArrayList<>();
    File projectsDir = new File(PROJECTS_DIR_PATH);
    if (!projectsDir.exists()) {
      logger.d("Creating projects directory");
      projectsDir.mkdirs();
    }

    File[] projectDirsList = projectsDir.listFiles((f, unused) -> f.isDirectory());
    if (projectDirsList.length < 0) {
      logger.d("The projects directory is empty");
      return projects;
    }

    for (File projectDir : projectDirsList) {
      Project project = getProjectFromDir(projectDir);
      if (project == null) {
        continue;
      }
      projects.add(project);
    }

    Collections.sort(projects, PROJECT_COMPARATOR);

    return projects;
  }

  public static Project getProjectFromDir(File projectDir) {
    if (!FileValidator.isValidDir(projectDir) || !TextUtils.isDigitsOnly(projectDir.getName())) {
      logger.w("Invalid project directory: " + projectDir.getName());
      return null;
    }
    File iconFile = new File(projectDir, PROJECT_ICON_FILE_NAME);
    if (!FileValidator.isValidFile(iconFile)) {
      logger.w("Invalid icon file");
      return null;
    }
    File configFile = new File(projectDir, PROJECT_CONFIG_FILE_NAME);
    if (!FileValidator.isValidFile(configFile)) {
      logger.w("Invalid configuration file");
      return null;
    }

    try {
      String config = FileUtil.readFile(configFile);

      JSONObject configJson = new JSONObject(config);
      if (!ProjectValidator.isValidConfig(configJson)) {
        return null;
      }

      logger.d("The project: " + configJson.getString(PROJECT_NAME_KEY) + " loaded!");

      return new Project(
          projectDir.getAbsolutePath(),
          configJson.getString(PROJECT_NAME_KEY),
          configJson.getString(PROJECT_PACKAGE_KEY),
          configJson.getInt(PROJECT_VERSION_CODE_KEY),
          configJson.getString(PROJECT_VERSION_NAME_KEY));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static final Comparator<Project> PROJECT_COMPARATOR =
      new Comparator<Project>() {
        @Override
        public int compare(Project p1, Project p2) {
          return p2.getProjectCode().compareTo(p1.getProjectCode());
        }
      };
}
