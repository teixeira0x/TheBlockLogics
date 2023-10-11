package com.raredev.theblocklogics.fragments.project;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.FragmentViewBinding;
import com.raredev.theblocklogics.editor.view.utils.ViewEditorUtils;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.managers.ProjectDataManager;
import com.raredev.theblocklogics.utils.Logger;
import com.raredev.theblocklogics.viewmodel.ProjectViewModel;

public class ViewFragment extends Fragment implements SavableFragment, View.OnDragListener {

  private final Logger logger = Logger.newInstance("ViewFragment");
  private FragmentViewBinding binding;

  private ProjectViewModel projectViewModel;

  private boolean removeView = false;

  public static ViewFragment newInstance() {
    return new ViewFragment();
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    projectViewModel = new ViewModelProvider(requireActivity()).get(ProjectViewModel.class);
  }

  @NonNull
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentViewBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    binding.removeViewArea.setOnDragListener(this);

    projectViewModel
        .getSelectedFileNameLiveData()
        .observe(getViewLifecycleOwner(), this::selectFile);
  }

  @Override
  public boolean onDrag(View host, DragEvent event) {
    Object object = event.getLocalState();
    switch (event.getAction()) {
      case DragEvent.ACTION_DRAG_STARTED:
        if (object instanceof ViewItem) {
          showRemoveViewArea();
        }
        break;
      case DragEvent.ACTION_DRAG_EXITED:
        binding.deleteImage.setImageResource(R.drawable.ic_delete);
        removeView = false;
        break;
      case DragEvent.ACTION_DRAG_ENDED:
        hideRemoveViewArea();
        break;
      case DragEvent.ACTION_DRAG_LOCATION:
      case DragEvent.ACTION_DRAG_ENTERED:
        binding.deleteImage.setImageResource(R.drawable.ic_delete_open);
        removeView = true;
        break;

      case DragEvent.ACTION_DROP:
        binding.deleteImage.setImageResource(R.drawable.ic_delete);
        if (removeView) {
          binding.viewEditor.removeDraggedEditorView();
        }
        break;
    }
    return true;
  }

  @Override
  public void onDestroyView() {
    binding.viewEditor.removeAllViews();
    super.onDestroyView();
    binding = null;
  }

  @Override
  public void saveSelectedFileData() {
    if (projectViewModel != null) saveFileData(projectViewModel.getSelectedFileName());
  }

  @Override
  public void saveFileData(String fileName) {
    if (fileName != null && isAdded()) {
      var viewsData = ViewEditorUtils.getViewsData(binding.viewEditor);
      ProjectDataManager.addView(fileName, viewsData);
    }
  }

  private void selectFile(String selectedFileName) {
    if (selectedFileName != null) {
      saveFileData(projectViewModel.getPreviousSelectedFileName());
      binding.viewEditor.setSelectedLayoutName(selectedFileName + ".xml");
      binding.viewEditor.onSelectedFile(ProjectDataManager.getView(selectedFileName));
      logger.d("Layout: " + selectedFileName + " loaded!");
    }
  }

  private void showRemoveViewArea() {
    animateRemoveViewArea(1.0f, 1.0f);
  }

  private void hideRemoveViewArea() {
    animateRemoveViewArea(0f, 0f);
  }

  private void animateRemoveViewArea(float y, float alpha) {
    binding
        .removeViewArea
        .animate()
        .y(y)
        .scaleY(y)
        .alpha(alpha)
        .setDuration(200)
        .setInterpolator(new DecelerateInterpolator())
        .start();
  }
}
