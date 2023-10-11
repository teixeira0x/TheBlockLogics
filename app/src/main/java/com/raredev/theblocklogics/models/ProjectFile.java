package com.raredev.theblocklogics.models;

public class ProjectFile {

  public static final int TYPE_XML = 0;
  public static final int TYPE_JAVA = 1;

  private String name;

  public ProjectFile(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
