package com.raredev.theblocklogics.editor.view.views;

import android.view.View;
import com.raredev.theblocklogics.editor.view.data.ViewData;

public interface ViewItem {

  void setViewData(ViewData data);

  ViewData getViewData();

  View getView();
}
