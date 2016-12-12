package com.rudy.framework.base.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.rudy.framework.R;
import com.rudy.framework.util.StringUtil;
import com.rudy.framework.widget.ImageCacheView;
import com.rudy.framework.widget.photoview.PhotoDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by RudyJun on 2016/12/8.
 */
public class PreviewPictureActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = PreviewPictureActivity.class.getName();
    public Context mContext;

    /**
     * 多张图片查看器
     */
    private ViewPager mViewPager;

    /**
     * 当前查看的是第几张图片
     */
    private TextView mTextViewCurrentViewPosition;
    /**
     * 图片的路径列表
     */
    private static List<String> imagePathList = null;
    private static int currentViewPosition = 0;//当前浏览到哪一张图片
    private static boolean isLocationPicture;   // 判断是否是本地地图预览
    private boolean isFromGrowAlbum;

    private static ProgressBar spinner = null;
    private TextView tvClose;
    private TextView tvDownload;
    private ProgressDialog progressDialog;

    protected int activityCloseEnterAnimation;
    protected int activityCloseExitAnimation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_preview_picture);
        // 解决退出Activity动画无效
        TypedArray activityStyle = getTheme().obtainStyledAttributes(new int[] {android.R.attr.windowAnimationStyle});
        int windowAnimationStyleResId = activityStyle.getResourceId(0, 0);
        activityStyle.recycle();
        activityStyle = getTheme().obtainStyledAttributes(windowAnimationStyleResId, new int[] {android.R.attr.activityCloseEnterAnimation, android.R.attr.activityCloseExitAnimation});
        activityCloseEnterAnimation = activityStyle.getResourceId(0, 0);
        activityCloseExitAnimation = activityStyle.getResourceId(1, 0);
        activityStyle.recycle();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("正在下载图片");
        mContext = this;
        tvClose= (TextView) findViewById(R.id.tvClose);
        tvClose.setOnClickListener(this);
        tvDownload= (TextView) findViewById(R.id.tvDownload);
        tvClose.setVisibility(View.VISIBLE);
        tvDownload.setVisibility(View.VISIBLE);
        tvDownload.setOnClickListener(this);
        loadData();
        findViews();
    }

    private void loadData() {
        Bundle bundle = getIntent().getExtras();
        isFromGrowAlbum=bundle.getBoolean("isFromGrowAlbum");
        imagePathList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            imagePathList.add("http://ypicture.img-cn-hangzhou.aliyuncs.com/20bd312d6eb5985314b1ac43e551a249.jpg");
        }
        if(imagePathList == null){//图片路径都没有,就算了
            finish();
        }

        Log.e(TAG,"imagePathList size"+imagePathList.size());
        isLocationPicture=bundle.getBoolean("isLocationPicture",false);
        currentViewPosition = bundle.getInt("position");

        Log.e(TAG, "current view position:" + currentViewPosition);
    }

    public void findViews(){

        mViewPager = (ViewPager) findViewById(R.id.image_view_vp);//使用开源的图片浏览实现方式
        mTextViewCurrentViewPosition = (TextView) findViewById(R.id.image_which);
        mViewPager.setAdapter(new SamplePagerAdapter());


        spinner = (ProgressBar) findViewById(R.id.loading);

        /**
         * 这里只有多张图片才出现第几张的提示，和图片滑动切换
         */
        if(imagePathList.size() > 1){

            /**
             * 设置这个,那么viewPager会将页面缓存起来,这里要注意,当设置过大时,可能会出现内存溢出
             */
            mViewPager.setOffscreenPageLimit(4);

            /**
             * 自己加一个页面变化的监听器，可以进页面的变化作相应的处理
             */
            mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {//当前选择的是哪个图片
                    // TODO Auto-generated method stub
                    Log.e(TAG, "currentViewPosition====>" + position);

                    /**
                     * 更新当前图片浏览的位置
                     */
                    currentViewPosition = position;

                    mTextViewCurrentViewPosition.setText((currentViewPosition+1)+"/"+imagePathList.size());
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                    // TODO Auto-generated method stub
//                  Log.d(TAG, "2");
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    // TODO Auto-generated method stub
                    Log.e(TAG, "3====>" + arg0);
                }

            });
            mViewPager.setCurrentItem(currentViewPosition);//设置当前显示的pager
            mTextViewCurrentViewPosition.setText((currentViewPosition + 1) + "/" + imagePathList.size());

        }else{
            mTextViewCurrentViewPosition.setText((currentViewPosition + 1) + "/" + imagePathList.size());
        }

    }

    private void setImageController(final ImageCacheView iv, String url) {
        Log.e("url" , url);
        iv.setController(Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(url))
                .setControllerListener(new BaseControllerListener<ImageInfo>() {
                    @Override
                    public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {
                        iv.getLayoutParams().height = imageInfo.getHeight();
                        iv.getLayoutParams().width = imageInfo.getWidth();
                        updateViewSize(iv, imageInfo);
                    }

                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo
                            imageInfo, @Nullable Animatable animatable) {
                        updateViewSize(iv, imageInfo);
                    }

                })
                .build());
    }

    private void updateViewSize(SimpleDraweeView draweeView, @Nullable ImageInfo imageInfo) {
        if (null != draweeView && null != imageInfo) {
            float aspectRatio = (float) imageInfo.getWidth() / imageInfo.getHeight();
            draweeView.setTag(R.id.photoTag, aspectRatio);
            Log.e("aspectRatio" , "aspectRatio = "+aspectRatio);
            draweeView.setAspectRatio(aspectRatio);
        }
    }


    private class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            if(imagePathList == null){
                return 0;
            }

            return imagePathList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {

//          if(imagePathList == null){
//              CLog.e(TAG, "iamge path list is null!");
//              return null;
//          }
            View view = View.inflate(PreviewPictureActivity.this , R.layout.layout_zoomin_post_pictures_item , null);

            PhotoDraweeView photoView = (PhotoDraweeView) view.findViewById(R.id.bigPhoto);
            Log.e(TAG, "currentViewPosition=======>" + currentViewPosition);
            Log.e(TAG, "picture path=======>" + imagePathList.get(currentViewPosition));


            /**
             * 这里的图片分两类:
             * 一个是直接的图片地址
             * 一个是base64格式的图片如：
             data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFYAAAB0CAIAAACQUFKdAAAD3ElEQVR4nO3Zv0tyCxgH8PcfaWhoUF6qpQgacqwl7CSnKOlaCELRb/RGkYFWUJRLWmRCxA2SrCUa0oZqCBcNEfoBWraoUTTk4A+ovu8QN+4t6w73eArf73c6PPI8PueDnoPHH/jt8+OrF/j6kIAEJEDhCNLp9O7u7l9SZGtr6+bmpkB7onAEx8fHP3/+/FOK1NXV2Wy2Au2JwhHs7e319PRIMmp1ddVisUgyKm8KReDz+fR6vSSj7Hb71NSUJKPyhgQkIAFIABKABCABSAASgAQgAUgAEoAEIAFIABKABCABSAASgAQgAUgAEoAEIAFIABKABCABSAASgAQgAUgAEoAEIAFIABKABCABSAASgAQgAUgAEoAEIAFIABKABCABSIDCERweHhoMBklGLS8vz8zMSDIqb94SJJPJUymytLTU0NAgySij0WgwGCQZFYvFHh8fPyO4v7+vra1tkSIajUatVksyShAEQRAkGVVeXn50dPQZQSwWa2pqkujz9R0zNDS0sbHxpvgvguvr6+bmZhlXkjuDg4Obm5tviiQgAQlAApAAJAAJQAKQACQACUACkAAkAAnw/pGJRqORcSW5YzKZ/uORyd3dXUVFxR/FG6VS6fV6PyN4fn6+uLg4Lt6EQqFsNvsZwe8ZEpCABCABSAASgAQgAUgAEuAbEsTjcZnf8QsIXC7XxMSE2Wy2WCyzs7NWq9VqtVosFrPZ3NnZqVAoAoGAnPvITZBOpysrK+12u9PpHB8fLysrs1qt09PTL5WRkZHq6urz8/O8vblcrhAryU3w8PAgCMLLsdvtLikp6e3t7ejoGB4eBnB6etrV1fW+KxwOOxyO7u7uQqwkN0EqlWpsbASws7Oj1+sHBgb8fv/CwsLKygoAv9+v1Wrfd7ndbo1G82onbb6AQKvVejye0dFRAPF4XKvVOhyOl1fD4XBeAgDRaLS9vb0QK33BF0EUxUwmA8Dn883NzW1vb9tsNrfbvb+/b7PZPjrPYDBYJATZbLampmZsbMxsNvf395tMJqvVGo1G+/r6SktLq6qq8l4LUEwE6XRapVK5XK61tTUAoVDI6XTOz88DuLy8PDg40Ol0eRuLh+Cfd4REItHa2rq4uOjxeF4qgUDgo/M8OTkpEoJUKqVWq5+enl4r6+vrSqXy5TgYDH50OQyHw21tbYVYSW6CXC6nUCgEQRBFURTFlpYWnU73WlGpVHnvfC6Xq76+XqFQiKJ4dXUl7UpyE2QyGaPReHZ2Fvk70Wg0mUxGo9FIJOL1eicnJ9933d7exmKxRCIRiUTe/xHwP/PtfibJHxKQgAQAfgEhmFtu4GQ2NAAAAABJRU5ErkJggg==
             */
            String url = imagePathList.get(position).trim();
            photoView.setImageSrc(url);

         /*   if(isLocationPicture){
                Bitmap bitmap= BitmapFactory.decodeFile(url);
                photoView.setImageBitmap(bitmap);
            }
            else {
//                photoView.setAdjustViewBounds(true);
//                setImageController(photoView , url);

            }*/

            /*//监听图片区域动作
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View arg0, float arg1, float arg2) {
                    ((Activity)mContext).finish();
                }
            });*/


     /*       //监听整个显示区域动作
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

                @Override
                public void onViewTap(View arg0, float arg1, float arg2) {
                    ((Activity) mContext).finish();
                }

            });*/

            /**
             * 这个是让小图保持原形，不充满全屏的关键
             */
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
    public void finish() {
        super.finish();
        overridePendingTransition(activityCloseEnterAnimation, activityCloseExitAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvClose:
                finish();
                break;
            case R.id.tvDownload:
                if(imagePathList!=null){
                    if(!StringUtil.isEmpty(imagePathList.get(currentViewPosition))){
                        progressDialog.show();
                        Log.e(TAG, "imagePathList.get(currentViewPosition)" + imagePathList.get(currentViewPosition) + "currentViewPosition" + currentViewPosition);
                        //TODO 图片下载
                    }
                }
                break;
        }
    }
}
