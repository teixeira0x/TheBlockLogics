package com.raredev.theblocklogics.editor.view

import android.content.Context
import android.content.ClipData
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.VibrateUtils

public class WidgetTouchListener(
  val editor: ViewEditor,
  val onViewClick: (View) -> Boolean = { false }
): View.OnTouchListener {

  private var touchedView: View? = null

  private val gestureDetector by lazy {
    GestureDetector(editor.getContext(),
      object : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
          return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {  
          return onViewClick(touchedView!!)
        }

        override fun onLongPress(e: MotionEvent) {
          if (ViewCompat.startDragAndDrop(touchedView!!, ClipData.newPlainText("", ""), View.DragShadowBuilder(touchedView!!), touchedView!!, 0)) {
            editor.removeViewFromParent(touchedView!!)
            VibrateUtils.vibrate(100)
          }
        }
      }
    )
  }

  override fun onTouch(v: View, event: MotionEvent): Boolean {
    if (event.action == MotionEvent.ACTION_DOWN) {
      this.touchedView = v
    }
    return gestureDetector.onTouchEvent(event)
  }
}
