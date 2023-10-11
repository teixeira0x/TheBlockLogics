package com.raredev.theblocklogics.editor.view.palette;

import android.widget.Button;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.data.ViewData;

public class ButtonPaletteItem extends PaletteItem {

  public ButtonPaletteItem() {
    super(R.mipmap.ic_palette_button, "Button");
  }

  @Override
  public String getViewClassName() {
    return Button.class.getName();
  }

  @Override
  public ViewData createViewData() {
    ViewData viewData = new ViewData(ViewData.TYPE_BUTTON);
    viewData.text.text = "Button";
    return viewData;
  }
}
