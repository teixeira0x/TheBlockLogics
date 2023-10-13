package com.raredev.theblocklogics.editor.view;

import android.content.ClipData;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.blankj.utilcode.util.VibrateUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.raredev.theblocklogics.adapters.PropertiesAdapter;
import com.raredev.theblocklogics.databinding.DialogPropertiesBinding;
import com.raredev.theblocklogics.databinding.LayoutToolbarBinding;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.editor.view.dialogs.PropertyDialog;
import com.raredev.theblocklogics.editor.view.dialogs.SizePropertyDialog;
import com.raredev.theblocklogics.editor.view.dialogs.StringPropertyDialog;
import com.raredev.theblocklogics.editor.view.palette.PaletteItem;
import com.raredev.theblocklogics.editor.view.utils.PropertiesApplicator;
import com.raredev.theblocklogics.editor.view.utils.ViewEditorUtils;
import com.raredev.theblocklogics.editor.view.utils.ViewItemCreator;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.PlaceView;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.editor.view.views.layout.LinearLayoutItem;
import com.raredev.theblocklogics.models.Property;
import com.raredev.theblocklogics.utils.Constants;
import java.util.ArrayList;
import java.util.List;

public class ViewEditorLayout extends FrameLayout implements View.OnDragListener, View.OnTouchListener {
  private static final float DEFAULT_EDITOR_SCALE = 0.85f;

  private final Handler handler = new Handler(Looper.getMainLooper());
  
  private LayoutToolbarBinding binding;

  private LinearLayoutItem root;
  private PlaceView placeView;

  private boolean removeDraggedView;
  private boolean dragging;
  private boolean ignoreViewClick;
  private long longPressTime;
  private int calculatedIndex;

  public ViewEditorLayout(Context context) {
    this(context, null);
  }

  public ViewEditorLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public ViewEditorLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    binding = LayoutToolbarBinding.inflate(LayoutInflater.from(getContext()));
    initRootLayout();

    placeView = new PlaceView(context);
    longPressTime =(long) (ViewConfiguration.getLongPressTimeout() / 2);

    setScaleX(DEFAULT_EDITOR_SCALE);
    setScaleY(DEFAULT_EDITOR_SCALE);
  }

  private void initRootLayout() {
    ViewData data = new ViewData(ViewData.TYPE_LINEAR_LAYOUT);
    data.id = Constants.ROOT_ID;
    data.layout.orientation = LinearLayout.VERTICAL;
    data.width = LayoutParams.MATCH_PARENT;
    data.height = LayoutParams.MATCH_PARENT;
    data.setAllPadding(0);

    root = (LinearLayoutItem) ViewItemCreator.createView(getContext(), data).getView();
    root.setOnDragListener(this);

    PropertiesApplicator.applyViewProperties((ViewItem) root);
    addView(root);
  }

  @Override
  public boolean onDrag(View hostView, DragEvent event) {
    var host = (ViewGroup) hostView;
    var state = event.getLocalState();
    switch (event.getAction()) {
      case DragEvent.ACTION_DRAG_STARTED:
        if (!(state instanceof PaletteItem || state instanceof ViewItem)) {
          return false;
        }
        if (state instanceof ViewItem) {
          ((LayoutItem) host).refreshChilds();
        }
        placeView.defineStateLayoutParams(state);
        removeDraggedView = false;
        break;
      case DragEvent.ACTION_DRAG_LOCATION:
      case DragEvent.ACTION_DRAG_ENTERED:
        int index = calculeIndexForEvent(host, event);
        if (index != host.indexOfChild(placeView)) {
          addView(host, placeView, index);
          calculatedIndex = index;
        }
        break;
      case DragEvent.ACTION_DRAG_EXITED:
        placeView.removeFromParent();
        break;
      case DragEvent.ACTION_DRAG_ENDED:
        placeView.removeFromParent();
        if (!(state instanceof ViewItem)) {
          break;
        }
        // Return the view being dragged
        // to the parent if "removeDraggedView" is false
        handler.post(
            () -> {
              if (!removeDraggedView) {
                var draggedView = (ViewItem) state;
                if (draggedView.getView().getParent() == null
                    && draggedView.getViewData().index >= 0) {
                  var draggedViewParent =
                      (LayoutItem) findViewItemById(draggedView.getViewData().parentId);
                  addViewItem(draggedViewParent, draggedView, draggedView.getViewData().index);
                }
              }
              calculatedIndex = -1;
            });
        break;
      case DragEvent.ACTION_DROP:
        placeView.removeFromParent();
        ViewItem view = null;
        if (state instanceof PaletteItem) {
          ViewData viewData = ((PaletteItem) state).createViewData();
          viewData.id = generateId(viewData.type);

          view = ViewItemCreator.createView(getContext(), viewData);
          PropertiesApplicator.applyViewProperties(view);
          setListeners(view.getView());
        } else {
          view = (ViewItem) state;
        }
        addViewItem((LayoutItem) host, view, calculatedIndex);
    }
    return true;
  }

  public void setSelectedLayoutName(String name) {
    binding.layoutName.setText(name);
  }

  public void onSelectedFile(List<ViewData> viewsData) {
    root.removeAllViews();
    root.addView(binding.getRoot());

    ViewEditorUtils.convertViewDataList(this, viewsData);
  }

  public ViewItem findViewItemById(String id) {
    return findViewItemById(this, id);
  }

  public ViewItem findViewItemById(ViewGroup viewGroup, String id) {
    for (int i = 0; i < viewGroup.getChildCount(); i++) {
      var child = viewGroup.getChildAt(i);

      if (child instanceof ViewItem) {
        var editorView = (ViewItem) child;
        if (editorView.getViewData().id.equals(id)) {
          return editorView;
        }
      }

      if (child instanceof LayoutItem) {
        ViewItem v = findViewItemById((ViewGroup) child, id);
        if (v == null) {
          continue;
        }
        if (v.getViewData().id.equals(id)) {
          return v;
        }
      }
    }
    return null;
  }

  public void removeDraggedEditorView() {
    removeDraggedView = true;
  }

  public void setListeners(View view) {
    view.setOnTouchListener(this);
    if (view instanceof ViewGroup) {
      view.setOnDragListener(this);
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        dragging = false;
        handler.postDelayed(
            () -> {
              dragging = true;
              ClipData clipData = ClipData.newPlainText("", "");
              if (ViewCompat.startDragAndDrop(v, clipData, new DragShadowBuilder(v), v, 0)) {
                ((ViewGroup) v.getParent()).removeView(v);
                VibrateUtils.vibrate(100);
              }
            },
            longPressTime);
        break;
      case MotionEvent.ACTION_UP:
        if (!dragging && !ignoreViewClick) {
          showViewProperties((ViewItem) v);
        }
        break;
    }
    return true;
  }

  public void addViewItem(LayoutItem parent, ViewItem view, int index) {
    removeViewFromParent(view.getView());
    ((ViewGroup) parent.getView()).addView(view.getView(), index);
    parent.refreshChilds();
  }

  private void addView(ViewGroup parent, View view, int index) {
    removeViewFromParent(view);
    parent.addView(view, index);
  }

  private void removeViewFromParent(View view) {
    if (view.getParent() != null) {
      ((ViewGroup) view.getParent()).removeView(view);
    }
  }

  private int calculeIndexForEvent(ViewGroup parent, DragEvent event) {
    int index = parent.getChildCount();
    if (parent instanceof LinearLayout) {
      if (((LinearLayout) parent).getOrientation() == LinearLayout.VERTICAL) {
        index = calculeVerticalIndexForEvent(parent, event);
      } else {
        index = calculeHorizontalIndexForEvent(parent, event);
      }
    }
    return Math.min(Math.max(0, index), parent.getChildCount());
  }

  private int calculeHorizontalIndexForEvent(ViewGroup parent, DragEvent event) {
    int index = 0;

    for (int i = 0; i < parent.getChildCount(); i++) {
      View child = parent.getChildAt(i);
      if (child == placeView) continue;

      var centerX = child.getX() + child.getWidth() / 2;
      if (centerX < event.getX()) {
        index++;
      }
    }
    return index;
  }

  private int calculeVerticalIndexForEvent(ViewGroup parent, DragEvent event) {
    int index = 0;

    for (int i = 0; i < parent.getChildCount(); i++) {
      View child = parent.getChildAt(i);
      if (child == placeView) continue;

      var centerY = child.getY() + child.getHeight() / 2;
      if (centerY < event.getY() || child == binding.getRoot()) {
        index++;
      }
    }
    return index;
  }

  private String generateId(int type) {
    var baseId = ViewEditorUtils.getIdForType(type);

    int count = 1;
    var id = baseId;
    while (ViewEditorUtils.isIdExists(this, id)) {
      id = baseId + count;
      count++;
    }
    return id;
  }

  private void showViewProperties(ViewItem view) {
    var propertiesDialog = new BottomSheetDialog(getContext());
    var layoutInflater = LayoutInflater.from(getContext());
    var binding = DialogPropertiesBinding.inflate(layoutInflater);
    binding.viewIcon.setImageResource(ViewEditorUtils.getIconForType(view.getViewData().type));
    binding.viewId.setText(view.getViewData().id);

    var adapter = new PropertiesAdapter(layoutInflater);
    adapter.setProperties(getProperties(view.getViewData()));
    adapter.setPropertyListener(
        (property) -> {
          onPropertyClick(view, property);
          propertiesDialog.cancel();
        });

    binding.rvProperties.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.rvProperties.setAdapter(adapter);

    propertiesDialog.setOnCancelListener((unused) -> ignoreViewClick = false);
    propertiesDialog.setContentView(binding.getRoot());
    propertiesDialog.show();
    ignoreViewClick = true;
  }

  private List<Property> getProperties(ViewData viewData) {
    List<Property> properties = new ArrayList<>();

    properties.add(new Property(Property.TYPE_ID, Constants.ID, viewData.id));
    properties.add(
        new Property(
            Property.TYPE_WIDTH,
            Constants.WIDTH,
            ViewEditorUtils.getLayoutParamsString(viewData.width)));
    properties.add(
        new Property(
            Property.TYPE_HEIGHT,
            Constants.HEIGHT,
            ViewEditorUtils.getLayoutParamsString(viewData.height)));

    switch (viewData.type) {
      case ViewData.TYPE_EDIT_TEXT:
        properties.add(new Property(Property.TYPE_HINT, Constants.HINT, viewData.text.hint));
      case ViewData.TYPE_TEXT_VIEW:
      case ViewData.TYPE_BUTTON:
        properties.add(new Property(Property.TYPE_TEXT, Constants.TEXT, viewData.text.text));
        break;
    }

    return properties;
  }

  private void onPropertyClick(ViewItem viewItem, Property property) {
    PropertyDialog dialog = null;
    switch (property.getType()) {
      case Property.TYPE_ID:
      case Property.TYPE_TEXT:
      case Property.TYPE_HINT:
        dialog = new StringPropertyDialog(getContext(), this, viewItem, property);
        break;
      case Property.TYPE_WIDTH:
      case Property.TYPE_HEIGHT:
        dialog = new SizePropertyDialog(getContext(), viewItem, property);
        break;
    }
  }
}
