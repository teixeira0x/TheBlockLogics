package com.raredev.theblocklogics.utils;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class OnTextChangedWatcher implements TextWatcher {

  @Override
  public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

  @Override
  public void afterTextChanged(Editable editable) {}

  @Override
  public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
}
