package com.rudy.framework.util;

    import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by RudyJun on 2016/11/23.
 */
public class RecyclerViewUtil {
    public static boolean isFillRecyclerView(RecyclerView mRecyclerView) {
        int childCount = mRecyclerView.getChildCount();
        View lastChildView = mRecyclerView.getChildAt(childCount - 1);
        View firstChildView = mRecyclerView.getChildAt(0);
        int top = firstChildView.getTop();
        int bottom = lastChildView.getBottom();
        int bottomEdge = mRecyclerView.getHeight() - mRecyclerView.getPaddingBottom();
        int topEdge = mRecyclerView.getPaddingTop();
        if (bottom <= bottomEdge && top < topEdge) {
            return true;
        } else {
            return false;
        }
    }
}
