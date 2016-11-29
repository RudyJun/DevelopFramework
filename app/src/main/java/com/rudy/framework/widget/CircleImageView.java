package com.rudy.framework.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;

/**
 * Created by RudyJun on 2016/11/29.
 */

public class CircleImageView extends ImageCacheView {

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected GenericDraweeHierarchyBuilder generateHierarchyBuilder() {
        GenericDraweeHierarchyBuilder builder = super.generateHierarchyBuilder();
        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        return builder.setRoundingParams(roundingParams);
    }
}
