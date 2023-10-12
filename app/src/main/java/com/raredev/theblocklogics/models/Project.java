package com.raredev.theblocklogics.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.FileUtil;
import java.io.File;

public class Project implements Parcelable {

  private String path;
  private String projectCode;

  private String appName;
  private String appPackage;

  private int versionCode;
  private String versionName;

  public Project(Parcel parcel) {
    path = parcel.readString();
    projectCode = parcel.readString();
    appName = parcel.readString();
    appPackage = parcel.readString();
    versionCode = parcel.readInt();
    versionName = parcel.readString();
  }

  public Project(
      String path, String appName, String appPackage, int versionCode, String versionName) {
    this.path = path;
    this.appName = appName;
    this.appPackage = appPackage;
    this.versionCode = versionCode;
    this.versionName = versionName;

    this.projectCode = FileUtil.getLastSegmentFromPath(path);
  }

  public String getAppIconPath() {
    return this.path + File.separator + Constants.PROJECT_ICON_FILE_NAME;
  }

  public String getProjectConfigPath() {
    return this.path + File.separator + Constants.PROJECT_CONFIG_FILE_NAME;
  }

  public String getAppPath() {
    return this.path + File.separator + "app";
  }

  public String getSrcPath() {
    return getAppPath() + File.separator + "src";
  }

  public String getJavaPath() {
    return getSrcPath() + File.separator + appPackage.replaceAll(".", File.separator);
  }

  public String getLayoutPath() {
    return getSrcPath() + File.separator + "res/layout";
  }

  public String getBakPath() {
    return this.path + File.separator + "bak";
  }

  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getProjectCode() {
    return this.projectCode;
  }

  public String getAppName() {
    return this.appName;
  }

  public void setAppName(String appName) {
    this.appName = appName;
  }

  public String getAppPackage() {
    return this.appPackage;
  }

  public void setAppPackage(String appPackage) {
    this.appPackage = appPackage;
  }

  public int getVersionCode() {
    return this.versionCode;
  }

  public void setVersionCode(int versionCode) {
    this.versionCode = versionCode;
  }

  public String getVersionName() {
    return this.versionName;
  }

  public void setVersionName(String versionName) {
    this.versionName = versionName;
  }

  public static final Creator<Project> CREATOR =
      new Creator<>() {

        @Override
        public Project createFromParcel(Parcel parcel) {
          return new Project(parcel);
        }

        @Override
        public Project[] newArray(int size) {
          return new Project[size];
        }
      };

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int args) {
    parcel.writeString(path);
    parcel.writeString(projectCode);
    parcel.writeString(appName);
    parcel.writeString(appPackage);
    parcel.writeInt(versionCode);
    parcel.writeString(versionName);
  }
}
