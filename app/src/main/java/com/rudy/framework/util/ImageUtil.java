package com.rudy.framework.util;

import android.graphics.Bitmap;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.rudy.framework.FrameWorkApplication;

import java.util.concurrent.Executor;
/**
 * 用于下载图片
 * Created by RudyJun on 2016/11/23.
 */

public class ImageUtil {

    public static void loadImageASync(String url, final BitmapUse bitmapUse) {
        if(null == url) {
            return;
        }

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>> dataSource =
                imagePipeline.fetchDecodedImage(ImageRequest.fromUri(url), FrameWorkApplication.getApplication());

        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            protected void onNewResultImpl(Bitmap bitmap) {
                bitmapUse.use(bitmap);
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                bitmapUse.loadFailed();
            }
        }, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
    }

    public interface BitmapUse {
        void use(Bitmap bitmap);

        void loadFailed();
    }
}
