package com.raredev.theblocklogics.utils;

import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SizeUtils;

public class Constants {

  // Main fragments
  public static final int HOME_FRAGMENT = 0;
  public static final int SETTINGS_FRAGMENT = 1;

  // Extra keys
  public static final String KEY_EXTRA_PROJECT = "key_extra_project";
  public static final String KEY_EXTRA_SRC_LIST = "src_list";
  public static final String KEY_EXTRA_ERROR = "key_error_extra";
  public static final String KEY_EXTRA_VIEW_DATA = "view_data";
  public static final String KEY_EXTRA_SELECTED_FILE = "selected_file";

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

  // View editor
  public static final String MAIN = "main";
  public static final String ROOT_ID = "root";
  public static final int LAYOUT_MIN_SIZE = SizeUtils.dp2px(32f);

  public static final String MATCH_PARENT = "match_parent";
  public static final String WRAP_CONTENT = "wrap_content";

  public static final String VERTICAL = "vertical";
  public static final String HORIZONTAL = "horizontal";

  public static final String DP = "dp";
  public static final String SP = "sp";

  // Properties
  public static final String ID = "id";
  public static final String WIDTH = "width";
  public static final String HEIGHT = "height";
  public static final String PADDING = "padding";
  public static final String ORIENTATION = "orientation";
  public static final String TEXT = "text";
  public static final String HINT = "hint";

  // Others
  public static final String UNKNOWN = "Unknown";
  public static final String UNDEFINED = "Undefined";
}
