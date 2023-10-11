package com.raredev.theblocklogics.editor.view.palette;

import com.raredev.theblocklogics.editor.view.data.ViewData;

public class CategoryPaletteItem extends PaletteItem {

  public CategoryPaletteItem(String text) {
    super(-1, text);
  }

  @Override
  public ViewData createViewData() {
    // do nothing
    return null;
  }
}
