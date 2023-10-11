package com.raredev.theblocklogics.editor.view.views.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.R.attr;
import com.google.android.material.color.MaterialColors;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.utils.Constants;

public class LinearLayoutItem extends LinearLayout implements LayoutItem {

  private ViewData data;
  private Paint paint;

  public LinearLayoutItem(Context context, ViewData data) {
    super(context);
    this.data = data;
    setMinimumWidth(Constants.LAYOUT_MIN_SIZE);
    setMinimumHeight(Constants.LAYOUT_MIN_SIZE);

    paint = new Paint();
    paint.setColor(MaterialColors.getColor(this, attr.colorOutline));
    paint.setStyle(Paint.Style.STROKE);
    paint.setAntiAlias(true);
    paint.setStrokeWidth(2);
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
  protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    // TODO: Implement this meth
    canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
  }
  

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
  }

  @Override
  public void refreshChilds() {
    for (int i = 0; i < getChildCount(); i++) {
      var child = getChildAt(i);
      if (child instanceof ViewItem) {
        ((ViewItem) child).getViewData().index = i;
        ((ViewItem) child).getViewData().parentId = data.id;
      }
    }
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
