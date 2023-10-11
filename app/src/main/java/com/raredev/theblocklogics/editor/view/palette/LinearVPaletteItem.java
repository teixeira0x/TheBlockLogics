package com.raredev.theblocklogics.editor.view.palette;

import android.widget.LinearLayout;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.data.ViewData;

public class LinearVPaletteItem extends PaletteItem {

  public LinearVPaletteItem() {
    super(R.mipmap.ic_palette_linear_layout_vert, "LinearLayout (V)");
  }

  @Override
  public String getViewClassName() {
    return LinearLayout.class.getName();
  }

  @Override
  public ViewData createViewData() {
    ViewData viewData = new ViewData(ViewData.TYPE_LINEAR_LAYOUT);
    viewData.height = LinearLayout.LayoutParams.MATCH_PARENT;
    viewData.layout.orientation = LinearLayout.VERTICAL;
    return viewData;
  }
}
