package com.raredev.theblocklogics.editor.view.dialogs;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.DialogSizeBinding;
import com.raredev.theblocklogics.editor.view.utils.PropertiesApplicator;
import com.raredev.theblocklogics.editor.view.views.LayoutItem;
import com.raredev.theblocklogics.editor.view.views.ViewItem;
import com.raredev.theblocklogics.models.Property;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.OnTextChangedWatcher;

public class SizePropertyDialog extends PropertyDialog {

  private DialogSizeBinding binding;

  public SizePropertyDialog(Context context, ViewItem viewItem, Property property) {
    super(context, viewItem, property);
    binding = DialogSizeBinding.inflate(LayoutInflater.from(context));
    setView(binding.getRoot(), 20, 0, 20, 0);
    show();

    binding.includeEditText.textInputLayout.setHint(R.string.enter_value);
    binding.includeEditText.textInputEditText.setInputType(
        InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

    binding.radiogroup.setOnCheckedChangeListener(
        new RadioGroup.OnCheckedChangeListener() {

          @Override
          public void onCheckedChanged(RadioGroup p1, int id) {
            if (id == R.id.rb_fixed_value) {
              binding.includeEditText.textInputLayout.setVisibility(View.VISIBLE);
              checkErrors();
            } else {
              binding.includeEditText.textInputLayout.setVisibility(View.GONE);
              setPositiveButtonEnabled(true);
            }
          }
        });

    binding.includeEditText.textInputEditText.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            checkErrors();
          }
        });

    var savedValue = getSavedValue();

    if (savedValue == ViewGroup.LayoutParams.MATCH_PARENT) {
      binding.rbMatchParent.setChecked(true);
    } else if (savedValue == ViewGroup.LayoutParams.WRAP_CONTENT) {
      binding.rbWrapContent.setChecked(true);
    } else {
      binding.includeEditText.textInputEditText.setText("" + savedValue);
      binding.rbFixedValue.setChecked(true);
    }
  }

  public int getSavedValue() {
    if (property.getType() == Property.TYPE_WIDTH) {
      return viewItem.getViewData().width;
    }
    return viewItem.getViewData().height;
  }

  @Override
  public void onSaveValue() {
    var viewData = viewItem.getViewData();

    int value = -1;

    var checkedId = binding.radiogroup.getCheckedRadioButtonId();
    if (checkedId == R.id.rb_match_parent) {
      value = ViewGroup.LayoutParams.MATCH_PARENT;
    } else if (checkedId == R.id.rb_wrap_content) {
      value = ViewGroup.LayoutParams.WRAP_CONTENT;
    } else {
      value = Integer.parseInt(binding.includeEditText.textInputEditText.getText().toString());
    }

    switch (property.getType()) {
      case Property.TYPE_WIDTH:
        viewData.width = value;
        break;
      case Property.TYPE_HEIGHT:
        viewData.height = value;
        break;
    }

    PropertiesApplicator.applyViewProperties(viewItem);
  }

  private void checkErrors() {
    var text = binding.includeEditText.textInputEditText.getText().toString();
    if (text.length() <= 0) {
      binding.includeEditText.textInputLayout.setError(
          context.getString(R.string.error_field_empty));
      setPositiveButtonEnabled(false);
      return;
    }

    if (text.length() > 3) {
      binding.includeEditText.textInputLayout.setError(
          context.getString(R.string.error_field_exceeds_character_limit));
      setPositiveButtonEnabled(false);
      return;
    }

    binding.includeEditText.textInputLayout.setErrorEnabled(false);
    setPositiveButtonEnabled(true);
  }
}
