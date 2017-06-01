package com.u1aryz.android.colorpicker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * This drawable will draw a simple white and gray chessboard pattern.
 * It's pattern you will often see as a background behind a
 * partly transparent image in many applications.
 *
 * @author Daniel Nilsson
 * @author u1aryz
 */
public class AlphaPatternDrawable extends Drawable {

  private final int mRectangleSize;
  private final Paint mPaint = new Paint();
  private final Paint mPaintWhite = new Paint();
  private final Paint mPaintGray = new Paint();

  /**
   * Bitmap in which the pattern will be cached.
   * This is so the pattern will not have to be recreated each time draw() gets called.
   * Because recreating the pattern i rather expensive. I will only be recreated if the
   * size changes.
   */
  private Bitmap mBitmap;

  public AlphaPatternDrawable(int mRectangleSize) {
    this.mRectangleSize = mRectangleSize;
    mPaintWhite.setColor(0xffffffff);
    mPaintGray.setColor(0xffcbcbcb);
  }

  @Override public void draw(@NonNull Canvas canvas) {
    if (mBitmap != null && !mBitmap.isRecycled()) {
      canvas.drawBitmap(mBitmap, null, getBounds(), mPaint);
    }
  }

  @Override public void setAlpha(int alpha) {
    throw new UnsupportedOperationException("Alpha is not supported by this drawable.");
  }

  @Override public void setColorFilter(@Nullable ColorFilter colorFilter) {
    throw new UnsupportedOperationException("ColorFilter is not supported by this drawable.");
  }

  @Override public int getOpacity() {
    return PixelFormat.UNKNOWN;
  }

  @Override protected void onBoundsChange(Rect bounds) {
    super.onBoundsChange(bounds);

    int height = bounds.height();
    int width = bounds.width();

    int numRectanglesHorizontal = (int) Math.ceil((width / mRectangleSize));
    int numRectanglesVertical = (int) Math.ceil(height / mRectangleSize);

    generatePatternBitmap(numRectanglesHorizontal, numRectanglesVertical);
  }

  /**
   * This will generate a bitmap with the pattern
   * as big as the rectangle we were allow to draw on.
   * We do this to chache the bitmap so we don't need to
   * recreate it each time draw() is called since it
   * takes a few milliseconds.
   */
  private void generatePatternBitmap(int numRectanglesHorizontal, int numRectanglesVertical) {

    if (getBounds().width() <= 0 || getBounds().height() <= 0) {
      return;
    }

    mBitmap =
        Bitmap.createBitmap(getBounds().width(), getBounds().height(), Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(mBitmap);

    Rect r = new Rect();
    boolean verticalStartWhite = true;
    for (int i = 0; i <= numRectanglesVertical; i++) {

      boolean isWhite = verticalStartWhite;
      for (int j = 0; j <= numRectanglesHorizontal; j++) {

        r.top = i * mRectangleSize;
        r.left = j * mRectangleSize;
        r.bottom = r.top + mRectangleSize;
        r.right = r.left + mRectangleSize;

        canvas.drawRect(r, isWhite ? mPaintWhite : mPaintGray);

        isWhite = !isWhite;
      }

      verticalStartWhite = !verticalStartWhite;
    }
  }
}
