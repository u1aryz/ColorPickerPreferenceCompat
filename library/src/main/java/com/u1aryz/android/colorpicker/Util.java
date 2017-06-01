package com.u1aryz.android.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;

/**
 * Created by u1aryz on 2017/06/01.
 */
public class Util {

  private Util() {
    // No instances.
  }

  public static int dpToPx(Context context, int dp) {
    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
    return (int) (dp * metrics.density);
  }

  public static int convertToColorInt(String argb) {

    if (argb.startsWith("#")) {
      argb = argb.substring(1);
    }

    int alpha = 0, red = 0, green = 0, blue = 0;
    if (argb.length() == 0) {
      alpha = 255;
    } else if (argb.length() <= 2) {
      alpha = 255;
      blue = Integer.parseInt(argb, 16);
    } else if (argb.length() == 3) {
      alpha = 255;
      red = Integer.parseInt(argb.substring(0, 1), 16);
      green = Integer.parseInt(argb.substring(1, 2), 16);
      blue = Integer.parseInt(argb.substring(2, 3), 16);
    } else if (argb.length() == 4) {
      alpha = 255;
      green = Integer.parseInt(argb.substring(0, 2), 16);
      blue = Integer.parseInt(argb.substring(2, 4), 16);
    } else if (argb.length() == 5) {
      alpha = 255;
      red = Integer.parseInt(argb.substring(0, 1), 16);
      green = Integer.parseInt(argb.substring(1, 3), 16);
      blue = Integer.parseInt(argb.substring(3, 5), 16);
    } else if (argb.length() == 6) {
      alpha = 255;
      red = Integer.parseInt(argb.substring(0, 2), 16);
      green = Integer.parseInt(argb.substring(2, 4), 16);
      blue = Integer.parseInt(argb.substring(4, 6), 16);
    } else if (argb.length() == 7) {
      alpha = Integer.parseInt(argb.substring(0, 1), 16);
      red = Integer.parseInt(argb.substring(1, 3), 16);
      green = Integer.parseInt(argb.substring(3, 5), 16);
      blue = Integer.parseInt(argb.substring(5, 7), 16);
    } else if (argb.length() == 8) {
      alpha = Integer.parseInt(argb.substring(0, 2), 16);
      red = Integer.parseInt(argb.substring(2, 4), 16);
      green = Integer.parseInt(argb.substring(4, 6), 16);
      blue = Integer.parseInt(argb.substring(6, 8), 16);
    }
    return Color.argb(alpha, red, green, blue);
  }
}
