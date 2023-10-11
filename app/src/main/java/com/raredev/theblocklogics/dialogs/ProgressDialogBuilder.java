package com.raredev.theblocklogics.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.raredev.theblocklogics.databinding.LayoutProgressDialogBinding;

public class ProgressDialogBuilder {

  private Context context;
  private MaterialAlertDialogBuilder builder;
  private LayoutProgressDialogBinding binding;

  public ProgressDialogBuilder(@NonNull Context context) {
    this.context = context;
    init(context);
  }

  private void init(Context context) {
    binding = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context));
    builder = new MaterialAlertDialogBuilder(context);
    builder.setView(binding.getRoot());
  }

  public static ProgressDialogBuilder create(Context context) {
    return new ProgressDialogBuilder(context);
  }

  public MaterialAlertDialogBuilder getDialog() {
    return this.builder;
  }

  public ProgressDialogBuilder setPositiveButton(
      String text, DialogInterface.OnClickListener clickListener) {
    builder.setPositiveButton(text, clickListener);
    return this;
  }

  public ProgressDialogBuilder setPositiveButton(
      @StringRes int text, DialogInterface.OnClickListener clickListener) {
    builder.setPositiveButton(text, clickListener);
    return this;
  }

  public ProgressDialogBuilder setNegativeButton(
      String text, DialogInterface.OnClickListener clickListener) {
    builder.setNegativeButton(text, clickListener);
    return this;
  }

  public ProgressDialogBuilder setNegativeButton(
      @StringRes int text, DialogInterface.OnClickListener clickListener) {
    builder.setNegativeButton(text, clickListener);
    return this;
  }

  public ProgressDialogBuilder setNeutralButton(
      String text, DialogInterface.OnClickListener clickListener) {
    builder.setNeutralButton(text, clickListener);
    return this;
  }

  public ProgressDialogBuilder setNeutralButton(
      @StringRes int text, DialogInterface.OnClickListener clickListener) {
    builder.setNeutralButton(text, clickListener);
    return this;
  }

  public AlertDialog show() {
    return builder.show();
  }
  
  public AlertDialog create() {
    return builder.create();
  }

  public ProgressDialogBuilder setTitle(String title) {
    builder.setTitle(title);
    return this;
  }

  public ProgressDialogBuilder setTitle(@StringRes int title) {
    builder.setTitle(title);
    return this;
  }

  public ProgressDialogBuilder setMessage(String message) {
    binding.message.setText(message);
    return this;
  }

  public ProgressDialogBuilder setMessage(@StringRes int message) {
    binding.message.setText(message);
    return this;
  }

  public ProgressDialogBuilder setProgress(int progress) {
    binding.circularProgressIndicator.setProgressCompat(progress, true);
    return this;
  }

  public ProgressDialogBuilder setMax(int max) {
    binding.circularProgressIndicator.setMax(max);
    return this;
  }

  public ProgressDialogBuilder setMin(int min) {
    binding.circularProgressIndicator.setMin(min);
    return this;
  }

  public ProgressDialogBuilder setCancelable(boolean cancelable) {
    builder.setCancelable(cancelable);
    return this;
  }
}
