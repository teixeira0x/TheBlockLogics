package com.raredev.theblocklogics.editor.view.views.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.R.attr;
import com.google.android.material.color.MaterialColors;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.utils.Constants;

public class FrameLayoutItem extends FrameLayout implements LayoutItem {

  private final Rect rect = new Rect();
  private Paint paint;

  private ViewData data;

  public FrameLayoutItem(Context context, ViewData data) {
    super(context);
    this.data = data;
    setMinimumWidth(Constants.LAYOUT_MIN_SIZE);
    setMinimumHeight(Constants.LAYOUT_MIN_SIZE);

    setWillNotDraw(false);

    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setColor(MaterialColors.getColor(this, attr.colorOutline));
    paint.setStyle(Paint.Style.STROKE);
    paint.setStrokeWidth(2);
  }

  @Override
  protected void onDraw(Canvas canvas) {
    rect.set(0, 0, getMeasuredWidth(), getMeasuredHeight());
    canvas.drawRect(rect, paint);
    super.onDraw(canvas);
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
  public void rebuildChildren() {
    for (int i = 0; i < getChildCount(); i++) {
      var child = getChildAt(i);
      if (child instanceof ViewItem) {
        ((ViewItem) child).getViewData().index = i;
        ((ViewItem) child).getViewData().parentId = data.id;
        ((ViewItem) child).getViewData().parentType = data.type;
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
