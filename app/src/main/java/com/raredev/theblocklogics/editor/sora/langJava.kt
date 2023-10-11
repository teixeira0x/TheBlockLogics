package com.raredev.theblocklogics.editor.sora

import com.itsaky.androidide.treesitter.java.TSLanguageJava
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.COMMENT
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.FUNCTION_NAME
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.KEYWORD
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.LITERAL
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.OPERATOR
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.ANNOTATION
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.FIELD
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.CONSTANT
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.TYPE_NAME
import com.raredev.theblocklogics.utils.FileUtil.readFromAsset
import io.github.rosemoe.sora.editor.ts.TsLanguage
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import io.github.rosemoe.sora.editor.ts.TsThemeBuilder
import io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle

/**
 * Tree Sitter language for Java.
 *
 * @author Akash Yadav
 */
class TsLanguageJava() : TsLanguage(JavaLanguageSpec(), false, { buildJavaTheme() }) {}

private class JavaLanguageSpec() : TsLanguageSpec(
  TSLanguageJava.getInstance(), readFromAsset("editor/treesitter/java/highlights.scm")
)

fun TsThemeBuilder.buildJavaTheme() {
  makeStyle(COMMENT, 0, false, true, false) applyTo "comment"
  makeStyle(KEYWORD, 0, true, false, false) applyTo arrayOf(
    "variable.builtin",
    "function.builtin",
    "type.builtin",
    "keyword",
  )
  makeStyle(LITERAL) applyTo arrayOf(
    "string",
    "number"
  )
  makeStyle(CONSTANT) applyTo arrayOf(
    "constant.builtin",
    "constant"
  )
  makeStyle(ANNOTATION) applyTo arrayOf(
    "attribute"
  )
  makeStyle(FIELD) applyTo arrayOf(
    "variable.field",
    "variable"
  )
  makeStyle(TYPE_NAME) applyTo arrayOf(
    "type"
  )
  makeStyle(FUNCTION_NAME) applyTo arrayOf(
    "function.method"
  )
  makeStyle(OPERATOR) applyTo "operator"
}