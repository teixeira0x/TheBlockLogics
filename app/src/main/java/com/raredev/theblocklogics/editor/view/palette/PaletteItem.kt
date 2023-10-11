package com.raredev.theblocklogics.editor.view.palette

import com.raredev.theblocklogics.editor.view.data.ViewData

abstract class PaletteItem(
  val icon: Int,
  val name: String,
  val className: String? = null
) {
  abstract fun createViewData(): ViewData?
}

class CategoryPaletteItem(
  var text: String
): PaletteItem(-1, text) {
  override fun createViewData(): ViewData? = null
}