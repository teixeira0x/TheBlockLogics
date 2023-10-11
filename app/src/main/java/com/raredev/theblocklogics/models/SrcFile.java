package com.raredev.theblocklogics.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.raredev.theblocklogics.managers.ProjectDataManager;

public class SrcFile implements Parcelable {

  private int type;
  private String name;
  private String code;

  public SrcFile(int type, String name, String code) {
    this.type = type;
    this.name = name;
    this.code = code;
  }

  public int getType() {
    return this.type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    if (type == ProjectFile.TYPE_XML) {
      return this.name + ".xml";
    }
    return ProjectDataManager.getLayoutActivityName(name);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String toString() {
    return getName();
  }

  public static final Creator<SrcFile> CREATOR =
      new Creator<>() {

        @Override
        public SrcFile createFromParcel(Parcel parcel) {
          return new SrcFile(parcel);
        }

        @Override
        public SrcFile[] newArray(int size) {
          return new SrcFile[size];
        }
      };

  public SrcFile(Parcel parcel) {
    type = parcel.readInt();
    name = parcel.readString();
    code = parcel.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int args) {
    parcel.writeInt(type);
    parcel.writeString(name);
    parcel.writeString(code);
  }
}
