package com.rudy.framework.base.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.rudy.framework.R;
import com.rudy.framework.base.Constants;
import com.rudy.framework.util.CollectionUtil;
import com.rudy.framework.util.FileUtil;
import com.rudy.framework.util.ImageUtil;
import com.rudy.framework.util.StringUtil;
import com.rudy.framework.widget.LoadingDrawable;
import com.rudy.framework.widget.photoview.HackyViewPager;
import com.rudy.framework.widget.photoview.OnPhotoTapListener;
import com.rudy.framework.widget.photoview.OnViewTapListener;
import com.rudy.framework.widget.photoview.PhotoDraweeView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by RudyJun on 2016/12/8.
 * 需传入图片的路径，默认查看图片的位置，图片路径必须传，位置不传的话默认为0
 */
public class PreviewPictureActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = PreviewPictureActivity.class.getName();

    /**
     * 多张图片查看器
     */
    private HackyViewPager mViewPager;

    /**
     * 当前查看的是第几张图片
     */
    private TextView mTextViewCurrentViewPosition;
    /**
     * 图片的路径列表
     */
    private static List<String> imagePathList;
    //当前浏览到哪一张图片
    private static int currentViewPosition = 0;

    private TextView tvClose;
    private TextView tvDownload;

    private static final String PICTURE_PATH = "PICTURE_PATH";
    private static final String CURRENT_POSITION = "CURRENT_POSITION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview_picture);
        // 解决退出Activity动画无效
       /* TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[]{android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[]{android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();*/
        initData();
        initViews();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        imagePathList = bundle.getStringArrayList(PICTURE_PATH);
        //路径为空或者图片数为0，关闭页面
        if (CollectionUtil.isEmpty(imagePathList)) {
            finish();
        }
        currentViewPosition = bundle.getInt(CURRENT_POSITION);
    }

    public void initViews() {
        tvClose = (TextView) findViewById(R.id.tvClose);
        tvClose.setOnClickListener(this);
        tvDownload = (TextView) findViewById(R.id.tvDownload);
        tvClose.setVisibility(View.VISIBLE);
        tvDownload.setVisibility(View.VISIBLE);
        tvDownload.setOnClickListener(this);

        mViewPager = (HackyViewPager) findViewById(R.id.image_view_vp);
        mTextViewCurrentViewPosition = (TextView) findViewById(R.id.image_which);
        mViewPager.setAdapter(new SamplePagerAdapter());


        /**
         * 这里只有多张图片才出现第几张的提示，和图片滑动切换
         */
        if (imagePathList.size() > 1) {

            //设置最大缓存页数
            mViewPager.setOffscreenPageLimit(3);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {//当前选择的是哪个图片
                    //更新当前图片浏览的位置
                    currentViewPosition = position;
                    mTextViewCurrentViewPosition.setText((currentViewPosition + 1) + "/" + imagePathList.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }

            });

            //设置当前显示第几张图片
            mViewPager.setCurrentItem(currentViewPosition);
            mTextViewCurrentViewPosition.setText((currentViewPosition + 1) + "/" + imagePathList.size());
        } else {
            mTextViewCurrentViewPosition.setText((currentViewPosition + 1) + "/" + imagePathList.size());
        }

    }

    /**
     * @param context
     * @param pathList 图片路径
     */
    public static void startActivity(Activity context, List<String> pathList) {
        startActivity(context, pathList, 0);
    }


    /**
     * @param context
     * @param pathList 图片路径
     * @param position 默认显示位置
     */
    public static void startActivity(Activity context, List<String> pathList, int position) {
        Intent intent = new Intent(context, PreviewPictureActivity.class);
        intent.putExtra(PICTURE_PATH, (Serializable) pathList);
        intent.putExtra(CURRENT_POSITION, position);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.anim_zoom_in, 0);
    }

    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imagePathList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(PreviewPictureActivity.this, R.layout.layout_zoomin_post_pictures_item, null);

            PhotoDraweeView photoView = (PhotoDraweeView) view.findViewById(R.id.bigPhoto);
            GenericDraweeHierarchy hierarchy = photoView.getHierarchy();
            //当图片过小时，保持图片大小不变
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.CENTER_INSIDE);
            hierarchy.setProgressBarImage(new LoadingDrawable(PreviewPictureActivity.this));
            photoView.setHierarchy(hierarchy);

            String url = imagePathList.get(position);
            photoView.setImageSrc(url);

            //监听图片区域动作
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    finish();
                }
            });


            //监听整个显示区域动作
            photoView.setOnViewTapListener(new OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    finish();
                }

            });

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvClose:
                finish();
                break;
            case R.id.tvDownload:
                if (imagePathList != null) {
                    if (!StringUtil.isEmpty(imagePathList.get(currentViewPosition))) {
                        // 下载图片
                        downLoadPicture();
                    }
                }
                break;
        }
    }

    private void downLoadPicture() {
        ImageUtil.loadImageASync(imagePathList.get(currentViewPosition), new ImageUtil.BitmapUse() {
            @Override
            public void use(Bitmap bitmap) {
                String fileName = new StringBuffer()
                        .append(System.currentTimeMillis())
                        .append(Constants.UNDERLINE)
                        .append(Constants.PNG_SUFFIX)
                        .toString();
                String filePath = FileUtil.saveBitMap(bitmap, Constants.DOWNLOAD_PHOTO_PATH, fileName);
                if (!StringUtil.isEmpty(filePath)) {
                    Toast.makeText(PreviewPictureActivity.this, "图片已保存至" + filePath, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PreviewPictureActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void loadFailed() {
                Looper.prepare();
                Toast.makeText(PreviewPictureActivity.this, "图片保存失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
    }

}
