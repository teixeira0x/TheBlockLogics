package com.raredev.theblocklogics.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.raredev.theblocklogics.databinding.LayoutProjectFileItemBinding;
import com.raredev.theblocklogics.managers.ProjectDataManager;
import com.raredev.theblocklogics.models.ProjectFile;
import java.util.List;

public class ProjectFileAdapter extends RecyclerView.Adapter<ProjectFileAdapter.VH> {

  private List<ProjectFile> files;
  private ProjectFileListener listener;

  private LayoutInflater layoutInflater;

  private int type;

  public ProjectFileAdapter(LayoutInflater layoutInflater, int type) {
    this.layoutInflater = layoutInflater;
    this.type = type;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VH(LayoutProjectFileItemBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    var file = files.get(position);

    String layoutName = file.getName() + ".xml";
    String activityName = ProjectDataManager.getLayoutActivityName(layoutName);
    if (type == ProjectFile.TYPE_XML) {
      holder.binding.name.setText(layoutName);
      holder.binding.className.setText(activityName);
    } else {
      holder.binding.name.setText(activityName);
      holder.binding.className.setText(layoutName);
    }

    holder.itemView.setOnClickListener(
        v -> {
          if (listener != null) listener.onFileClick(file);
        });
    holder.itemView.setOnLongClickListener(
        v -> {
          if (listener != null) {
            return listener.onFileLongClick(file);
          }
          return false;
        });
  }

  @Override
  public int getItemCount() {
    return files == null ? 0 : files.size();
  }

  public void setFiles(List<ProjectFile> files) {
    this.files = files;
    notifyDataSetChanged();
  }

  public void setProjectFileListener(ProjectFileListener listener) {
    this.listener = listener;
  }

  public interface ProjectFileListener {
    void onFileClick(ProjectFile file);

    boolean onFileLongClick(ProjectFile file);
  }

  class VH extends RecyclerView.ViewHolder {
    LayoutProjectFileItemBinding binding;

    public VH(LayoutProjectFileItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
