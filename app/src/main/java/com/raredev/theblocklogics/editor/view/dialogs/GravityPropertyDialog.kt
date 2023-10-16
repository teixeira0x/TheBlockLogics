package com.raredev.theblocklogics.editor.view.dialogs

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.databinding.DialogGravityBinding
import com.raredev.theblocklogics.editor.view.utils.PropertiesApplicator
import com.raredev.theblocklogics.editor.view.views.LayoutItem
import com.raredev.theblocklogics.editor.view.views.ViewItem
import com.raredev.theblocklogics.models.Property
import com.raredev.theblocklogics.utils.Constants

class GravityPropertyDialog(
  val context: Context,
  val viewItem: ViewItem,
  val property: Property
): PropertyDialog(context, viewItem, property) {

  private val binding = DialogGravityBinding.inflate(LayoutInflater.from(context))

  init {
    setView(binding.root, 20, 0, 20, 0)
    show()

    val savedValue = getSavedValue()

    val vertical = savedValue and Gravity.FILL_VERTICAL
    val horizontal = savedValue and Gravity.FILL_HORIZONTAL
    binding.hcenter.isChecked = horizontal == Gravity.CENTER_HORIZONTAL
    binding.left.isChecked = (horizontal and Gravity.LEFT) == Gravity.LEFT
    binding.right.isChecked = (horizontal and Gravity.RIGHT) == Gravity.RIGHT

    binding.vcenter.isChecked = vertical == Gravity.CENTER_VERTICAL
    binding.top.isChecked = (vertical and Gravity.TOP) == Gravity.TOP
    binding.bottom.isChecked = (vertical and Gravity.BOTTOM) == Gravity.BOTTOM
  }

  fun getSavedValue(): Int {
    return when {
      property.type == Property.TYPE_GRAVITY -> viewItem.viewData.layout.gravity
      else -> viewItem.viewData.layout.layoutGravity
    }
  }

  override fun onSaveValue() {
    val left = binding.left
    val right = binding.right
    val hcenter = binding.hcenter
    val top = binding.top
    val bottom = binding.bottom
    val vcenter = binding.vcenter

    var value = Gravity.NO_GRAVITY

    if (left.isChecked) {
      value = value or Gravity.LEFT
    }

    if (right.isChecked) {
      value = value or Gravity.RIGHT
    }

    if (hcenter.isChecked) {
      value = value or Gravity.CENTER_HORIZONTAL
    }

    if (top.isChecked) {
      value = value or Gravity.TOP
    }

    if (bottom.isChecked) {
      value = value or Gravity.BOTTOM
    }

    if (vcenter.isChecked) {
      value = value or Gravity.CENTER_VERTICAL
    }

    if (property.type == Property.TYPE_GRAVITY) {
      viewItem.viewData.layout.gravity = value
    } else {
      viewItem.viewData.layout.layoutGravity = value
    }
    PropertiesApplicator.applyViewProperties(viewItem)
  }
}
