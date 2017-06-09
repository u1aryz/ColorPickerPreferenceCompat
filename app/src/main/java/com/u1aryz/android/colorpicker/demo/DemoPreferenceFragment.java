package com.u1aryz.android.colorpicker.demo;

import android.os.Bundle;
import com.u1aryz.android.colorpicker.ColorPreferenceFragmentCompat;

/**
 * Created by u1aryz on 2017/06/10.
 */
public class DemoPreferenceFragment extends ColorPreferenceFragmentCompat {

  @Override public void onCreatePreferences(Bundle bundle, String s) {
    addPreferencesFromResource(R.xml.demo);
  }
}
