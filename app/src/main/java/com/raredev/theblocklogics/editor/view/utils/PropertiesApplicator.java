package com.raredev.theblocklogics.editor.view.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.SizeUtils;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.views.ViewItem;

public class PropertiesApplicator {

  public static void applyViewProperties(ViewItem viewItem) {
    var viewData = viewItem.getViewData();
    var view = viewItem.getView();

    switch (viewData.type) {
      case ViewData.TYPE_LINEAR_LAYOUT:
        applyLayoutProperties(viewData, (LinearLayout) view);
        break;
      case ViewData.TYPE_TEXT_VIEW:
      case ViewData.TYPE_EDIT_TEXT:
      case ViewData.TYPE_BUTTON:
        applyTextProperties(viewData, (TextView) view);
        break;
    }

    applyLayoutParamsProperties(viewData, view);
    applyPadding(viewData, view);
  }

  public static void applyLayoutProperties(ViewData viewData, LinearLayout layout) {
    layout.setOrientation(viewData.layout.orientation);
  }

  public static void applyTextProperties(ViewData viewData, TextView textView) {
    textView.setText(viewData.text.text);
    textView.setTextSize(viewData.text.textSize);

    if (textView instanceof EditText) {
      applyEditTextProperties(viewData, (EditText) textView);
    }
  }

  public static void applyEditTextProperties(ViewData viewData, EditText editText) {
    editText.setHint(viewData.text.hint);
  }

  public static void applyLayoutParamsProperties(ViewData viewData, View view) {
    int width = resolveParam(viewData.width);
    int height = resolveParam(viewData.height);

    ViewGroup.LayoutParams params = view.getLayoutParams();
    if (params == null) {
      params = new LinearLayout.LayoutParams(width, height);
    } else {
      params.width = width;
      params.height = height;
    }

    view.setLayoutParams(params);
    view.requestLayout();
  }

  public static void applyPadding(ViewData viewData, View view) {
    view.setPadding(
        viewData.paddingLeft, viewData.paddingTop, viewData.paddingRight, viewData.paddingBottom);
  }

  public static int resolveParam(int value) {
    if (value == ViewGroup.LayoutParams.MATCH_PARENT
        || value == ViewGroup.LayoutParams.WRAP_CONTENT) {
      return value;
    }
    return SizeUtils.dp2px(value);
  }
}
