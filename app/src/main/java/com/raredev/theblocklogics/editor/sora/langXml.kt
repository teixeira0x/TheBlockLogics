package com.raredev.theblocklogics.editor.sora

import com.itsaky.androidide.treesitter.xml.TSLanguageXml
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.COMMENT
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.LITERAL
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.OPERATOR
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.XML_TAG
import com.raredev.theblocklogics.editor.sora.schemes.TheBlockLogicsColorScheme.TEXT_NORMAL
import com.raredev.theblocklogics.utils.FileUtil.readFromAsset
import io.github.rosemoe.sora.editor.ts.TsLanguage
import io.github.rosemoe.sora.editor.ts.TsLanguageSpec
import io.github.rosemoe.sora.editor.ts.TsThemeBuilder
import io.github.rosemoe.sora.lang.styling.TextStyle.makeStyle

class TsLanguageXml() : TsLanguage(XmlLanguageSpec(), false, { buildXmlTheme() }) {}

private class XmlLanguageSpec() : TsLanguageSpec(
    TSLanguageXml.getInstance(), readFromAsset("editor/treesitter/xml/highlights.scm"),
)

fun TsThemeBuilder.buildXmlTheme() {
  makeStyle(COMMENT, 0, false, true, false) applyTo "comment"
  makeStyle(LITERAL) applyTo arrayOf("attr.value")
  makeStyle(XML_TAG) applyTo arrayOf(
    "element.tag"
  )
  makeStyle(TEXT_NORMAL) applyTo arrayOf(
    "xml_decl",
    "xmlns.prefix",
    "attr.prefix",
    "attr.name",
    "xml.ref"
  )
  makeStyle(OPERATOR) applyTo "operator"
}