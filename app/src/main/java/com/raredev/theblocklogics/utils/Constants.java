package com.raredev.theblocklogics.utils;

import com.blankj.utilcode.util.PathUtils;

public class Constants {

  // Main fragments
  public static final int HOME_FRAGMENT = 0;
  public static final int SETTINGS_FRAGMENT = 1;

  // Extra keys
  public static final String KEY_EXTRA_PROJECT = "key_extra_project";
  public static final String KEY_EXTRA_ERROR = "key_error_extra";

  // Project
  public static final String PROJECT_ICON_FILE_NAME = "app_icon.png";
  public static final String PROJECT_CONFIG_FILE_NAME = "project.config";
  public static final String PROJECT_NAME_KEY = "name";
  public static final String PROJECT_PACKAGE_KEY = "package";
  public static final String PROJECT_VERSION_CODE_KEY = "versionCode";
  public static final String PROJECT_VERSION_NAME_KEY = "versionName";

  // Paths
  public static final String DATA_DIR_PATH = PathUtils.getExternalAppDataPath() + "/files";
  public static final String PROJECTS_DIR_PATH = DATA_DIR_PATH + "/projects";
}
