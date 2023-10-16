package com.raredev.theblocklogics.editor.view.utils;

import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.ViewEditor;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.data.ViewData.*;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class ViewEditorUtils {

  private static final SparseArray<String> views = new SparseArray<>();

  static {
    views.put(ViewData.TYPE_LINEAR_LAYOUT, "LinearLayout");
    views.put(ViewData.TYPE_FRAME_LAYOUT, "FrameLayout");
    views.put(ViewData.TYPE_RELATIVE_LAYOUT, "RelativeLayout");
    views.put(ViewData.TYPE_VSCROLL_VIEW, "ScrollView");
    views.put(ViewData.TYPE_HSCROLL_VIEW, "HorizontalScrollView");
    views.put(ViewData.TYPE_RADIO_GROUP, "RadioGroup");
    views.put(ViewData.TYPE_TEXT_VIEW, "TextView");
    views.put(ViewData.TYPE_EDIT_TEXT, "EditText");
    views.put(ViewData.TYPE_BUTTON, "Button");
    views.put(ViewData.TYPE_IMAGE_VIEW, "ImageView");
    views.put(ViewData.TYPE_CHECK_BOX, "CheckBox");
    views.put(ViewData.TYPE_RADIO_BUTTON, "RadioButton");
    views.put(ViewData.TYPE_SWITCH, "Switch");
    views.put(ViewData.TYPE_SEEK_BAR, "SeekBar");
    views.put(ViewData.TYPE_PROGRESS_BAR, "ProgressBar");
  }

  public static List<ViewData> getViewsData(ViewEditor editor) {
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

  public static void convertViewDataList(ViewEditor editor, List<ViewData> views) {
    if (views != null && !views.isEmpty()) {
      for (ViewData data : views) {
        // Create ViewItem
        ViewItem view = ViewItemCreator.createView(editor.getContext(), data);

        // Set default listeners
        editor.setListeners(view.getView());

        // Add child to parent
        addView(editor, view);

        // Apply the view's saved properties
        PropertiesApplicator.applyViewProperties(view);
      }
    }
  }

  private static void addView(ViewEditor editor, ViewItem view) {
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
      case ViewData.TYPE_VSCROLL_VIEW:
        return "vscroll";
      case ViewData.TYPE_HSCROLL_VIEW:
        return "hscroll";
      case ViewData.TYPE_FRAME_LAYOUT:
        return "flayout";
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
      case ViewData.TYPE_VSCROLL_VIEW:
        return R.mipmap.ic_palette_scroll_view;
      case ViewData.TYPE_HSCROLL_VIEW:
        return R.mipmap.ic_palette_horizontal_scroll_view;
      case ViewData.TYPE_FRAME_LAYOUT:
        return R.mipmap.ic_palette_frame_layout;
      case ViewData.TYPE_TEXT_VIEW:
        return R.mipmap.ic_palette_text_view;
      case ViewData.TYPE_EDIT_TEXT:
        return R.mipmap.ic_palette_edit_text;
      case ViewData.TYPE_BUTTON:
        return R.mipmap.ic_palette_button;
      default:
        return 0;
    }
  }

  public static String gravityToString(int gravity) {
    int vertical = gravity & Gravity.FILL_VERTICAL;
    int horizontal = gravity & Gravity.FILL_HORIZONTAL;

    var gravityString = new StringBuilder();
    if (horizontal == Gravity.CENTER_HORIZONTAL) {
      gravityString.append("center_horizontal|");
    } else {
      if ((horizontal & Gravity.LEFT) == Gravity.LEFT) {
        gravityString.append("left|");
      }

      if ((horizontal & Gravity.RIGHT) == Gravity.RIGHT) {
        gravityString.append("right|");
      }
    }

    if (vertical == Gravity.CENTER_VERTICAL) {
      gravityString.append("center_vertical|");
    } else {
      if ((vertical & Gravity.TOP) == Gravity.TOP) {
        gravityString.append("top|");
      }

      if ((vertical & Gravity.BOTTOM) == Gravity.BOTTOM) {
        gravityString.append("bottom|");
      }
    }

    if (gravityString.length() > 0) {
      gravityString.deleteCharAt(gravityString.length() - 1);
    } else {
      gravityString.append(Constants.NONE);
    }

    return gravityString.toString();
  }

  public static String layoutParamsToString(int value) {
    if (value == LinearLayout.LayoutParams.MATCH_PARENT) {
      return Constants.MATCH_PARENT;
    }
    if (value == LinearLayout.LayoutParams.WRAP_CONTENT) {
      return Constants.WRAP_CONTENT;
    }
    return value + Constants.DP;
  }

  public static String orientationToString(int orientation) {
    if (orientation == LinearLayout.VERTICAL) {
      return Constants.VERTICAL;
    }
    return Constants.HORIZONTAL;
  }
}
