package com.raredev.theblocklogics.models;

public class Property {

  public static final int TYPE_ID = 0;
  public static final int TYPE_WIDTH = 1;
  public static final int TYPE_HEIGHT = 2;
  public static final int TYPE_PADDING = 3;
  public static final int TYPE_GRAVITY = 4;
  public static final int TYPE_LAYOUT_GRAVITY = 5;
  public static final int TYPE_TEXT = 6;
  public static final int TYPE_HINT = 7;

  private int type;

  private String name;

  private String value;

  public Property(int type, String name, String value) {
    this.type = type;
    this.name = name;
    this.value = value;
  }

  public int getType() {
    return this.type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
