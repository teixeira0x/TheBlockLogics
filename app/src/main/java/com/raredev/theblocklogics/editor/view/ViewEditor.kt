package com.raredev.theblocklogics.editor.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.raredev.theblocklogics.adapters.PropertiesAdapter
import com.raredev.theblocklogics.databinding.DialogPropertiesBinding
import com.raredev.theblocklogics.editor.view.data.ViewData
import com.raredev.theblocklogics.editor.view.dialogs.*
import com.raredev.theblocklogics.editor.view.utils.PropertiesApplicator
import com.raredev.theblocklogics.editor.view.utils.ViewEditorUtils
import com.raredev.theblocklogics.editor.view.utils.ViewItemCreator
import com.raredev.theblocklogics.editor.view.views.LayoutItem
import com.raredev.theblocklogics.editor.view.views.PlaceView
import com.raredev.theblocklogics.editor.view.views.ViewItem
import com.raredev.theblocklogics.editor.view.views.layout.LinearLayoutItem
import com.raredev.theblocklogics.models.Property
import com.raredev.theblocklogics.utils.Constants

class ViewEditor: FrameLayout {

  constructor(context: Context) : super(context)
  constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  private val placeView: PlaceView
  private val touchListener: WidgetTouchListener
  private val dragListener: WidgetDragListener
  private lateinit var root: LinearLayoutItem
  private var ignoreClick: Boolean = false

  init {
    placeView = PlaceView(context)
    touchListener = WidgetTouchListener(this, this::onViewClick)
    dragListener = WidgetDragListener(this, placeView)
    createRoot()
  }

  fun createRoot() {
    val data = ViewData(ViewData.TYPE_LINEAR_LAYOUT)
    data.id = Constants.ROOT_ID
    data.width = LayoutParams.MATCH_PARENT
    data.height = LayoutParams.MATCH_PARENT
    data.layout.orientation = LinearLayout.VERTICAL
    data.setPadding(0)

    root = ViewItemCreator.createView(getContext(), data) as LinearLayoutItem
    root.setOnDragListener(dragListener)
    addView(root)

    root.scaleX = DEFAULT_SCALE
    root.scaleY = DEFAULT_SCALE

    PropertiesApplicator.applyViewProperties(root as ViewItem)
  }

  fun onChangeSelectedFile(viewsData: List<ViewData>) {
    root.removeAllViews()

    ViewEditorUtils.convertViewDataList(this, viewsData)
  }

  fun removeDraggedView() {
    dragListener.removeDraggedView()
  }

  fun setListeners(view: View) {
    view.setOnTouchListener(touchListener)
    if (view is LayoutItem) {
      view.setOnDragListener(dragListener)
    }
  }

  fun findViewItemById(id: String): ViewItem? {
    return findViewItemById(this, id)
  }

  fun findViewItemById(viewGroup: ViewGroup, id: String): ViewItem? {
    for (i in 0 until viewGroup.childCount) {
      val child = viewGroup.getChildAt(i);

      if (child is ViewItem) {
        if (child.viewData.id.equals(id)) {
          return child
        }
      }

      if (child is LayoutItem) {
        val v = findViewItemById(child as ViewGroup, id)
        if (v == null) {
          continue
        }
        if (v.viewData.id.equals(id)) {
          return v
        }
      }
    }
    return null
  }

  fun addViewItem(parent: LayoutItem, view: ViewItem, index: Int) {
    addView(parent as ViewGroup, view.view, index)
    parent.rebuildChildren()
  }

  fun addView(parent: ViewGroup, view: View, index: Int) {
    removeViewFromParent(view)
    parent.addView(view, index)
  }

  fun removeViewFromParent(view: View) {
    (view.parent as? ViewGroup)?.removeView(view)
  }

  fun generateId(type: Int): String {
    val baseId = ViewEditorUtils.getIdForType(type)

    var count = 1
    var id = baseId
    while (ViewEditorUtils.isIdExists(this, id)) {
      id = baseId + count
      count++
    }
    return id
  }

  fun onViewClick(view: View): Boolean {
    if (!ignoreClick) {
      showViewProperties(view as ViewItem)
    }
    return true
  }

  private fun showViewProperties(view: ViewItem) {
    val dialog = BottomSheetDialog(getContext())
    val layoutInflater = LayoutInflater.from(getContext())
    val binding = DialogPropertiesBinding.inflate(layoutInflater)
    binding.viewIcon.setImageResource(ViewEditorUtils.getIconForType(view.viewData.type))
    binding.viewName.setText(ViewEditorUtils.getTagForType(view.viewData.type))
    binding.viewId.setText(view.viewData.id)

    val adapter = PropertiesAdapter(layoutInflater)
    adapter.setProperties(getProperties(view.viewData))
    adapter.setPropertyListener {
      onPropertyClick(view, it)
      dialog.cancel()
    }

    binding.rvProperties.adapter = adapter

    dialog.setOnCancelListener { ignoreClick = false }
    dialog.setContentView(binding.root)
    dialog.show()
    ignoreClick = true
  }

  private fun getProperties(viewData: ViewData): List<Property> {
    val properties: MutableList<Property> = ArrayList()
    addProperty(properties, Property.TYPE_ID, Constants.PROPERTY_ID, viewData.id)
    addProperty(properties, Property.TYPE_WIDTH, Constants.PROPERTY_WIDTH, ViewEditorUtils.layoutParamsToString(viewData.width))
    addProperty(properties, Property.TYPE_HEIGHT, Constants.PROPERTY_HEIGHT, ViewEditorUtils.layoutParamsToString(viewData.height))

    addProperty(properties, Property.TYPE_LAYOUT_GRAVITY, Constants.PROPERTY_LAYOUT_GRAVITY, ViewEditorUtils.gravityToString(viewData.layout.layoutGravity))

    when (viewData.type) {
      ViewData.TYPE_LINEAR_LAYOUT, ViewData.TYPE_FRAME_LAYOUT -> {
        addProperty(properties, Property.TYPE_GRAVITY, Constants.PROPERTY_GRAVITY, ViewEditorUtils.gravityToString(viewData.layout.gravity))
      }
      ViewData.TYPE_EDIT_TEXT -> {
        addProperty(properties, Property.TYPE_GRAVITY, Constants.PROPERTY_GRAVITY, ViewEditorUtils.gravityToString(viewData.layout.gravity))
        addProperty(properties, Property.TYPE_TEXT, Constants.PROPERTY_TEXT, viewData.text.text)
        addProperty(properties, Property.TYPE_HINT, Constants.PROPERTY_HINT, viewData.text.hint)
      }
      ViewData.TYPE_TEXT_VIEW, ViewData.TYPE_BUTTON -> {
        addProperty(properties, Property.TYPE_GRAVITY, Constants.PROPERTY_GRAVITY, ViewEditorUtils.gravityToString(viewData.layout.gravity))
        addProperty(properties, Property.TYPE_TEXT, Constants.PROPERTY_TEXT, viewData.text.text)
      }
    }
    return properties
  }

  private fun addProperty(properties: MutableList<Property>, type: Int, key: String, value: String?) {
    properties.add(Property(type, key, value ?: Constants.NONE))
  }

  private fun onPropertyClick(viewItem: ViewItem, property: Property) {
    var dialog: PropertyDialog? = when (property.type) {
      Property.TYPE_ID,
      Property.TYPE_TEXT,
      Property.TYPE_HINT -> {
        StringPropertyDialog(getContext(), this, viewItem, property)
      }
      Property.TYPE_WIDTH,
      Property.TYPE_HEIGHT -> {
        SizePropertyDialog(getContext(), viewItem, property)
      }
      Property.TYPE_GRAVITY,
      Property.TYPE_LAYOUT_GRAVITY -> {
        GravityPropertyDialog(getContext(), viewItem, property)
      }
      else -> null
    }
  }

  companion object {
    const val DEFAULT_SCALE = 0.85f
  }
}
