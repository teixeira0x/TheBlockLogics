package com.raredev.theblocklogics.editor.view.palette

import android.widget.LinearLayout
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.editor.view.data.ViewData

class LinearHPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_linear_layout_horz,
  "LinearLayout (H)",
  LinearLayout::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_LINEAR_LAYOUT).apply {
        width = LinearLayout.LayoutParams.MATCH_PARENT
        layout.orientation = LinearLayout.HORIZONTAL
      }
  }
}

class LinearVPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_linear_layout_vert,
  "LinearLayout (V)",
  LinearLayout::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_LINEAR_LAYOUT).apply {
        width = LinearLayout.LayoutParams.MATCH_PARENT
        layout.orientation = LinearLayout.VERTICAL
      }
  }
}