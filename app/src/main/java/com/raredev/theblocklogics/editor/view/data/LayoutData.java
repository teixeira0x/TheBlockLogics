package com.raredev.theblocklogics.editor.view.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

public class LayoutData implements Parcelable {

  public int orientation;

  public LayoutData() {
    orientation = LinearLayout.VERTICAL;
  }

  public static final Creator<LayoutData> CREATOR =
      new Creator<>() {

        @Override
        public LayoutData createFromParcel(Parcel parcel) {
          return new LayoutData(parcel);
        }

        @Override
        public LayoutData[] newArray(int size) {
          return new LayoutData[size];
        }
      };

  public LayoutData(Parcel parcel) {
    orientation = parcel.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int args) {
    parcel.writeInt(orientation);
  }
}
