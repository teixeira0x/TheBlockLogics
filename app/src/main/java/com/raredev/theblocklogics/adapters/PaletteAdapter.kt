package com.raredev.theblocklogics.adapters

import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.VibrateUtils
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.databinding.LayoutPalleteItemBinding
import com.raredev.theblocklogics.editor.view.palette.ButtonPaletteItem
import com.raredev.theblocklogics.editor.view.palette.CategoryPaletteItem
import com.raredev.theblocklogics.editor.view.palette.EditTextPaletteItem
import com.raredev.theblocklogics.editor.view.palette.LinearHPaletteItem
import com.raredev.theblocklogics.editor.view.palette.LinearVPaletteItem
import com.raredev.theblocklogics.editor.view.palette.PaletteItem
import com.raredev.theblocklogics.editor.view.palette.TextViewPaletteItem

public class PaletteAdapter(
  val drawerLayout: DrawerLayout,
  val layoutInflater: LayoutInflater
): RecyclerView.Adapter<PaletteAdapter.VH>() {

  private var palette: MutableList<PaletteItem> = ArrayList()

  class VH(internal val binding: LayoutPalleteItemBinding):
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH{
    return VH(LayoutPalleteItemBinding.inflate(layoutInflater, parent, false))
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.binding.apply {
      var item = palette[position]
      if (item is CategoryPaletteItem) {
        dragView.visibility = View.GONE
        category.visibility = View.VISIBLE
        category.setText(item.name)
        return
      }
      category.visibility = View.GONE

      icon.setImageResource(item.icon)
      name.setText(item.name)
      widgetClass.setText(item.className)

      root.setOnLongClickListener {
        if (ViewCompat.startDragAndDrop(dragView, ClipData.newPlainText("", ""), View.DragShadowBuilder(dragView), item, 0)) {
          drawerLayout.closeDrawer(GravityCompat.START)
          VibrateUtils.vibrate(100)
          true
        }
        false
      }
    }
  }

  override fun getItemCount(): Int  {
    return palette.size
  }

  fun clearPalette() {
    palette.clear()
  }

  fun ensurePalette() {
    palette.add(CategoryPaletteItem(StringUtils.getString(R.string.category_layouts)))
    palette.add(LinearHPaletteItem())
    palette.add(LinearVPaletteItem())

    palette.add(CategoryPaletteItem(StringUtils.getString(R.string.category_widgets)))
    palette.add(TextViewPaletteItem())
    palette.add(EditTextPaletteItem())
    palette.add(ButtonPaletteItem())

    notifyDataSetChanged()
  }
}
