package com.raredev.theblocklogics.fragments.project

import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.viewModels
import androidx.fragment.app.Fragment
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.databinding.FragmentViewBinding
import com.raredev.theblocklogics.editor.view.utils.ViewEditorUtils
import com.raredev.theblocklogics.editor.view.views.ViewItem
import com.raredev.theblocklogics.managers.ProjectDataManager
import com.raredev.theblocklogics.utils.Logger
import com.raredev.theblocklogics.viewmodel.ProjectViewModel

class ViewFragment: Fragment(), SavableFragment, View.OnDragListener {

  private val logger = Logger.newInstance("ViewFragment")

  private val viewModel by viewModels<ProjectViewModel>(
    ownerProducer = { requireActivity() } )

  private var _binding: FragmentViewBinding? = null
  private val binding: FragmentViewBinding
    get() = checkNotNull(_binding)

  private var removeView: Boolean = false

  companion object {

    fun newInstance(): ViewFragment {
      return ViewFragment()
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    _binding = FragmentViewBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.removeViewArea.setOnDragListener(this)

    viewModel.selectedFileNameLiveData.observe(viewLifecycleOwner, this::selectFile)
  }

  override fun onDrag(host: View, event: DragEvent): Boolean {
    val state = event.getLocalState();
    when (event.getAction()) {
      DragEvent.ACTION_DRAG_STARTED -> {
        if (state is ViewItem) {
          showRemoveViewArea()
        }
        return true
      }
      DragEvent.ACTION_DRAG_EXITED -> {
        binding.deleteImage.setImageResource(R.drawable.ic_delete)
        removeView = false
        return true
      }
      DragEvent.ACTION_DRAG_ENDED -> {
        hideRemoveViewArea()
        return true
      }
      DragEvent.ACTION_DRAG_LOCATION,
      DragEvent.ACTION_DRAG_ENTERED -> {
        binding.deleteImage.setImageResource(R.drawable.ic_delete_open)
        removeView = true
        return true
      }
      DragEvent.ACTION_DROP -> {
        binding.deleteImage.setImageResource(R.drawable.ic_delete)
        if (removeView) {
          binding.viewEditor.removeDraggedEditorView()
        }
        return true
      }
      else -> return false
    }
  }

  override fun onDestroyView() {
    binding.viewEditor.removeAllViews()
    super.onDestroyView()
    _binding = null
  }

  override fun saveSelectedFileData() {
    saveFileData(viewModel.selectedFileName)
  }

  override fun saveFileData(fileName: String?) {
    fileName?.let {
      val viewsData = ViewEditorUtils.getViewsData(binding.viewEditor)
      ProjectDataManager.addView(it, viewsData)
    }
  }

  private fun selectFile(selectedFileName: String?) {
    selectedFileName?.let {
      saveFileData(viewModel.previousSelectedFileName)
      binding.viewEditor.setSelectedLayoutName("$it")
      binding.viewEditor.onSelectedFile(ProjectDataManager.getView(selectedFileName))
      logger.d("Layout: $it loaded!");
    }
  }

  private fun showRemoveViewArea() {
    animateRemoveViewArea(1.0f, 1.0f)
  }

  private fun hideRemoveViewArea() {
    animateRemoveViewArea(0f, 0f)
  }

  private fun animateRemoveViewArea(y: Float, alpha: Float) {
    binding
        .removeViewArea
        .animate()
        .y(y)
        .scaleY(y)
        .alpha(alpha)
        .setDuration(200)
        .setInterpolator(DecelerateInterpolator())
        .start()
  }
}
