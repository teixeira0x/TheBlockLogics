package com.raredev.theblocklogics.editor.view.views.text;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import com.blankj.utilcode.util.SizeUtils;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.views.ViewItem;

public class ButtonItem extends Button implements ViewItem {

  private ViewData data;

  public ButtonItem(Context context, ViewData data) {
    super(context);
    this.data = data;
    setFocusable(false);
  }

  @Override
  public void setPadding(int left, int top, int right, int bottom) {
    super.setPadding(
        SizeUtils.dp2px(left),
        SizeUtils.dp2px(top),
        SizeUtils.dp2px(right),
        SizeUtils.dp2px(bottom));
  }

  @Override
  public void setViewData(ViewData data) {
    this.data = data;
  }

  @Override
  public ViewData getViewData() {
    return data;
  }

  @Override
  public View getView() {
    return this;
  }
}
