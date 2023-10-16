package com.raredev.theblocklogics.editor.view.palette

import android.widget.ScrollView
import android.widget.HorizontalScrollView
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
        height = LinearLayout.LayoutParams.MATCH_PARENT
        layout.orientation = LinearLayout.VERTICAL
      }
  }
}

class HScrollViewPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_scroll_view,
  "ScrollView (H)",
  HorizontalScrollView::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_HSCROLL_VIEW).apply {
        width = LinearLayout.LayoutParams.MATCH_PARENT
      }
  }
}

class ScrollViewPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_scroll_view,
  "ScrollView (V)",
  ScrollView::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_VSCROLL_VIEW).apply {
        height = LinearLayout.LayoutParams.MATCH_PARENT
      }
  }
}

class FrameLayoutPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_frame_layout,
  "FrameLayout",
  ScrollView::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_FRAME_LAYOUT).apply {
        width = LinearLayout.LayoutParams.MATCH_PARENT
      }
  }
}