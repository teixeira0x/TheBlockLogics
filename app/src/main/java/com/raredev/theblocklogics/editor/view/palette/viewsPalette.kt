package com.raredev.theblocklogics.editor.view.palette

import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.editor.view.data.ViewData

class TextViewPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_text_view,
  "TextView",
  TextView::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_TEXT_VIEW).apply {
        text.text = name
      }
  }
}

class EditTextPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_edit_text,
  "EditText",
  EditText::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_EDIT_TEXT).apply {
        text.hint = name
      }
  }
}

class ButtonPaletteItem(): PaletteItem(
  R.mipmap.ic_palette_button,
  "Button",
  Button::class.java.name
) {
  override fun createViewData(): ViewData {
    return ViewData(ViewData.TYPE_BUTTON).apply {
        text.text = name
      }
  }
}