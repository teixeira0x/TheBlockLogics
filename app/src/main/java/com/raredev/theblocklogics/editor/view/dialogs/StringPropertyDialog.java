package com.raredev.theblocklogics.editor.view.dialogs;

import android.content.Context;
import android.text.Editable;
import android.view.LayoutInflater;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.DialogEditTextBinding;
import com.raredev.theblocklogics.editor.view.ViewEditorLayout;
import com.raredev.theblocklogics.editor.view.utils.PropertiesApplicator;
import com.raredev.theblocklogics.editor.view.utils.ViewEditorUtils;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.models.Property;
import com.raredev.theblocklogics.utils.OnTextChangedWatcher;

public class StringPropertyDialog extends PropertyDialog {

  private DialogEditTextBinding binding;
  private ViewEditorLayout editor;

  public StringPropertyDialog(
      Context context, ViewEditorLayout editor, ViewItem viewItem, Property property) {
    super(context, viewItem, property);
    this.editor = editor;
    binding = DialogEditTextBinding.inflate(LayoutInflater.from(context));
    setView(binding.getRoot(), 20, 20, 20, 20);
    show();

    binding.textInputLayout.setHint(R.string.enter_value);
    binding.textInputEditText.setText(property.getValue());

    binding.textInputEditText.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            checkErrors();
          }
        });
    checkErrors();
  }

  @Override
  public void onSaveValue() {
    var viewData = viewItem.getViewData();
    String value = binding.textInputEditText.getText().toString();
    switch (property.getType()) {
      case Property.TYPE_ID:
        viewData.id = value;
        break;
      case Property.TYPE_TEXT:
        viewData.text.text = value;
        break;
      case Property.TYPE_HINT:
        viewData.text.hint = value;
        break;
    }
    PropertiesApplicator.applyViewProperties(viewItem);
    if (viewItem instanceof LayoutItem) {
      ((LayoutItem) viewItem).refreshChilds();
    }
  }

  private void checkErrors() {
    var text = binding.textInputEditText.getText().toString();

    if (property.getType() == Property.TYPE_ID) {
      if (text.length() <= 0) {
        binding.textInputLayout.setError(context.getString(R.string.error_field_empty));
        setPositiveButtonEnabled(false);
        return;
      }

      if (!text.matches("[a-z_][a-z0-9_]*")) {
        binding.textInputLayout.setError(context.getString(R.string.error_invalid_characters));
        setPositiveButtonEnabled(false);
        return;
      }

      if (!text.equals(viewItem.getViewData().id) && ViewEditorUtils.isIdExists(editor, text)) {
        binding.textInputLayout.setError(context.getString(R.string.error_invalid_id));
        setPositiveButtonEnabled(false);
        return;
      }
    }

    binding.textInputLayout.setErrorEnabled(false);
    setPositiveButtonEnabled(true);
  }
}
