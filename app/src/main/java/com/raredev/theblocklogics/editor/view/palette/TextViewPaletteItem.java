package com.raredev.theblocklogics.editor.view.palette;

import android.widget.TextView;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.data.ViewData;

public class TextViewPaletteItem extends PaletteItem {

  public TextViewPaletteItem() {
    super(R.mipmap.ic_palette_text_view, "TextView");
  }

  @Override
  public String getViewClassName() {
    return TextView.class.getName();
  }

  @Override
  public ViewData createViewData() {
    ViewData viewData = new ViewData(ViewData.TYPE_TEXT_VIEW);
    viewData.text.text = "TextView";
    return viewData;
  }
}
