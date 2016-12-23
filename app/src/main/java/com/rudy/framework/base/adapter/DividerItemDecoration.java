package com.rudy.framework.base.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rudy.framework.util.DisplayUtil;

/**
 * Created by RudyJun on 2016/12/23.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {


    public static final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL = LinearLayoutManager.VERTICAL;

    private ShapeDrawable mDivider;

    private int mOrientation;
    private Context context;

    public DividerItemDecoration(Context context, int orientation) {
        mDivider = new ShapeDrawable();
        mDivider.setIntrinsicHeight(DisplayUtil.dip2px(context, 1));
        mDivider.setShape(new RectShape());
        mDivider.getPaint().setColor(Color.parseColor("#BEBEBE"));
        setOrientation(orientation);
        this.context = context;
    }

    public DividerItemDecoration(Context context, int orientation, int dividerColor, int dividerHeight) {
        mDivider = new ShapeDrawable();
        mDivider.setIntrinsicHeight(DisplayUtil.dip2px(context, dividerHeight));
        mDivider.setShape(new RectShape());
        mDivider.getPaint().setColor(dividerColor);
        setOrientation(orientation);
    }

    //分割线的颜色
    public void setDividerColor(int color) {
        mDivider.getPaint().setColor(context.getResources().getColor(color));
    }

    /**
     * @param height 单位为dp
     */
    public void setDividerHeight(int height) {
        int heightPixel = DisplayUtil.dip2px(context, height);
        mDivider.setIntrinsicHeight(heightPixel);
    }


    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent) {

        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }

    }


    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mDivider.getIntrinsicHeight();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        if (mOrientation == VERTICAL) {
            int childCount = parent.getAdapter().getItemCount();
            if (!isLastRaw(itemPosition, childCount)) {
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }


    private boolean isLastRaw(int pos, int childCount) {

        if (pos >= childCount - 1) {
            // 如果是最后一行，则不需要绘制底部
            return true;

        }
        return false;
    }
}
