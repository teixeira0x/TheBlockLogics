package com.raredev.theblocklogics.editor.view.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.ViewGroup;

public class ViewData implements Parcelable {

  public static final int DEFAULT_PADDING = 8;

  // Layouts
  public static final int TYPE_LINEAR_LAYOUT = 0;
  public static final int TYPE_FRAME_LAYOUT = 1;
  public static final int TYPE_RELATIVE_LAYOUT = 2;
  public static final int TYPE_VSCROLL_VIEW = 3;
  public static final int TYPE_HSCROLL_VIEW = 4;
  public static final int TYPE_RADIO_GROUP = 5;

  // Widgets
  public static final int TYPE_TEXT_VIEW = 6;
  public static final int TYPE_EDIT_TEXT = 7;
  public static final int TYPE_BUTTON = 8;
  public static final int TYPE_IMAGE_VIEW = 9;
  public static final int TYPE_CHECK_BOX = 10;
  public static final int TYPE_RADIO_BUTTON = 11;
  public static final int TYPE_SWITCH = 12;
  public static final int TYPE_SEEK_BAR = 13;
  public static final int TYPE_PROGRESS_BAR = 14;

  public String parentId;
  public String id;

  public int parentType;
  public int type;

  public int width;
  public int height;
  public int paddingLeft;
  public int paddingTop;
  public int paddingRight;
  public int paddingBottom;
  public int index;

  public LayoutData layout;
  public TextData text;

  public ViewData(int type) {
    this.type = type;
    width = ViewGroup.LayoutParams.WRAP_CONTENT;
    height = ViewGroup.LayoutParams.WRAP_CONTENT;
    paddingLeft = DEFAULT_PADDING;
    paddingTop = DEFAULT_PADDING;
    paddingRight = DEFAULT_PADDING;
    paddingBottom = DEFAULT_PADDING;

    layout = new LayoutData();
    text = new TextData();
  }

  public void setPadding(int padding) {
    this.paddingLeft = padding;
    this.paddingTop = padding;
    this.paddingRight = padding;
    this.paddingBottom = padding;
  }

  public static final Creator<ViewData> CREATOR =
      new Creator<>() {

        @Override
        public ViewData createFromParcel(Parcel parcel) {
          return new ViewData(parcel);
        }

        @Override
        public ViewData[] newArray(int size) {
          return new ViewData[size];
        }
      };

  public ViewData(Parcel parcel) {
    parentId = parcel.readString();
    id = parcel.readString();
    parentType = parcel.readInt();
    type = parcel.readInt();
    width = parcel.readInt();
    height = parcel.readInt();
    paddingLeft = parcel.readInt();
    paddingTop = parcel.readInt();
    paddingRight = parcel.readInt();
    paddingBottom = parcel.readInt();
    layout = parcel.readParcelable(LayoutData.class.getClassLoader(), LayoutData.class);
    text = parcel.readParcelable(TextData.class.getClassLoader(), TextData.class);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int args) {
    parcel.writeString(parentId);
    parcel.writeString(id);
    parcel.writeInt(parentType);
    parcel.writeInt(type);
    parcel.writeInt(width);
    parcel.writeInt(height);
    parcel.writeInt(paddingLeft);
    parcel.writeInt(paddingTop);
    parcel.writeInt(paddingRight);
    parcel.writeInt(paddingBottom);
    parcel.writeInt(index);
    parcel.writeParcelable(layout, args);
    parcel.writeParcelable(text, args);
  }
}
