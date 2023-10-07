package com.raredev.theblocklogics.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.raredev.theblocklogics.databinding.LayoutProjectItemBinding;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.viewmodel.MainViewModel;
import java.io.File;

public class ProjectsAdapter extends RecyclerView.Adapter<ProjectsAdapter.VH> {

  private LayoutInflater layoutInflater;
  private MainViewModel mainViewModel;

  private ProjectListener listener;

  public ProjectsAdapter(
      @NonNull LayoutInflater layoutInflater, @NonNull MainViewModel mainViewModel) {
    this.layoutInflater = layoutInflater;
    this.mainViewModel = mainViewModel;
  }

  @Override
  public VH onCreateViewHolder(ViewGroup parent, int viewType) {
    return new VH(LayoutProjectItemBinding.inflate(layoutInflater, parent, false));
  }

  @Override
  public void onBindViewHolder(VH holder, int position) {
    holder.itemView.setAnimation(
        AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in));
    var project = mainViewModel.getProjectsList().get(position);

    holder.binding.icon.setImageDrawable(Drawable.createFromPath(project.getAppIconPath()));
    holder.binding.name.setText(project.getAppName());
    holder.binding.appPackage.setText(project.getAppPackage());

    holder.itemView.setOnClickListener(
        v -> {
          if (listener != null) listener.onProjectClick(project);
        });

    holder.binding.menu.setOnClickListener(
        v -> {
          if (listener != null) listener.onProjectMenuClick(v, project);
        });
  }

  @Override
  public int getItemCount() {
    return mainViewModel.getProjectsList().size();
  }

  public void setProjectListener(ProjectListener listener) {
    this.listener = listener;
  }

  public interface ProjectListener {
    void onProjectClick(Project project);

    void onProjectMenuClick(View v, Project project);
  }

  class VH extends RecyclerView.ViewHolder {
    LayoutProjectItemBinding binding;

    public VH(@NonNull LayoutProjectItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }
  }
}
