package com.raredev.theblocklogics.dialogs

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raredev.theblocklogics.activities.ProjectActivity
import com.raredev.theblocklogics.R
import com.raredev.theblocklogics.databinding.DialogNewProjectBinding
import com.raredev.theblocklogics.models.Project
import com.raredev.theblocklogics.utils.Constants
import com.raredev.theblocklogics.utils.FileUtil
import com.raredev.theblocklogics.utils.FileUtil.writeFile
import com.raredev.theblocklogics.utils.FileUtil.writeBitmapDrawableImage
import com.raredev.theblocklogics.utils.Logger
import com.raredev.theblocklogics.utils.OnTextChangedWatcher
import com.raredev.theblocklogics.utils.UriUtils
import com.raredev.theblocklogics.viewmodel.MainViewModel
import java.io.File;
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import org.json.JSONException;
import org.json.JSONObject;

class ConfigProjectDialog(): DialogFragment() {

  private val viewModel by viewModels<MainViewModel>(
    ownerProducer = { requireActivity() })

  private var _binding: DialogNewProjectBinding? = null
  private val binding: DialogNewProjectBinding
    get() = checkNotNull(_binding)

  private val pickMedia = registerForActivityResult(PickVisualMedia()) {
      if (it != null) {
        val drawable = UriUtils.convertUriToDrawable(it)
        binding.chooseIcon.setImageDrawable(drawable)
      }
    }

  private val textWatcher = object : OnTextChangedWatcher() {
      override fun afterTextChanged(editable: Editable) {
        verifyDetails()
      }
    }

  private lateinit var dialog: AlertDialog
  private var showAdvancedOptions: Boolean = true
  private var project: Project? = null

  companion object {
    const val APP_NAME_PATTERN = "^[a-zA-Z0-9 ]+$"
    const val APP_PACKAGE_PATTERN = "^[a-z-0-9.]+$"
    const val MIME_TYPE = "image/*"

    fun newInstance(project: Project?): ConfigProjectDialog {
      val instance = ConfigProjectDialog()
      if (project != null) {
        val args = Bundle()
        args.putParcelable(Constants.KEY_EXTRA_PROJECT, project)
        instance.arguments = args
      }
      return instance
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      project = it.getParcelable(Constants.KEY_EXTRA_PROJECT, Project::class.java)
    }
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    _binding = DialogNewProjectBinding.inflate(layoutInflater)
    toggleAdvancedOptionsVisibility()
    setProjectDetails()

    val builder = MaterialAlertDialogBuilder(requireContext())
    builder.setView(binding.root)
    builder.setTitle(if (project != null) R.string.edit_project else R.string.new_project)
    builder.setNegativeButton(R.string.cancel, null)
    builder.setPositiveButton(if (project != null) R.string.save else R.string.create, null)

    dialog = builder.create()
    dialog.setOnShowListener {
      addTextWatcher()
      verifyDetails()
      setListeners()
    }
    return dialog
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }

  private fun setProjectDetails() {
    project?.let {
      binding.apply {
        tieName.setText(it.appName)
        tiePackage.setText(it.appPackage)
        tieVersionCode.setText(it.versionCode.toString())
        tieVersionName.setText(it.versionName)
        chooseIcon.setImageDrawable(Drawable.createFromPath(it.appIconPath))
      }
    }
  }

  /** Add text watcher for all EditText  */
  private fun addTextWatcher() {
    binding.apply {
      setEditTextWatcher(tieName)
      setEditTextWatcher(tiePackage)
      setEditTextWatcher(tieVersionCode)
      setEditTextWatcher(tieVersionName)
    }
  }

  private fun setEditTextWatcher(editText: EditText) {
    editText.addTextChangedListener(textWatcher)
  }

  /** Add click listeners */
  private fun setListeners() {
    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener {
      setPositiveButtonEnabled(false)
      dismiss()
    }
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
      if (!isValidDetails()) {
        return@setOnClickListener
      }
      setPositiveButtonEnabled(false)
      CoroutineScope(Dispatchers.IO).launch {
        try {
          // Write or rewrite the project
          val writtenProject = writeProject(
            binding.tieName.text.toString(),
            binding.tiePackage.text.toString(),
            binding.tieVersionCode.text.toString().toInt(),
            binding.tieVersionName.text.toString()
          )

          withContext(Dispatchers.Main) {
            // checks if a project is being edited
            if (project == null) {
              // If there is no project being edited, start the 'ProjectActivity'
              val intent = Intent(requireContext(), ProjectActivity::class.java)
              intent.putExtra(Constants.KEY_EXTRA_PROJECT, writtenProject)
              startActivity(intent)
            } else {
              // If a project is being edited, update the project list
              if (viewModel.selectedFragment.value != Constants.HOME_FRAGMENT) {
                viewModel.setFragment(Constants.HOME_FRAGMENT)
              } else {
                viewModel.loadProjects()
              }
            }
            dismiss()
          }
        } catch (e: JSONException) {
          e.printStackTrace()
        }
      }
    }

    binding.apply {
      chooseIcon.setOnClickListener {
        // Launch the media picker to get an image
        pickMedia.launch(
          PickVisualMediaRequest.Builder()
          .setMediaType(PickVisualMedia.SingleMimeType(MIME_TYPE))
          .build()
        );
      }
      advancedOptionsToggle.setOnClickListener {
        // Changes the visibility of the advanced options
        toggleAdvancedOptionsVisibility()
      }
    }
  }

  private fun toggleAdvancedOptionsVisibility() {
    this.showAdvancedOptions = !showAdvancedOptions

    binding.advancedOptions.visibility =
        if (showAdvancedOptions) View.VISIBLE else View.GONE
  }

  private suspend fun writeProject(appName: String, appPackage: String, versionCode: Int, versionName: String): Project {
    val projectsDir = File(Constants.PROJECTS_DIR_PATH)
    val projectCode = project?.projectCode ?: getNewProjectCode(projectsDir).toString()
    val projectDir = File(projectsDir, projectCode)

    if (!projectDir.exists()) {
      projectDir.mkdirs()
    }

    val configJson = JSONObject().apply {
      put(Constants.PROJECT_NAME_KEY, appName)
      put(Constants.PROJECT_PACKAGE_KEY, appPackage)
      put(Constants.PROJECT_VERSION_CODE_KEY, versionCode)
      put(Constants.PROJECT_VERSION_NAME_KEY, versionName)
    }

    val iconFile = File(projectDir, Constants.PROJECT_ICON_FILE_NAME)
    val configFile = File(projectDir, Constants.PROJECT_CONFIG_FILE_NAME)

    writeBitmapDrawableImage(iconFile.absolutePath, binding.chooseIcon.getDrawable())
    writeFile(configFile.absolutePath, configJson.toString())

    return Project(projectDir.absolutePath, appName, appPackage, versionCode, versionName)
  }

  private fun getNewProjectCode(projectsDir: File): Int {
    var code = 600

    val projectsDirList = projectsDir.listFiles { file -> file.isDirectory }
    if (projectsDirList != null) {
      for (file in projectsDirList) {
        val name = file.name
        if (TextUtils.isDigitsOnly(name)) {
          val nameInt = name.toInt()
          code = maxOf(code, nameInt)
        }
      }
    }
    return code + 1
  }

  private fun verifyDetails() {
    setPositiveButtonEnabled(isValidName() && isValidPackage() && isValidVersionCode()  && isValidVersionName())
  }

  private fun isValidDetails(): Boolean {
    return !(binding.tilName.isErrorEnabled ||
        binding.tilPackage.isErrorEnabled ||
        binding.tilVersionCode.isErrorEnabled ||
        binding.tilVersionName.isErrorEnabled
      )
  }

  private fun isValidName(): Boolean {
    val appName = binding.tieName.text.toString().trim()

    if (appName.length <= 0) {
      binding.tilName.error = getString(R.string.error_field_empty)
    } else if (!appName.matches(Regex(APP_NAME_PATTERN))) {
      binding.tilName.error = getString(R.string.error_invalid_characters)
    } else {
      binding.tilName.isErrorEnabled = false
      return true
    }
    return false
  }

  private fun isValidPackage(): Boolean {
    val appPackage = binding.tiePackage.text.toString().trim()

    if (appPackage.length <= 0) {
      binding.tilPackage.error = getString(R.string.error_field_empty)
    } else if (!appPackage.matches(Regex(APP_PACKAGE_PATTERN)) || appPackage.endsWith(".")) {
      binding.tilPackage.error = getString(R.string.error_invalid_characters)
    } else {
      binding.tilPackage.isErrorEnabled = false
      return true
    }
    return false
  }

  private fun isValidVersionCode(): Boolean {
    val versionCode = binding.tieVersionCode.text.toString().trim()

    if (versionCode.length <= 0) {
      binding.tilVersionCode.error = getString(R.string.error_field_empty)
    } else if (versionCode.length > 4) {
      binding.tilVersionCode.error = getString(R.string.error_field_exceeds_character_limit)
    } else if (versionCode.contains(".")) {
      binding.tilVersionCode.error = getString(R.string.error_invalid_characters)
    } else {
      binding.tilVersionCode.isErrorEnabled = false
      return true
    }
    return false
  }

  private fun isValidVersionName(): Boolean {
    val versionName = binding.tieVersionName.text.toString().trim();

    if (versionName.length <= 0) {
      binding.tilVersionName.error = getString(R.string.error_field_empty)
    } else if (versionName.endsWith(".")) {
      binding.tilVersionName.error = getString(R.string.error_invalid_characters)
    } else {
      binding.tilVersionName.isErrorEnabled = false
      return true
    }
    return false
  }

  private fun setPositiveButtonEnabled(enabled: Boolean) {
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = enabled
  }
}
