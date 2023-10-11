package com.raredev.theblocklogics.editor.view.palette;

import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.utils.Constants;

public abstract class PaletteItem {

  private int icon;
  private String name;

  public PaletteItem(int icon, String name) {
    this.icon = icon;
    this.name = name;
  }

  public int getIcon() {
    return this.icon;
  }

  public void setIcon(int icon) {
    this.icon = icon;
  }

  public String getViewClassName() {
    return Constants.UNDEFINED;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public abstract ViewData createViewData();
}
