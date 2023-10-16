package com.raredev.theblocklogics.editor.view.utils;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
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
    layout.setGravity(viewData.layout.gravity);
  }

  public static void applyTextProperties(ViewData viewData, TextView textView) {
    textView.setText(viewData.text.text);
    textView.setTextSize(viewData.text.textSize);
    if (viewData.layout.gravity != 0) {
      textView.setGravity(viewData.layout.gravity);
    } else {
      textView.setGravity(Gravity.CENTER);
    }

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
    params.width = width;
    params.height = height;

    if (params instanceof LinearLayout.LayoutParams ) {
      ((LinearLayout.LayoutParams) params).gravity = viewData.layout.layoutGravity;
    } else if (params instanceof FrameLayout.LayoutParams) {
      ((FrameLayout.LayoutParams) params).gravity = viewData.layout.layoutGravity;
    }

    view.setLayoutParams(params);
    view.requestLayout();
  }

  public static ViewGroup.LayoutParams createLayoutParams(int type, int width, int height) {
    switch (type) {
      case ViewData.TYPE_LINEAR_LAYOUT:
      case ViewData.TYPE_VSCROLL_VIEW:
      case ViewData.TYPE_HSCROLL_VIEW:
        return new LinearLayout.LayoutParams(width, height);
      case ViewData.TYPE_FRAME_LAYOUT:
        return new FrameLayout.LayoutParams(width, height);
    }
    return null;
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
