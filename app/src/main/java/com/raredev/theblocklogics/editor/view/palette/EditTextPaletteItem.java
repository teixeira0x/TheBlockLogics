package com.raredev.theblocklogics.editor.view.palette;

import android.widget.EditText;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.utils.Constants;

public class EditTextPaletteItem extends PaletteItem {

  public EditTextPaletteItem() {
    super(R.mipmap.ic_palette_edit_text, Constants.EDIT_TEXT);
  }

  @Override
  public String getViewClassName() {
    return EditText.class.getName();
  }

  @Override
  public ViewData createViewData() {
    ViewData viewData = new ViewData(ViewData.TYPE_EDIT_TEXT);
    viewData.text.hint = Constants.EDIT_TEXT;
    return viewData;
  }
}
