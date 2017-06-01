package com.u1aryz.android.colorpicker;

import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Support for Preference v7.
 * need to extends this class.
 *
 * @author u1aryz
 */
public abstract class ColorPreferenceFragmentCompat extends PreferenceFragmentCompat {

  private static final String COLOR_PICKER_DIALOG_TAG =
      "com.u1aryz.android.colorpicker.COLOR_PICKER_DIALOG";

  @Override public void onDisplayPreferenceDialog(Preference preference) {
    // check if dialog is already showing
    if (getFragmentManager().findFragmentByTag(COLOR_PICKER_DIALOG_TAG) != null) {
      return;
    }

    if (preference instanceof ColorPreference) {
      ColorPickerDialog dialog = ColorPickerDialog.newInstace(preference.getKey());
      dialog.setTargetFragment(this, 0);
      dialog.show(getFragmentManager(), COLOR_PICKER_DIALOG_TAG);
    } else {
      super.onDisplayPreferenceDialog(preference);
    }
  }
}
