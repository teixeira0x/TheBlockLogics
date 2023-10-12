package com.raredev.theblocklogics.tasks;

import com.blankj.utilcode.util.ThreadUtils;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * Abstract class to represent asynchronous tasks. 
 *
 * @param <R> Task result type.
 */
public abstract class Task<R> {

  private Callable<R> callable = this::doInBackground;
  private CompletableFuture<R> future;
  private volatile boolean isCancelled = false;

  /**
   * Implement this method to run the task in the background.
   *
   * @return The result of the task.
   * @throws The exception that can be thrown during the task.
   */
  protected abstract R doInBackground() throws Exception;

  /**
   * This method is called when the task is successful. 
   *
   * @param result The task result.
   */
  protected void onSuccess(R result) {}

  /**
   * This method is called when the task fails.
   *
   * @param e The exception that indicates failure.
   */
  protected void onFail(Exception e) {
    e.printStackTrace();
  }

  /**  This method is called when the task is complete, regardless of the result. */
  protected void onFinish() {}

  /** This method is called when the task starts. */
  protected void onStart() {}

  /**
   * Start the task.
   *
   * @return The task instance.
   */
  public final Task<R> start() {
    onStart();
    future =
        CompletableFuture.supplyAsync(
                () -> {
                  try {
                    if (!isCancelled) {
                      return callable.call();
                    } else {
                      throw new CancellationException("Task was cancelled");
                    }
                  } catch (Throwable throwable) {
                    throw new CompletionException(throwable);
                  }
                })
            .whenComplete((result, e) -> ThreadUtils.runOnUiThread(() -> handleResult(result, e)));
    return this;
  }

  /** This method is called when the task is canceled. */
  protected void onCancel() {}

  /** Cancel the task. */
  public final void cancel() {
    isCancelled = true;
  }

  private void handleResult(R result, Throwable e) {
    if (e == null) {
      onSuccess(result);
    } else {
      if (e instanceof CancellationException) {
        onCancel();
      } else {
        onFail((Exception) e);
      }
    }
    onFinish();
  }
}
