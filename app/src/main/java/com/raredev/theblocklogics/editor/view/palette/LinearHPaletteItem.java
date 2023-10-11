package com.raredev.theblocklogics.editor.view.palette;

import android.widget.LinearLayout;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.data.ViewData;

public class LinearHPaletteItem extends PaletteItem {

  public LinearHPaletteItem() {
    super(R.mipmap.ic_palette_linear_layout_horz, "LinearLayout (H)");
  }

  @Override
  public String getViewClassName() {
    return LinearLayout.class.getName();
  }

  @Override
  public ViewData createViewData() {
    ViewData viewData = new ViewData(ViewData.TYPE_LINEAR_LAYOUT);
    viewData.width = LinearLayout.LayoutParams.MATCH_PARENT;
    viewData.layout.orientation = LinearLayout.HORIZONTAL;
    return viewData;
  }
}
