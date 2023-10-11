package com.raredev.theblocklogics.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.raredev.theblocklogics.databinding.LayoutPropertyItemBinding;
import com.raredev.theblocklogics.models.Property;
import java.util.ArrayList;
import java.util.List;

public class PropertiesAdapter extends RecyclerView.Adapter<PropertiesAdapter.VH> {

  private List<Property> properties = new ArrayList<>();
  private LayoutInflater layoutInflater;

  private PropertyListener listener;

  public PropertiesAdapter(LayoutInflater layoutInflater) {
    this.layoutInflater = layoutInflater;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VH(LayoutPropertyItemBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    var property = properties.get(position);

    holder.binding.name.setText(property.getName());
    holder.binding.value.setText(property.getValue());

    holder.itemView.setOnClickListener(
        v -> {
          if (listener != null) listener.onPropertyClick(property);
        });
  }

  @Override
  public int getItemCount() {
    return properties == null ? 0 : properties.size();
  }

  public void setProperties(List<Property> properties) {
    this.properties = properties;
    notifyDataSetChanged();
  }

  public void setPropertyListener(PropertyListener listener) {
    this.listener = listener;
  }

  public interface PropertyListener {
    void onPropertyClick(Property property);
  }

  class VH extends RecyclerView.ViewHolder {
    LayoutPropertyItemBinding binding;

    public VH(LayoutPropertyItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
