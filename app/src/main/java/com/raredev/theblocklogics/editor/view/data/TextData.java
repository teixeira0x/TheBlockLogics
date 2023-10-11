package com.raredev.theblocklogics.editor.view.data;

import android.os.Parcel;
import android.os.Parcelable;

public class TextData implements Parcelable {

  public static final int DEFAULT_TEXT_SIZE = 14;

  public int textSize;
  public String text;
  public String hint;

  public TextData() {
    this.textSize = DEFAULT_TEXT_SIZE;
  }

  public static final Creator<TextData> CREATOR =
      new Creator<>() {

        @Override
        public TextData createFromParcel(Parcel parcel) {
          return new TextData(parcel);
        }

        @Override
        public TextData[] newArray(int size) {
          return new TextData[size];
        }
      };

  public TextData(Parcel parcel) {
    textSize = parcel.readInt();
    text = parcel.readString();
    hint = parcel.readString();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int args) {
    parcel.writeInt(textSize);
    parcel.writeString(text);
    parcel.writeString(hint);
  }
}
