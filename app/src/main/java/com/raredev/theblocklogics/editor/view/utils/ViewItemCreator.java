package com.raredev.theblocklogics.editor.view.utils;

import android.content.Context;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.editor.view.views.layout.LinearLayoutItem;
import com.raredev.theblocklogics.editor.view.views.text.ButtonItem;
import com.raredev.theblocklogics.editor.view.views.text.EditTextItem;
import com.raredev.theblocklogics.editor.view.views.text.TextViewItem;

public class ViewItemCreator {

  public static ViewItem createView(Context context, ViewData viewData) {
    switch (viewData.type) {
      case ViewData.TYPE_LINEAR_LAYOUT:
        return new LinearLayoutItem(context, viewData);
      case ViewData.TYPE_TEXT_VIEW:
        return new TextViewItem(context, viewData);
      case ViewData.TYPE_EDIT_TEXT:
        return new EditTextItem(context, viewData);
      case ViewData.TYPE_BUTTON:
        return new ButtonItem(context, viewData);

      default:
        return null;
    }
  }
}
