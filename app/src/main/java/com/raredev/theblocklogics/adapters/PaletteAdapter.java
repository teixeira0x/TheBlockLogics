package com.raredev.theblocklogics.adapters;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.VibrateUtils;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.LayoutPalleteItemBinding;
import com.raredev.theblocklogics.editor.view.palette.ButtonPaletteItem;
import com.raredev.theblocklogics.editor.view.palette.CategoryPaletteItem;
import com.raredev.theblocklogics.editor.view.palette.EditTextPaletteItem;
import com.raredev.theblocklogics.editor.view.palette.LinearHPaletteItem;
import com.raredev.theblocklogics.editor.view.palette.LinearVPaletteItem;
import com.raredev.theblocklogics.editor.view.palette.PaletteItem;
import com.raredev.theblocklogics.editor.view.palette.TextViewPaletteItem;
import java.util.ArrayList;
import java.util.List;

public class PaletteAdapter extends RecyclerView.Adapter<PaletteAdapter.VH> {

  private final List<PaletteItem> pallete = new ArrayList<>();

  private final DrawerLayout drawerLayout;
  private final LayoutInflater layoutInflater;

  public PaletteAdapter(DrawerLayout drawerLayout, LayoutInflater layoutInflater) {
    this.drawerLayout = drawerLayout;
    this.layoutInflater = layoutInflater;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VH(LayoutPalleteItemBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    var item = pallete.get(position);

    if (item instanceof CategoryPaletteItem) {
      holder.binding.category.setVisibility(View.VISIBLE);
      holder.binding.icon.setVisibility(View.GONE);
      holder.binding.name.setVisibility(View.GONE);
      holder.binding.widgetClass.setVisibility(View.GONE);
      holder.binding.category.setText(item.getName());
      return;
    }
    holder.binding.category.setVisibility(View.GONE);
    holder.binding.icon.setVisibility(View.VISIBLE);
    holder.binding.name.setVisibility(View.VISIBLE);
    holder.binding.widgetClass.setVisibility(View.VISIBLE);

    holder.binding.icon.setImageResource(item.getIcon());
    holder.binding.name.setText(item.getName());
    holder.binding.widgetClass.setText(item.getViewClassName());

    holder.itemView.setOnLongClickListener(
        v -> {
          ClipData clipData = ClipData.newPlainText("", "");
          if (ViewCompat.startDragAndDrop(
              holder.binding.dragView,
              null,
              new View.DragShadowBuilder(holder.binding.dragView),
              item,
              0)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            VibrateUtils.vibrate(100);
            return true;
          }
          return false;
        });
  }

  @Override
  public int getItemCount() {
    return pallete.size();
  }

  public void clearPalette() {
    pallete.clear();
  }

  public void ensurePalette() {
    pallete.add(new CategoryPaletteItem(StringUtils.getString(R.string.category_layouts)));
    pallete.add(new LinearHPaletteItem());
    pallete.add(new LinearVPaletteItem());

    pallete.add(new CategoryPaletteItem(StringUtils.getString(R.string.category_widgets)));
    pallete.add(new TextViewPaletteItem());
    pallete.add(new EditTextPaletteItem());
    pallete.add(new ButtonPaletteItem());

    notifyDataSetChanged();
  }

  class VH extends RecyclerView.ViewHolder {
    LayoutPalleteItemBinding binding;

    public VH(LayoutPalleteItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
