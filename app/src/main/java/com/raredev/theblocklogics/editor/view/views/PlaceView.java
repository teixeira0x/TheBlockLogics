package com.raredev.theblocklogics.editor.view.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;
import com.raredev.theblocklogics.utils.Constants;

public class PlaceView extends View {

  public PlaceView(Context context) {
    super(context);

    setBackgroundColor(MaterialColors.getColor(this, R.attr.colorOutline));
    setLayoutParams(new ViewGroup.LayoutParams(100, 50));
    setMinimumWidth(Constants.LAYOUT_MIN_SIZE);
    setMinimumHeight(Constants.LAYOUT_MIN_SIZE);
  }

  public void defineStateLayoutParams(Object state) {
    var placeViewParams = getLayoutParams();
    if (state instanceof ViewItem) {
      var draggedView = ((ViewItem) state).getView();
      var draggedViewParams = draggedView.getLayoutParams();

      placeViewParams.width = draggedViewParams.width;
      placeViewParams.height = draggedViewParams.height;

      if (draggedViewParams.width != ViewGroup.LayoutParams.MATCH_PARENT) {
        placeViewParams.width = draggedView.getWidth();
      }

      if (draggedViewParams.height != ViewGroup.LayoutParams.MATCH_PARENT) {
        placeViewParams.height = draggedView.getHeight();
      }
    } else {
      placeViewParams.width = 100;
      placeViewParams.height = 50;
    }
    setLayoutParams(placeViewParams);
  }

  public void removeFromParent() {
    if (getParent() != null) {
      ((ViewGroup) getParent()).removeView(this);
    }
  }
}
