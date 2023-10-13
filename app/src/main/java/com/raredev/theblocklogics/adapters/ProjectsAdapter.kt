package com.raredev.theblocklogics.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.raredev.theblocklogics.databinding.LayoutProjectItemBinding
import com.raredev.theblocklogics.models.Project
import com.raredev.theblocklogics.utils.Constants
import com.raredev.theblocklogics.viewmodel.MainViewModel
import java.io.File

public class ProjectsAdapter(
  val layoutInflater: LayoutInflater,
  val viewModel: MainViewModel,
  val listener: ProjectListener
): RecyclerView.Adapter<ProjectsAdapter.VH>() {

  class VH(internal val binding: LayoutProjectItemBinding):
    RecyclerView.ViewHolder(binding.root)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
    return VH(LayoutProjectItemBinding.inflate(layoutInflater, parent, false))
  }

  override fun onBindViewHolder(holder: VH, position: Int) {
    holder.binding.apply {
      root.setAnimation(
        AnimationUtils.loadAnimation(holder.itemView.getContext(), android.R.anim.fade_in))
      val projects = viewModel.projects.value
      projects?.let {
        val project = projects[position]

        icon.setImageDrawable(Drawable.createFromPath(project.getAppIconPath()))
        name.text = project.appName
        appPackage.text = project.appPackage

        root.setOnClickListener {
          listener.onProjectClick(project)
        }

        menu.setOnClickListener { v ->
          listener.onProjectMenuClick(v, project)
        }
      }
    }
  }

  override fun getItemCount(): Int {
    val projects = viewModel.projects.value
    return projects?.size ?: 0
  }

  public interface ProjectListener {
    fun onProjectClick(project: Project)

    fun onProjectMenuClick(v: View, project: Project)
  }
}