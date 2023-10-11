package com.raredev.theblocklogics.editor.sora.schemes;

import android.content.Context;
import com.google.android.material.R;
import com.google.android.material.color.MaterialColors;

public class DynamicColorScheme extends TheBlockLogicsColorScheme {

  public void applyDynamic(Context context) {
    var colorPrimary = MaterialColors.getColor(context, R.attr.colorPrimary, 0);
    var colorOnPrimary = MaterialColors.getColor(context, R.attr.colorOnPrimary, 0);
    var colorSurface = MaterialColors.getColor(context, R.attr.colorSurface, 0);
    var colorOnSurface = MaterialColors.getColor(context, R.attr.colorOnSurface, 0);
    var colorOnSurfaceInverse = MaterialColors.getColor(context, R.attr.colorOnSurfaceInverse, 0);
    var colorSurfaceVariant = MaterialColors.getColor(context, R.attr.colorSurfaceVariant, 0);
    var colorOutline = MaterialColors.getColor(context, R.attr.colorOutline, 0);

    setColor(WHOLE_BACKGROUND, colorSurface);
    setColor(LINE_NUMBER_BACKGROUND, colorSurface);
    setColor(LINE_DIVIDER, colorOutline);
    setColor(LINE_NUMBER, colorOnSurface);
    setColor(LINE_NUMBER_CURRENT, colorOnSurface);
    setColor(LINE_NUMBER_PANEL_TEXT, colorOnSurface);
    setColor(CURRENT_LINE, colorSurfaceVariant);
    setColor(TEXT_NORMAL, colorOnSurface);
    setColor(TEXT_SELECTED, colorOnSurface);
    setColor(SELECTED_TEXT_BACKGROUND, colorPrimary);
    setColor(SELECTION_HANDLE, colorPrimary);
    setColor(SELECTION_INSERT, colorPrimary);
    setColor(BLOCK_LINE, colorSurfaceVariant);
    setColor(BLOCK_LINE_CURRENT, colorOutline);
    setColor(UNDERLINE, colorOutline);
  }
}
