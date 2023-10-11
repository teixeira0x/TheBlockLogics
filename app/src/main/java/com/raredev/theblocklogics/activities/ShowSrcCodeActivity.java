package com.raredev.theblocklogics.activities;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import androidx.core.content.res.ResourcesCompat;
import com.raredev.theblocklogics.R;
import com.raredev.theblocklogics.databinding.ActivityShowCodeBinding;
import com.raredev.theblocklogics.editor.sora.TsLanguageJava;
import com.raredev.theblocklogics.editor.sora.TsLanguageXml;
import com.raredev.theblocklogics.editor.sora.schemes.DynamicColorScheme;
import com.raredev.theblocklogics.models.ProjectFile;
import com.raredev.theblocklogics.models.SrcFile;
import com.raredev.theblocklogics.utils.Constants;
import com.raredev.theblocklogics.utils.OnTextChangedWatcher;
import io.github.rosemoe.sora.editor.ts.TsLanguage;
import java.util.ArrayList;

public class ShowSrcCodeActivity extends BaseActivity {

  private ActivityShowCodeBinding binding;
  private ArrayList<SrcFile> sources;

  private int lastSrcType = -1;
  private String lastSrcName;

  static {
    System.loadLibrary("android-tree-sitter");
    System.loadLibrary("tree-sitter-java");
    System.loadLibrary("tree-sitter-xml");
  }

  @Override
  protected View bindLayout() {
    binding = ActivityShowCodeBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setSupportActionBar(binding.toolbar);

    var extras = getIntent().getExtras();
    if (extras == null) {
      return;
    }
    if (extras.containsKey(Constants.KEY_EXTRA_SRC_LIST)) {
      sources = extras.getParcelableArrayList(Constants.KEY_EXTRA_SRC_LIST, SrcFile.class);
    }

    var dynamicColorScheme = new DynamicColorScheme();
    dynamicColorScheme.applyDynamic(this);

    binding.editor.setTypefaceText(ResourcesCompat.getFont(this, R.font.droid_sans_mono));
    binding.editor.setTypefaceLineNumber(ResourcesCompat.getFont(this, R.font.droid_sans_mono));
    binding.editor.setColorScheme(dynamicColorScheme);
    binding.editor.setDividerWidth(0);
    binding.editor.setEditable(false);

    var sourcesAdapter =
        new ArrayAdapter<SrcFile>(this, android.R.layout.simple_list_item_1, sources);
    binding.srcFileName.setAdapter(sourcesAdapter);

    binding.srcFileName.addTextChangedListener(
        new OnTextChangedWatcher() {
          @Override
          public void afterTextChanged(Editable editable) {
            setSelectedSrc(editable.toString());
          }
        });

    binding.srcFileName.setText(extras.getString(Constants.KEY_EXTRA_SELECTED_FILE), false);
    setSelectedSrc(extras.getString(Constants.KEY_EXTRA_SELECTED_FILE));
  }

  @Override
  protected void onDestroy() {
    binding.editor.release();
    super.onDestroy();
    binding = null;
  }

  private void setSelectedSrc(String srcName) {
    if (!srcName.equals(lastSrcName)) {
      lastSrcName = srcName;

      for (SrcFile source : sources) {
        if (source.getName().equals(srcName)) {
          binding.editor.setText(source.getCode());
          updateEditorLanguage(source.getType());

          break;
        }
      }
    }
  }

  private void updateEditorLanguage(int type) {
    if (type != lastSrcType) {
      lastSrcType = type;
      binding.editor.setEditorLanguage(createLanguage(type));
    }
  }

  private TsLanguage createLanguage(int type) {
    if (type == ProjectFile.TYPE_XML) {
      return new TsLanguageXml();
    }
    return new TsLanguageJava();
  }
}
