package com.raredev.theblocklogics.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.blankj.utilcode.util.FileUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.activities.BaseActivity;
import com.raredev.theblocklogics.activities.ProjectActivity;
import com.raredev.theblocklogics.adapters.ProjectsAdapter;
import com.raredev.theblocklogics.databinding.FragmentHomeBinding;
import com.raredev.theblocklogics.dialogs.ConfigProjectDialog;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.viewmodel.MainViewModel;

public class HomeFragment extends Fragment implements ProjectsAdapter.ProjectListener {

  private FragmentHomeBinding binding;
  private ProjectsAdapter adapter;

  private MainViewModel mainViewModel;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
  }

  @NonNull
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentHomeBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new ProjectsAdapter(getLayoutInflater(), mainViewModel);
    adapter.setProjectListener(this);
    binding.rvProjects.setLayoutManager(new LinearLayoutManager(requireContext()));
    binding.rvProjects.setAdapter(adapter);

    mainViewModel
        .getProjects()
        .observe(
            getViewLifecycleOwner(),
            (projects) -> {
              binding.noProjects.setVisibility(projects.isEmpty() ? View.VISIBLE : View.GONE);
              adapter.notifyDataSetChanged();
            });

    mainViewModel.observeSelectedFragment(
        getViewLifecycleOwner(),
        (selectedIndex) -> {
          if (selectedIndex != Constants.HOME_FRAGMENT) {
            return;
          }
          mainViewModel.loadProjects();
        });
  }

  @Override
  public void onResume() {
    super.onResume();
    mainViewModel.loadProjects();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void onProjectClick(Project project) {
    var intent = new Intent(requireContext(), ProjectActivity.class);
    intent.putExtra(Constants.KEY_EXTRA_PROJECT, project);
    startActivity(intent);
  }

  @Override
  public void onProjectMenuClick(View v, Project project) {
    PopupMenu pm = new PopupMenu(requireContext(), v);
    pm.inflate(R.menu.project_item_menu);

    pm.setOnMenuItemClickListener(
        (item) -> {
          var id = item.getItemId();
          if (id == R.id.menu_edit) {
            new ConfigProjectDialog(
                    (BaseActivity) requireActivity(), mainViewModel, getLayoutInflater(), project)
                .show();
          } else if (id == R.id.menu_delete) {
            deleteProject(project);
          }
          return false;
        });
    pm.show();
  }

  private void deleteProject(Project project) {
    new MaterialAlertDialogBuilder(requireContext())
        .setTitle(R.string.delete_project)
        .setMessage(getString(R.string.delete_project_message, project.getAppName()))
        .setPositiveButton(
            R.string.yes,
            (d, w) -> {
              new Thread(
                      () -> {
                        FileUtils.delete(project.getPath());
                        mainViewModel.loadProjects();
                      })
                  .start();
            })
        .setNegativeButton(
            R.string.no,
            (d, w) -> {
              d.dismiss();
            })
        .show();
  }
}
