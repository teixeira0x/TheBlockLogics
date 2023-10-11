package com.raredev.theblocklogics.tasks;

import com.blankj.utilcode.util.ThreadUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public abstract class Task<R> {

  private Callable<R> callable = () -> doInBackground();

  private CompletableFuture future;

  public abstract R doInBackground();

  public void onSuccess(R result) {}

  public void onFail(Throwable throwable) {
    throwable.printStackTrace();
  }

  public void onFinish() {}

  public void onStart() {}

  public final Task start() {
    onStart();
    future =
        CompletableFuture.supplyAsync(
                () -> {
                  try {
                    return callable.call();
                  } catch (Throwable throwable) {
                    return throwable;
                  }
                })
            .whenComplete(
                (result, throwable) -> {
                  ThreadUtils.runOnUiThread(
                      () -> {
                        if (result != null) {
                          onSuccess((R) result);
                        } else if (throwable != null){
                          onFail(throwable);
                        }
                        onFinish();
                      });
                });
    return this;
  }

  public void onCancel() {}

  public final void cancel() {
    if (future != null) {
      future.cancel(true);
      onCancel();
    }
  }
}
