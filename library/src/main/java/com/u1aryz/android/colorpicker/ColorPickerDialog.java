package com.u1aryz.android.colorpicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.DialogPreference;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Called from {@link ColorPreferenceFragmentCompat}.
 *
 * @author u1aryz
 */
public class ColorPickerDialog extends DialogFragment
    implements DialogInterface.OnClickListener, ColorPickerView.OnColorChangedListener,
    TextWatcher {

  protected static final String ARG_KEY = "key";
  private static final String SAVE_STATE_COLOR = "ColorPickerDialog.color";
  private static final String SAVE_STATE_SHOW_ALPHA_SLIDER = "ColorPickerDialog.showAlphaSlider";

  private ColorPreference mPreference;

  private int mColor;
  private boolean mShowAlphaSlider;

  private ColorPickerView mColorPicker;
  private ColorPanelView mOldColorPanel;
  private ColorPanelView mNewColorPanel;
  private EditText mHexEdit;

  public static ColorPickerDialog newInstace(String key) {
    ColorPickerDialog dialog = new ColorPickerDialog();
    Bundle args = new Bundle(1);
    args.putString(ARG_KEY, key);
    dialog.setArguments(args);
    return dialog;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final DialogPreference.TargetFragment fragment =
        (DialogPreference.TargetFragment) getTargetFragment();

    if (savedInstanceState == null) {
      final String key = getArguments().getString(ARG_KEY);
      mPreference = (ColorPreference) fragment.findPreference(key);
      mColor = mPreference.getColor();
      mShowAlphaSlider = mPreference.isShowAlphaSlider();
    } else {
      mColor = savedInstanceState.getInt(SAVE_STATE_COLOR, Color.BLACK);
      mShowAlphaSlider = savedInstanceState.getBoolean(SAVE_STATE_SHOW_ALPHA_SLIDER, false);
    }
  }

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    Context context = getContext();
    View view = onCreateDialogView(context);

    mColorPicker = (ColorPickerView) view.findViewById(R.id.color_picker);
    mOldColorPanel = (ColorPanelView) view.findViewById(R.id.old_color_panel);
    mNewColorPanel = (ColorPanelView) view.findViewById(R.id.new_color_panel);
    mHexEdit = (EditText) view.findViewById(R.id.edit_hex);

    mColorPicker.setOnColorChangedListener(this);
    mHexEdit.addTextChangedListener(this);

    mOldColorPanel.setColor(mColor);
    mColorPicker.setAlphaSliderVisible(mShowAlphaSlider);
    mColorPicker.setColor(mColor, true);

    mHexEdit.setFilters(
        new InputFilter[] { new InputFilter.LengthFilter(mShowAlphaSlider ? 8 : 6) });

    return new AlertDialog.Builder(context).setView(view)
        .setPositiveButton(R.string.ok, this)
        .setNegativeButton(R.string.cancel, null)
        .create();
  }

  protected View onCreateDialogView(Context context) {
    return View.inflate(context, R.layout.dialog_color_picker, null);
  }

  @Override public void onClick(DialogInterface dialog, int which) {
    if (which == DialogInterface.BUTTON_POSITIVE) {
      int value = mColorPicker.getColor();
      getPreference().callChangeListener(value);
      getPreference().setColor(value);
    }
  }

  @Override public void onColorChanged(int newColor) {
    mNewColorPanel.setColor(newColor);

    if (mShowAlphaSlider) {
      mHexEdit.setText(String.format("%08X", newColor));
    } else {
      mHexEdit.setText(String.format("%06X", (0xFFFFFF & newColor)));
    }

    if (mHexEdit.hasFocus()) {
      InputMethodManager imm =
          (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(mHexEdit.getWindowToken(), 0);
      mHexEdit.clearFocus();
    }
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
  }

  @Override public void afterTextChanged(Editable s) {
    if (mHexEdit.isFocused()) {
      int color = Util.convertToColorInt(s.toString());
      if (color != mColorPicker.getColor()) {
        mColorPicker.setColor(color, false);
        mNewColorPanel.setColor(color);
      }
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);

    outState.putInt(SAVE_STATE_COLOR, mColor);
    outState.putBoolean(SAVE_STATE_SHOW_ALPHA_SLIDER, mShowAlphaSlider);
  }

  public ColorPreference getPreference() {
    if (mPreference == null) {
      final String key = getArguments().getString(ARG_KEY);
      final DialogPreference.TargetFragment fragment =
          (DialogPreference.TargetFragment) getTargetFragment();
      mPreference = (ColorPreference) fragment.findPreference(key);
    }
    return mPreference;
  }
}
