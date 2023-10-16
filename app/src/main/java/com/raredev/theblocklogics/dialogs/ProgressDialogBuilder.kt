package com.raredev.theblocklogics.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.raredev.theblocklogics.databinding.LayoutProgressDialogBinding

class ProgressDialogBuilder(val context: Context) {

  private val binding = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context))
  private val builder = MaterialAlertDialogBuilder(context).setView(binding.root)

  fun getDialogBuilder(): MaterialAlertDialogBuilder {
    return this.builder;
  }

  fun setPositiveButton(text: String, listener: DialogInterface.OnClickListener): ProgressDialogBuilder {
    builder.setPositiveButton(text, listener)
    return this
  }

  fun setPositiveButton(@StringRes text: Int, listener: DialogInterface.OnClickListener): ProgressDialogBuilder {
    builder.setPositiveButton(text, listener)
    return this
  }

  fun setNegativeButton(text: String, listener: DialogInterface.OnClickListener): ProgressDialogBuilder {
    builder.setNegativeButton(text, listener)
    return this
  }

  fun setNegativeButton(@StringRes text: Int, listener: DialogInterface.OnClickListener): ProgressDialogBuilder {
    builder.setNegativeButton(text, listener)
    return this
  }

  fun setNeutralButton(text: String, listener: DialogInterface.OnClickListener): ProgressDialogBuilder {
    builder.setNeutralButton(text, listener)
    return this
  }

  fun setNeutralButton(@StringRes text: Int, listener: DialogInterface.OnClickListener): ProgressDialogBuilder {
    builder.setNeutralButton(text, listener)
    return this
  }

  fun show(): AlertDialog = builder.show()

  fun create(): AlertDialog = builder.create()

  fun setTitle(title: String): ProgressDialogBuilder {
    builder.setTitle(title)
    return this
  }

  fun setTitle(@StringRes title: Int): ProgressDialogBuilder {
    builder.setTitle(title)
    return this
  }

  fun setMessage(message: String): ProgressDialogBuilder{
    binding.message.setText(message)
    return this
  }

  fun setMessage(@StringRes message: Int): ProgressDialogBuilder {
    binding.message.setText(message)
    return this
  }

  fun setProgress(progress: Int): ProgressDialogBuilder {
    binding.circularProgressIndicator.setProgressCompat(progress, true);
    return this
  }

  fun setMax(max: Int): ProgressDialogBuilder {
    binding.circularProgressIndicator.setMax(max)
    return this
  }

  fun setMin(min: Int): ProgressDialogBuilder {
    binding.circularProgressIndicator.setMin(min)
    return this
  }

  fun setCancelable(cancelable: Boolean): ProgressDialogBuilder {
    builder.setCancelable(cancelable)
    return this
  }
}
