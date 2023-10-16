package com.raredev.theblocklogics.editor.view

import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.blankj.utilcode.util.ThreadUtils
import com.raredev.theblocklogics.editor.view.data.ViewData
import com.raredev.theblocklogics.editor.view.palette.PaletteItem
import com.raredev.theblocklogics.editor.view.utils.PropertiesApplicator
import com.raredev.theblocklogics.editor.view.utils.ViewEditorUtils
import com.raredev.theblocklogics.editor.view.utils.ViewItemCreator
import com.raredev.theblocklogics.editor.view.views.LayoutItem
import com.raredev.theblocklogics.editor.view.views.PlaceView
import com.raredev.theblocklogics.editor.view.views.ViewItem
import kotlin.math.min
import kotlin.math.max

class WidgetDragListener(
  private val editor: ViewEditor,
  private val placeView: PlaceView
): View.OnDragListener {

  private var calculatedIndex = -1
  private var removeDraggedView = false

  override fun onDrag(hostView: View, event: DragEvent): Boolean {
    val host = hostView as ViewGroup
    val state = event.localState
    when (event.action) {
      DragEvent.ACTION_DRAG_STARTED -> {
        if (state !is PaletteItem && state !is ViewItem) {
          throw IllegalStateException("Invalid state")
        }
        if (state is ViewItem && host is LayoutItem) {
          host.rebuildChildren()
        }
        removeDraggedView = false
        return true
      }
      DragEvent.ACTION_DRAG_ENTERED,
      DragEvent.ACTION_DRAG_LOCATION -> {
        if (placeView.parent != null) {
          val placeIndex = host.indexOfChild(placeView)
          if (host is LinearLayout) {
            calculatedIndex = calculeIndexForEvent(host, event)
          } else {
            calculatedIndex = placeIndex
          }

          if (placeIndex != calculatedIndex) {
            editor.addView(host, placeView, calculatedIndex)
          }
        } else {
          host.addView(placeView)
        }
        return true
      }
      DragEvent.ACTION_DRAG_EXITED -> {
        placeView.removeFromParent()
        return true
      }
      DragEvent.ACTION_DRAG_ENDED -> {
        placeView.removeFromParent()
        calculatedIndex = -1
        if (state !is ViewItem) {
          return false
        }
        // Return the view being dragged
        // to the parent if "removeDraggedView" is false
        ThreadUtils.runOnUiThread {
          if (!removeDraggedView) {
            if (state.view.parent == null && state.viewData.index >= 0) {
              val viewParent = editor.findViewItemById(state.viewData.parentId) as LayoutItem
              editor.addViewItem(viewParent, state, state.viewData.index)
            }
          }
        }
        return true
      }
      DragEvent.ACTION_DROP -> {
        placeView.removeFromParent()
        val view: ViewItem
        if (state is PaletteItem) {
          val viewData = state.createViewData()
          viewData!!.id = editor.generateId(viewData.type)

          view = ViewItemCreator.createView(editor.getContext(), viewData)
          editor.setListeners(view.view)
        } else {
          view = state as ViewItem
        }
        editor.addViewItem(host as LayoutItem, view, calculatedIndex)
        // Update properties.
        PropertiesApplicator.applyViewProperties(view)
        return true
    }
      else -> return false
    }
  }

  fun removeDraggedView() {
    removeDraggedView = true
  }

  private fun calculeIndexForEvent(parent: ViewGroup, event: DragEvent): Int {
    val index: Int
    if (parent is LinearLayout) {
      if (parent.orientation == LinearLayout.VERTICAL) {
        index = calculeVerticalIndexForEvent(parent, event)
      } else {
        index = calculeHorizontalIndexForEvent(parent, event)
      }
    } else {
      index = parent.childCount
    }
    return min(max(0, index), parent.childCount)
  }

  private fun calculeHorizontalIndexForEvent(parent: ViewGroup, event: DragEvent): Int {
    var index = 0

    for (i in 0 until parent.childCount) {
      val child = parent.getChildAt(i)
      if (child === placeView) continue

      val centerX = child.x + child.width / 2
      if (centerX < event.x) {
        index++
      }
    }
    return index
  }

  private fun calculeVerticalIndexForEvent(parent: ViewGroup, event: DragEvent): Int {
    var index = 0

    for (i in 0 until parent.childCount) {
      val child = parent.getChildAt(i)
      if (child === placeView) continue

      val centerY = child.y + child.height / 2
      if (centerY < event.y) {
        index++
      }
    }
    return index
  }
}