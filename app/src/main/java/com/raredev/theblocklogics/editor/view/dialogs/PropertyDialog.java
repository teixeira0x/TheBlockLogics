package com.raredev.theblocklogics.editor.view.dialogs;

import android.content.Context;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.blankj.utilcode.util.SizeUtils;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.models.Property;

public abstract class PropertyDialog {

  protected AlertDialog dialog;

  protected Context context;
  protected ViewItem viewItem;
  protected Property property;

  public PropertyDialog(Context context, ViewItem viewItem, Property property) {
    this.context = context;
    this.viewItem = viewItem;
    this.property = property;

    dialog = new MaterialAlertDialogBuilder(context).create();
    dialog.setTitle(getTitle());
    dialog.setButton(
        AlertDialog.BUTTON_POSITIVE, context.getString(R.string.save), (d, w) -> onSaveValue());
    dialog.setButton(
        AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel), (d, w) -> d.dismiss());
  }

  public String getTitle() {
    var title = property.getName();
    return Character.toUpperCase(title.charAt(0)) + title.substring(1);
  }

  public void setPositiveButtonEnabled(boolean enabled) {
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(enabled);
  }

  public void setView(View view, int left, int top, int right, int bottom) {
    dialog.setView(
        view,
        SizeUtils.dp2px(left),
        SizeUtils.dp2px(top),
        SizeUtils.dp2px(right),
        SizeUtils.dp2px(bottom));
  }

  public void show() {
    dialog.show();
  }

  public abstract void onSaveValue();
}
