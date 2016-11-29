package com.rudy.framework.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.rudy.framework.R;

/**
 * Created by RudyJun on 2016/11/29.
 */

public class ImageCacheView extends SimpleDraweeView {

    protected String imageSrc;

    protected Context context;

    public ImageCacheView(Context context) {
        super(context);
        init();
    }

    public ImageCacheView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public ImageCacheView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCacheView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageCacheView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setHierarchy(generateHierarchyBuilder().build());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .build();
        setController(controller);
    }

    protected GenericDraweeHierarchyBuilder generateHierarchyBuilder() {
        return generateHierarchyBuilder(R.color.imageBackground);
    }

    protected GenericDraweeHierarchyBuilder generateHierarchyBuilder(int backgroundColor) {
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources())
                .setFadeDuration(100)
                .setBackground(ContextCompat.getDrawable(getContext(), backgroundColor))
                .setPlaceholderImage(ContextCompat.getDrawable(getContext(), R.mipmap.image_loading));

        return builder;
    }

    public void setImageSrc(String src) {
        if (src == null) {
            return;
        }
        // Uri.parse 入参为null是会出现空指针异常
        imageSrc = null == src ? "" : src;
        setImageURI(Uri.parse(src));
    }

    public String getImageSrc() {
        return imageSrc;
    }
}
