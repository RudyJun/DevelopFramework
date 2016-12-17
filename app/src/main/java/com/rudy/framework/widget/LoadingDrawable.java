package com.rudy.framework.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.rudy.framework.R;
import com.rudy.framework.util.DisplayUtil;

/**
 * Created by RudyJun on 2016/12/16.
 */

public class LoadingDrawable extends Drawable {

    private Paint mPaint;
    //进度条宽度
    private int mStrokeWidth;
    //进度条颜色
    private int mColor;
    //圆环大小
    private int mRingRadius;
    //背景圆环的颜色
    private int mRingColor;

    public LoadingDrawable(Context context) {

        mStrokeWidth = DisplayUtil.dp2px(context, 2);
        mColor = ContextCompat.getColor(context, R.color.loadingColor);
        mRingRadius = DisplayUtil.dip2px(context, 12);
        mRingColor = ContextCompat.getColor(context, R.color.ringColor);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        //抗锯齿
        mPaint.setAntiAlias(true);
    }


    @Override
    public void draw(Canvas canvas) {
        //绘制背景圆环
        drawRing(canvas, false);
        drawRing(canvas, true);

    }

    private void drawRing(final Canvas canvas, boolean isProgress) {
        final RectF rf = getRingRectF();
        float cx = rf.left + rf.width() / 2;
        float cy = rf.top + rf.height() / 2;
        if (!isProgress) {
            mPaint.setColor(mRingColor);
            canvas.drawCircle(cx, cy, mRingRadius, mPaint);
        } else {
            mPaint.setColor(mColor);
            canvas.drawArc(rf, -90, ((getLevel() / 10000f) * 360), false, mPaint);
        }
    }

    private RectF getRingRectF() {
        Rect bounds = getBounds();
        float x = bounds.width() / 2.0f;
        float y = bounds.height() / 2.0f;
        return new RectF(x - mRingRadius, y - mRingRadius, x + mRingRadius, y + mRingRadius);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }


}
