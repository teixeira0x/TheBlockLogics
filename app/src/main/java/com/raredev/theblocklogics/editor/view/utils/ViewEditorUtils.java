package com.raredev.theblocklogics.editor.view.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.ViewEditorLayout;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.data.ViewData.*;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class ViewEditorUtils {

  static BiMap<Integer, String> views =
      new ImmutableBiMap.Builder<Integer, String>()
          .put(ViewData.TYPE_LINEAR_LAYOUT, "LinearLayout")
          .put(ViewData.TYPE_FRAME_LAYOUT, "FrameLayout")
          .put(ViewData.TYPE_RELATIVE_LAYOUT, "RelativeLayout")
          .put(ViewData.TYPE_VSCROLL_VIEW, "ScrollView")
          .put(ViewData.TYPE_HSCROLL_VIEW, "HorizontalScrollView")
          .put(ViewData.TYPE_RADIO_GROUP, "RadioGroup")
          .put(ViewData.TYPE_TEXT_VIEW, "TextView")
          .put(ViewData.TYPE_EDIT_TEXT, "EditText")
          .put(ViewData.TYPE_BUTTON, "Button")
          .put(ViewData.TYPE_IMAGE_VIEW, "ImageView")
          .put(ViewData.TYPE_CHECK_BOX, "CheckBox")
          .put(ViewData.TYPE_RADIO_BUTTON, "RadioButton")
          .put(ViewData.TYPE_SWITCH, "Switch")
          .put(ViewData.TYPE_SEEK_BAR, "SeekBar")
          .put(ViewData.TYPE_PROGRESS_BAR, "ProgressBar")
          .build();

  public static List<ViewData> getViewsData(ViewEditorLayout editor) {
    List<ViewData> viewsData = new ArrayList<>();

    listViewsData(viewsData, (LayoutItem) editor.getChildAt(0));

    return viewsData;
  }

  private static void listViewsData(List<ViewData> viewsData, LayoutItem layout) {
    for (int i = 0; i < ((ViewGroup) layout.getView()).getChildCount(); i++) {
      View child = ((ViewGroup) layout.getView()).getChildAt(i);

      if (child instanceof ViewItem) {
        ViewItem view = (ViewItem) child;
        viewsData.add(view.getViewData());
      }

      if (child instanceof LayoutItem) {
        listViewsData(viewsData, (LayoutItem) child);
      }
    }
  }

  public static void convertViewDataList(ViewEditorLayout editor, List<ViewData> views) {
    for (ViewData data : views) {
      // Create ViewItem
      ViewItem view = ViewItemCreator.createView(editor.getContext(), data);

      // Apply the view's saved properties
      PropertiesApplicator.applyViewProperties(view);

      // Set default listeners
      editor.setListeners(view.getView());

      // Add child to parent
      addView(editor, view);
    }
  }

  private static void addView(ViewEditorLayout editor, ViewItem view) {
    ViewItem parent = editor.findViewItemById(view.getViewData().parentId);
    if (parent != null) {
      editor.addViewItem((LayoutItem) parent, view, view.getViewData().index);

      if (parent.getView().getParent() == null) {
        addView(editor, parent);
      }
    }
  }

  public static boolean isIdExists(ViewGroup viewGroup, String id) {
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      View child = viewGroup.getChildAt(i);

      if (child instanceof ViewItem) {
        String childId = ((ViewItem) child).getViewData().id;

        if (childId.equals(id)) {
          return true;
        }
      }

      if (child instanceof LayoutItem) {
        if (isIdExists((ViewGroup) child, id)) {
          return true;
        }
      }
    }
    return false;
  }

  public static String getTagForType(int type) {
    return views.get(type);
  }

  public static String getIdForType(int type) {
    switch (type) {
      case ViewData.TYPE_LINEAR_LAYOUT:
        return "linear";
      case ViewData.TYPE_TEXT_VIEW:
        return "textview";
      case ViewData.TYPE_EDIT_TEXT:
        return "edittext";
      case ViewData.TYPE_BUTTON:
        return "button";
      default:
        return null;
    }
  }

  public static int getIconForType(int type) {
    switch (type) {
      case ViewData.TYPE_LINEAR_LAYOUT:
        return R.mipmap.ic_palette_linear_layout_vert;
      case ViewData.TYPE_TEXT_VIEW:
        return R.mipmap.ic_palette_text_view;
      case ViewData.TYPE_EDIT_TEXT:
        return R.mipmap.ic_palette_edit_text;
      case ViewData.TYPE_BUTTON:
        return R.mipmap.ic_palette_button;
      default:
        return -1;
    }
  }

  public static String getLayoutParamsString(int value) {
    if (value == LinearLayout.LayoutParams.MATCH_PARENT) {
      return Constants.MATCH_PARENT;
    }
    if (value == LinearLayout.LayoutParams.WRAP_CONTENT) {
      return Constants.WRAP_CONTENT;
    }
    return value + Constants.DP;
  }

  public static String getOrientationString(int orientation) {
    if (orientation == LinearLayout.VERTICAL) {
      return Constants.VERTICAL;
    }
    return Constants.HORIZONTAL;
  }
}
