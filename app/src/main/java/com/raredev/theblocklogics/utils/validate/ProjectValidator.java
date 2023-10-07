package com.raredev.theblocklogics.utils.validate;

import com.raredev.theblocklogics.utils.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class ProjectValidator {

  public static boolean isValidConfig(JSONObject configJson) throws JSONException {
    Object appName = configJson.get(Constants.PROJECT_NAME_KEY);
    Object appPackage = configJson.get(Constants.PROJECT_PACKAGE_KEY);
    Object versionCode = configJson.get(Constants.PROJECT_VERSION_CODE_KEY);
    Object versionName = configJson.get(Constants.PROJECT_VERSION_NAME_KEY);

    return (appName instanceof String
        && appPackage instanceof String
        && versionCode instanceof Integer
        && versionName instanceof String);
  }
}
