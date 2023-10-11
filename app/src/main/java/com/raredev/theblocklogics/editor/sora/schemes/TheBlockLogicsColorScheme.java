package com.raredev.theblocklogics.editor.sora.schemes;

import io.github.rosemoe.sora.widget.schemes.EditorColorScheme;
import android.graphics.Color;

public class TheBlockLogicsColorScheme extends EditorColorScheme {

  protected static int endColorId = EditorColorScheme.END_COLOR_ID;

  public static final int XML_TAG = ++endColorId;
  public static final int FIELD = ++endColorId;
  public static final int CONSTANT = ++endColorId;
  public static final int TYPE_NAME = ++endColorId;

  @Override
  public void applyDefault() {
    super.applyDefault();

    setColor(ANNOTATION, 0xffbbb529);
    setColor(FUNCTION_NAME, Color.parseColor("#cccccc"));
    setColor(IDENTIFIER_NAME, Color.parseColor("#cccccc"));
    setColor(IDENTIFIER_VAR, Color.parseColor("#ff9876aa"));
    setColor(LITERAL, Color.parseColor("#6A8759"));
    setColor(OPERATOR, Color.parseColor("#6A8759"));
    setColor(COMMENT, 0xff808080);
    setColor(KEYWORD, 0xffcc7832);
    setColor(WHOLE_BACKGROUND, 0xff2b2b2b);
    setColor(COMPLETION_WND_BACKGROUND, 0xff2b2b2b);
    setColor(COMPLETION_WND_CORNER, 0xff999999);
    setColor(TEXT_NORMAL, Color.parseColor("#cccccc"));
    setColor(LINE_NUMBER_BACKGROUND, 0xff313335);
    setColor(LINE_NUMBER, 0xff606366);
    setColor(LINE_NUMBER_CURRENT, 0xff606366);
    setColor(LINE_DIVIDER, 0xff606366);
    setColor(SCROLL_BAR_THUMB, 0xffa6a6a6);
    setColor(SCROLL_BAR_THUMB_PRESSED, 0xff565656);
    setColor(SELECTED_TEXT_BACKGROUND, 0xff3676b8);
    setColor(MATCHED_TEXT_BACKGROUND, 0xff32593d);
    setColor(CURRENT_LINE, 0xff323232);
    setColor(SELECTION_INSERT, 0xff3676b8);
    setColor(SELECTION_HANDLE, 0xff3676b8);
    setColor(BLOCK_LINE, 0xff575757);
    setColor(BLOCK_LINE_CURRENT, 0xdd575757);
    setColor(NON_PRINTABLE_CHAR, 0xffdddddd);
    setColor(TEXT_SELECTED, 0xffffffff);
    setColor(HIGHLIGHTED_DELIMITERS_FOREGROUND, 0xffffffff);

    setColor(XML_TAG, Color.parseColor("#FFC66D"));
    setColor(FIELD, Color.parseColor("#cccccc"));
    setColor(CONSTANT, Color.parseColor("#cccccc"));
    setColor(TYPE_NAME, Color.parseColor("#cccccc"));
  }

  @Override
  public boolean isDark() {
    return true;
  }
}
