package com.u1aryz.android.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceViewHolder;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * A preference type that allows a user to choose a time.
 *
 * @author Sergey Margaritov
 * @author u1aryz
 */
public class ColorPreference extends Preference {

  private float mDensity = 0;
  private boolean mShowAlphaSlider;
  private int mColor = Color.BLACK;

  public ColorPreference(Context context) {
    super(context);
    init(context, null);
  }

  public ColorPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public ColorPreference(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    mDensity = context.getResources().getDisplayMetrics().density;
    if (attrs != null) {
      TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ColorPreference);
      try {
        mShowAlphaSlider = a.getBoolean(R.styleable.ColorPreference_alphaSlider, false);
      } finally {
        a.recycle();
      }
    }
  }

  public boolean isShowAlphaSlider() {
    return mShowAlphaSlider;
  }

  public void setShowAlphaSlider(boolean showAlphaSlider) {
    mShowAlphaSlider = showAlphaSlider;
  }

  public int getColor() {
    return mColor;
  }

  public void setColor(int color) {
    mColor = color;
    if (isPersistent()) {
      persistInt(color);
    }
    notifyChanged();
  }

  @Override public void onBindViewHolder(PreferenceViewHolder holder) {
    super.onBindViewHolder(holder);
    setPreviewColor(holder.itemView);
  }

  private void setPreviewColor(View itemView) {
    if (itemView == null) return;
    ImageView iv = new ImageView(getContext());
    LinearLayout widgetFrameView = (LinearLayout) itemView.findViewById(android.R.id.widget_frame);
    if (widgetFrameView == null) return;
    widgetFrameView.setVisibility(View.VISIBLE);

    // Remove already create preview image
    int count = widgetFrameView.getChildCount();
    if (count > 0) {
      widgetFrameView.removeViews(0, count);
    }

    LinearLayout.LayoutParams params =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    int paddingLeft = Util.dpToPx(getContext(), 22);
    int paddingRight = Util.dpToPx(getContext(), 6);

    widgetFrameView.setPadding(paddingLeft, 0, paddingRight, 0);
    widgetFrameView.setGravity(Gravity.CENTER);
    widgetFrameView.addView(iv, params);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
      iv.setBackground(new AlphaPatternDrawable((int) (5 * mDensity)));
    } else {
      iv.setBackgroundDrawable(new AlphaPatternDrawable((int) (5 * mDensity)));
    }
    iv.setImageBitmap(createPreviewBitmap());
  }

  private Bitmap createPreviewBitmap() {
    int d = (int) (mDensity * 20);
    Bitmap bm = Bitmap.createBitmap(d, d, Bitmap.Config.ARGB_8888);
    int w = bm.getWidth();
    int h = bm.getHeight();
    int c;
    final int frameWidth = (int) (mDensity * 1);
    for (int i = 0; i < w; i++) {
      for (int j = i; j < h; j++) {
        c = (i <= frameWidth
            || j <= frameWidth
            || i >= w - frameWidth - 1
            || j >= h - frameWidth - 1) ? Color.GRAY : mColor;
        bm.setPixel(i, j, c);
        if (i != j) {
          bm.setPixel(j, i, c);
        }
      }
    }
    return bm;
  }

  @Override protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    setColor(restorePersistedValue ? getPersistedInt(mColor) : (Integer) defaultValue);
  }

  @Override protected Object onGetDefaultValue(TypedArray a, int index) {
    return a.getInteger(index, Color.BLACK);
  }

  @Override protected void onClick() {
    getPreferenceManager().showDialog(this);
  }

  @Override protected Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    if (isPersistent()) {
      // No need to save instance state since it's persistent
      return superState;
    }

    SavedState state = new SavedState(superState);
    state.color = getColor();
    return state;
  }

  @Override protected void onRestoreInstanceState(Parcelable state) {
    if (state == null || !state.getClass().equals(SavedState.class)) {
      // Didn't save state for us in onSaveInstanceState
      super.onRestoreInstanceState(state);
      return;
    }

    SavedState savedState = (SavedState) state;
    super.onRestoreInstanceState(savedState.getSuperState());
    setColor(savedState.color);
  }

  private static class SavedState extends BaseSavedState {

    int color;

    public SavedState(Parcel source) {
      super(source);
      color = source.readInt();
    }

    public SavedState(Parcelable superState) {
      super(superState);
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      super.writeToParcel(dest, flags);
      dest.writeInt(color);
    }

    public static final Parcelable.Creator<SavedState> CREATOR =
        new Parcelable.Creator<SavedState>() {
          @Override public SavedState createFromParcel(Parcel in) {
            return new SavedState(in);
          }

          @Override public SavedState[] newArray(int size) {
            return new SavedState[size];
          }
        };
  }
}
