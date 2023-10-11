package com.raredev.theblocklogics.editor.view.palette;

import android.widget.TextView;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.utils.Constants;

public class TextViewPaletteItem extends PaletteItem {

  public TextViewPaletteItem() {
    super(R.mipmap.ic_palette_text_view, Constants.TEXT_VIEW);
  }

  @Override
  public String getViewClassName() {
    return TextView.class.getName();
  }

  @Override
  public ViewData createViewData() {
    ViewData viewData = new ViewData(ViewData.TYPE_TEXT_VIEW);
    viewData.text.text = Constants.TEXT_VIEW;
    return viewData;
  }
}
