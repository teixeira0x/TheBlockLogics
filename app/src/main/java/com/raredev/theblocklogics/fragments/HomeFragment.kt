package com.raredev.theblocklogics.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.FileUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.activities.ProjectActivity
import com.raredev.theblocklogics.adapters.ProjectsAdapter
import com.raredev.theblocklogics.databinding.FragmentHomeBinding
import com.raredev.theblocklogics.dialogs.ConfigProjectDialog
import com.raredev.theblocklogics.models.Project
import com.raredev.theblocklogics.utils.Constants
import com.raredev.theblocklogics.viewmodel.MainViewModel

class HomeFragment: Fragment() {

  private val viewModel by viewModels<MainViewModel>(
    ownerProducer = { requireActivity() } )

  private var _binding: FragmentHomeBinding? = null
  private val binding: FragmentHomeBinding
    get() = checkNotNull(_binding)

  private lateinit var adapter: ProjectsAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = ProjectsAdapter(layoutInflater, viewModel, object: ProjectsAdapter.ProjectListener {
      override fun onProjectClick(project: Project) {
        val intent = Intent(requireContext(), ProjectActivity::class.java)
        intent.putExtra(Constants.KEY_EXTRA_PROJECT, project)
        startActivity(intent)
      }
      override fun onProjectMenuClick(v: View, project: Project) {
        showProjectMenu(v, project)
      }
    })
    binding.rvProjects.layoutManager = LinearLayoutManager(requireContext())
    binding.rvProjects.adapter = adapter

    binding.root.setOnRefreshListener {
      binding.root.isRefreshing = false
      viewModel.loadProjects()
    }

    viewModel.projects.observe(viewLifecycleOwner) {
      if (it != null) {
        binding.noProjects.isVisible = it.isEmpty()
        adapter.notifyDataSetChanged()
      } else {
        binding.noProjects.isVisible = false
      }
    }
  }

  override fun onResume() {
    super.onResume()
    viewModel.loadProjects()
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  fun showProjectMenu(v: View, project: Project) {
    val pm = PopupMenu(requireContext(), v)
    pm.inflate(R.menu.project_item_menu)

    pm.setOnMenuItemClickListener() {
      when (it.itemId) {
        R.id.menu_config -> ConfigProjectDialog.Companion.newInstance(project).show(childFragmentManager, null)
        R.id.menu_delete -> deleteProject(project)
        else -> false
      }
      true
    }
    pm.show()
  }

  fun deleteProject(project: Project) {
    MaterialAlertDialogBuilder(requireContext())
      .setTitle(R.string.delete_project)
      .setMessage(getString(R.string.delete_project_message, project.appName))
      .setPositiveButton(R.string.yes, { _, _ ->
        FileUtils.delete(project.path)
        viewModel.loadProjects()
      })
      .setNegativeButton(R.string.no, { d, _ ->
        d.dismiss()
      })
      .show()
  }
}
