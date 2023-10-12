package com.raredev.theblocklogics.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.blankj.utilcode.util.ToastUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.adapters.PaletteAdapter;
import com.raredev.theblocklogics.adapters.ProjectPageAdapter;
import com.raredev.theblocklogics.databinding.ActivityProjectBinding;
import com.raredev.theblocklogics.editor.source.XMLSourceCodeGenerator;
import com.raredev.theblocklogics.editor.view.data.ViewData;
import com.raredev.theblocklogics.fragments.project.ViewFragment;
import com.raredev.theblocklogics.managers.ProjectDataManager;
import com.raredev.theblocklogics.models.Project;
import com.raredev.theblocklogics.models.ProjectFile;
import com.raredev.theblocklogics.models.SrcFile;
import com.raredev.theblocklogics.tasks.Task;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.viewmodel.ProjectViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectActivity extends BaseActivity {

  public static final int VIEW_FRAGMENT_POS = 0;
  public static final int EVENT_FRAGMENT_POS = 1;
  public static final int COMPONENT_FRAGMENT_POS = 2;

  private ActivityProjectBinding binding;
  private ProjectPageAdapter pageAdapter;

  private PaletteAdapter paletteAdapter;

  private ProjectViewModel viewModel;
  private Project project;

  @Override
  protected View bindLayout() {
    binding = ActivityProjectBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(binding.toolbar);
    binding.toolbar.setNavigationOnClickListener((v) -> onBackPressed());

    viewModel = new ViewModelProvider(this).get(ProjectViewModel.class);

    configureDrawer();
    configureTabs();

    var extras = getIntent().getExtras();
    if (extras != null && extras.containsKey(Constants.KEY_EXTRA_PROJECT)) {
      project = extras.getParcelable(Constants.KEY_EXTRA_PROJECT, Project.class);
      viewModel.setOpenedProject(project);
      new LoadProjectTask().start();
    }

    viewModel
        .getSelectedFileNameLiveData()
        .observe(
            this,
            (selectedFileName) -> {
              binding.fileSelector.setSelectedFileName(selectedFileName);
            });

    binding.fileSelector.setProjectViewModel(viewModel);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.project_toolbar_menu, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    var id = item.getItemId();

    if (id == R.id.menu_save) {
      new SaveProjectTask(() -> ToastUtils.showShort(R.string.saved)).start();
    } else if (id == R.id.menu_show_source_code) {
      new GenerateSrcCodeTask(true).start();
    }

    return true;
  }

  @Override
  public void onBackPressed() {
    if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
      binding.drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    var currentPosition = binding.viewPager.getCurrentItem();
    if (currentPosition > VIEW_FRAGMENT_POS) {
      binding.viewPager.setCurrentItem(currentPosition - 1);
      return;
    }
    showExitConfirmationDialog();
  }

  @Override
  protected void onDestroy() {
    ProjectDataManager.clearData();
    paletteAdapter.clearPalette();
    super.onDestroy();
    binding = null;
  }

  private void configureDrawer() {
    binding.drawerLayout.addDrawerListener(
        new DrawerLayout.DrawerListener() {

          @Override
          public void onDrawerSlide(View view, float v) {
            binding.root.setTranslationX(view.getWidth() * v / 2);
          }

          public void onDrawerOpened(View view) {}

          public void onDrawerClosed(View view) {}

          public void onDrawerStateChanged(int state) {}
        });

    paletteAdapter = new PaletteAdapter(binding.drawerLayout, getLayoutInflater());
    binding.rvPallete.setLayoutManager(new LinearLayoutManager(this));
    binding.rvPallete.setAdapter(paletteAdapter);
  }

  private void configureTabs() {
    pageAdapter = new ProjectPageAdapter(this);

    binding.viewPager.setAdapter(pageAdapter);
    binding.viewPager.setOffscreenPageLimit(2);

    binding.tabs.addOnTabSelectedListener(
        new TabLayout.OnTabSelectedListener() {

          @Override
          public void onTabSelected(TabLayout.Tab tab) {
            int position = tab.getPosition();
            binding.fileSelector.setSelectedTabPos(position);
            if (position > VIEW_FRAGMENT_POS) {
              binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
              return;
            }
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
          }

          public void onTabUnselected(TabLayout.Tab tab) {}

          public void onTabReselected(TabLayout.Tab tab) {}
        });

    TabLayoutMediator mediator =
        new TabLayoutMediator(
            binding.tabs,
            binding.viewPager,
            new TabLayoutMediator.TabConfigurationStrategy() {
              @Override
              public void onConfigureTab(TabLayout.Tab tab, int position) {
                tab.setText(pageAdapter.getTitle(ProjectActivity.this, position));
              }
            });
    mediator.attach();
  }

  private void saveFragmentsFileData() {
    var viewFragment = getViewFragment();
    if (viewFragment != null) {
      viewFragment.saveSelectedFileData();
    }
  }

  private void showExitConfirmationDialog() {
    new MaterialAlertDialogBuilder(this)
        .setTitle(R.string.exit_project)
        .setMessage(R.string.save_changes_before_quitting)
        .setNeutralButton(R.string.cancel, null)
        .setPositiveButton(
            R.string.save_and_exit, (d, w) -> new SaveProjectTask(() -> finish()).start())
        .setNegativeButton(R.string.exit, (d, w) -> finish())
        .show();
  }

  private ViewFragment getViewFragment() {
    ViewFragment fragment = (ViewFragment) getSupportFragmentManager().findFragmentByTag("f" + 0);
    return fragment;
  }

  class LoadProjectTask extends Task<Void> {

    @Override
    public void onStart() {
      getProgressDialog().setTitle(R.string.loading_project);
      showNonCancelableProgress();
    }

    @Override
    public Void doInBackground() throws Exception {
      // Load widget palette
      paletteAdapter.ensurePalette();

      ProjectDataManager.loadProjectFiles(project);
      ProjectDataManager.loadProjectViews(project);
      return null;
    }

    @Override
    public void onSuccess(Void result) {
      getSupportActionBar().setTitle(project.getAppName());
      getSupportActionBar().setSubtitle(project.getProjectCode());
      viewModel.setSelectedFileName(Constants.MAIN);
    }

    @Override
    public void onFail(Exception e) {
      new MaterialAlertDialogBuilder(ProjectActivity.this)
          .setTitle("Unable to load project")
          .setMessage(
              "Project name: "
                  + project.getAppName()
                  + ".\nProject code: "
                  + project.getProjectCode()
                  + ".\nException error:\n\n"
                  + e.getLocalizedMessage())
          .setCancelable(false)
          .setPositiveButton(R.string.exit_project, (d, w) -> finish())
          .show();
    }

    @Override
    public void onFinish() {
      dismissProgress();
    }
  }

  class SaveProjectTask extends Task<Void> {
    private Runnable afterSave;

    public SaveProjectTask(Runnable afterSave) {
      this.afterSave = afterSave;
    }

    @Override
    public void onStart() {
      getProgressDialog().setTitle(R.string.saving_project);
      showNonCancelableProgress();
    }

    @Override
    public Void doInBackground() throws Exception {
      saveFragmentsFileData();
      ProjectDataManager.writeProjectData(project.getPath());
      return null;
    }

    @Override
    public void onFinish() {
      if (afterSave != null) {
        afterSave.run();
      }
      dismissProgress();
    }
  }

  class GenerateSrcCodeTask extends Task<ArrayList<SrcFile>> {
    private boolean startSrcAct;

    public GenerateSrcCodeTask(boolean startSrcAct) {
      this.startSrcAct = startSrcAct;
    }

    @Override
    public void onStart() {
      getProgressDialog().setTitle(R.string.generating_source_code);
      showNonCancelableProgress();
    }

    @Override
    public ArrayList<SrcFile> doInBackground() throws Exception {
      saveFragmentsFileData();
      ArrayList<SrcFile> sources = new ArrayList<>();

      var xmlGenerator = new XMLSourceCodeGenerator();
      for (Map.Entry<String, List<ViewData>> entry : ProjectDataManager.views.entrySet()) {
        sources.add(
            new SrcFile(
                ProjectFile.TYPE_XML, entry.getKey(), xmlGenerator.generate(entry.getValue())));
      }

      return sources;
    }

    @Override
    public void onSuccess(ArrayList<SrcFile> result) {
      if (startSrcAct) {
        var it = new Intent(ProjectActivity.this, ShowSrcCodeActivity.class);
        it.putParcelableArrayListExtra(Constants.KEY_EXTRA_SRC_LIST, result);
        it.putExtra(Constants.KEY_EXTRA_SELECTED_FILE, viewModel.getSelectedFileName() + ".xml");
        startActivity(it);
      }
    }

    @Override
    public void onFinish() {
      dismissProgress();
    }
  }
}
