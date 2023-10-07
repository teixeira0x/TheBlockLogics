package com.raredev.theblocklogics.callback;

public interface PushCallback<Type> {
  void onComplete(Type type);
}
