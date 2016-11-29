package com.rudy.framework.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

/**
 * Created by RudyJun on 2016/11/23.
 */
public class BitmapHelper {

    private static String TAG = "BitmapHelper";

    /**
     * get the orientation of the bitmap {@link ExifInterface}
     *
     * @param path
     * @return
     */
    public final static int getDegress(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation =
                    exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * rotate the bitmap
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * caculate the bitmap sampleSize
     */
    public final static int caculateInSampleSize(Options options, int rqsW, int rqsH) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (rqsW == 0 || rqsH == 0)
            return 1;
        if (height > rqsH || width > rqsW) {
            final int heightRatio = Math.round((float) height / (float) rqsH);
            final int widthRatio = Math.round((float) width / (float) rqsW);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    /**
     * 压缩指定路径的图片，并得到图片对象
     *
     * @param path    bitmap source path
     * @return Bitmap {@link Bitmap}
     */
    public final static Bitmap compressBitmap(String path, int rqsW, int rqsH) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = caculateInSampleSize(options, rqsW, rqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    /**
     * 压缩指定路径图片，并将其保存在缓存目录中，通过isDelSrc判定是否删除源文件，并获取到缓存后的图片路径
     *
     * @param context
     * @param srcPath
     * @param rqsW
     * @param rqsH
     * @param isDelSrc
     * @return
     */
    public final static String compressBitmap(Context context, String srcPath, String desPath, int rqsW, int rqsH,
                                              boolean isDelSrc) {
        Bitmap bitmap = compressBitmap(srcPath, rqsW, rqsH);
        File srcFile = new File(srcPath);

        int degree = getDegress(srcPath);
        try {
            if (degree != 0)
                bitmap = rotateBitmap(bitmap, degree);
            File file = new File(desPath);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(CompressFormat.PNG, 70, fos);
            fos.close();
            if (isDelSrc)
                srcFile.deleteOnExit();
        } catch (Exception e) {
            Log.e("BitmapHelper-->compressBitmap", e.getMessage() + "");
        }
        return desPath;
    }

    /**
     * 此方法过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     *
     * @param is
     * @param reqsW
     * @param reqsH
     * @param isAdjust
     * @return
     */
    @Deprecated
    public final static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH, boolean isAdjust) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return compressBitmap(bitmap, reqsW, reqsH, isAdjust);
    }

    /**
     * 压缩某个输入流中的图片，可以解决网络输入流压缩问题，并得到图片对象
     */
    public final static Bitmap compressBitmap(InputStream is, int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ReadableByteChannel channel = Channels.newChannel(is);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining())
                    baos.write(buffer.get());
                buffer.clear();
            }
            byte[] bts = baos.toByteArray();
            Bitmap bitmap = compressBitmap(bts, reqsW, reqsH);
            is.close();
            channel.close();
            baos.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static Bitmap compressBitMap(String path, int reqsW, int reqsH) {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ReadableByteChannel channel = Channels.newChannel(inputStream);
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (channel.read(buffer) != -1) {
                buffer.flip();
                while (buffer.hasRemaining())
                    baos.write(buffer.get());
                buffer.clear();
            }
            byte[] bts = baos.toByteArray();
            Bitmap bitmap = compressBitmap(bts, reqsW, reqsH);
            inputStream.close();
            channel.close();
            baos.close();
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 压缩指定byte[]图片，并得到压缩后的图像
     *
     * @param bts
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(byte[] bts, int reqsW, int reqsH) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
    }

    /**
     * 此方法已过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法
     *
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    @Deprecated
    public final static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH, boolean isAdjust) {
        if (bitmap == null || reqsW == 0 || reqsH == 0)
            return bitmap;
        if (bitmap.getWidth() > reqsW || bitmap.getHeight() > reqsH) {
            float scaleX =
                    new BigDecimal(reqsW).divide(new BigDecimal(bitmap.getWidth()), 4, RoundingMode.DOWN).floatValue();
            float scaleY =
                    new BigDecimal(reqsH).divide(new BigDecimal(bitmap.getHeight()), 4, RoundingMode.DOWN).floatValue();
            if (isAdjust) {
                scaleX = scaleX < scaleY ? scaleX : scaleY;
                scaleY = scaleX;
            }
            Matrix matrix = new Matrix();
            matrix.postScale(scaleX, scaleY);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     * 压缩已存在的图片对象，并返回压缩后的图片
     *
     * @param bitmap
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(Bitmap bitmap, int reqsW, int reqsH) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            byte[] bts = baos.toByteArray();
            Bitmap res = compressBitmap(bts, reqsW, reqsH);
            baos.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    /**
     * 此方法过期，该方法可能造成OutOfMemoryError，使用不含isAdjust参数的方法 get bitmap form resource dictory, and then compress bitmap
     * according to reqsW and reqsH
     *
     * @param res   {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    @Deprecated
    public final static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH, boolean isAdjust) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resID);
        return compressBitmap(bitmap, reqsW, reqsH, isAdjust);
    }

    /**
     * 压缩资源图片，并返回图片对象
     *
     * @param res   {@link Resources}
     * @param resID
     * @param reqsW
     * @param reqsH
     * @return
     */
    public final static Bitmap compressBitmap(Resources res, int resID, int reqsW, int reqsH) {
        final Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resID, options);
        options.inSampleSize = caculateInSampleSize(options, reqsW, reqsH);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resID, options);
    }

    /**
     * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题 <br>
     * 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
     * @param maxBytes 压缩后的图像最大大小 单位为byte
     * @return
     */
    public final static Bitmap compressBitmap(Bitmap bitmap, long maxBytes) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, baos);
            int options = 90;
            while (baos.toByteArray().length > maxBytes) {
                baos.reset();
                bitmap.compress(CompressFormat.PNG, options, baos);
                options -= 10;
            }
            byte[] bts = baos.toByteArray();
            Bitmap bmp = BitmapFactory.decodeByteArray(bts, 0, bts.length);
            baos.close();
            return bmp;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // public final static Bitmap compressBitmap(InputStream is, long maxBytes) {
    // try {
    // ByteArrayOutputStream baos = new ByteArrayOutputStream();
    // byte[] bts = new byte[1024];
    // while (is.read(bts) != -1) baos.write(bts, 0, bts.length);
    // is.close();
    // int options = 100;
    // while (baos.toByteArray().length > maxBytes) {
    //
    // }
    // } catch (Exception e) {
    // }
    // }

    /**
     * 得到指定路径图片的options
     *
     * @param srcPath
     * @return Options {@link Options}
     */
    public final static Options getBitmapOptions(String srcPath) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(srcPath, options);
        return options;
    }

    public static Bitmap readBitMap(Context context, int resId) {
        Options opt = new Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * 获取网络图片
     *
     * @param url
     * @return Bitmap 返回类型
     * @author T430
     * @date 2015年9月23日 上午10:52:20
     * @version [1.0, 2015年9月23日]
     * @since version 1.0
     */
    public static Bitmap getHttpBitmap(String url) {
        URL myFileURL;
        Bitmap bitmap = null;
        try {
            myFileURL = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
            // 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
            conn.setConnectTimeout(6000);
            // 连接设置获得数据流
            conn.setDoInput(true);
            // 不使用缓存
            conn.setUseCaches(false);
            // 这句可有可无，没有影响
            // conn.connect();
            // 得到数据流
            InputStream is = conn.getInputStream();
            // 解析得到图片
//            bitmap = BitmapFactory.decodeStream(is);
            bitmap = BitmapHelper.compressBitmap(is, 480, 800);
            // 关闭数据流
            is.close();
        } catch (Exception e) {
            Log.e("Exception", e.toString());
            return null;
        }
        return bitmap;

    }

    /* 读取照片exif信息中的旋转角度
    * @param path 照片路径
    * @return角度
    */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotate(Bitmap img, int rotateDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+rotateDegree); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    /**
     * 检查照片是否有翻转, 有翻转重新翻转回正常角度
     *
     * @param filePath
     * @return boolean 是否有处理翻转
     */
    public static boolean checkAndRotatePic(String filePath) {
        try {
            // 检查是否有旋转, 有旋转重新调整
            int degree = BitmapHelper.readPictureDegree(filePath);
            if (degree > 0) {
                FileInputStream inputStream = new FileInputStream(filePath);
                Bitmap bitmap = BitmapHelper.compressBitmap(inputStream, 720, 1280);
                bitmap = BitmapHelper.rotate(bitmap, degree);
                FileUtil.saveBitmap(bitmap, filePath);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Rotate bitmap failed. exception:" + e.toString(), e);
        }
        return false;
    }

    /**
     * 检查照片是否有翻转, 有翻转重新翻转回正常角度
     *
     * @param filePath 图片源路径
     * @param savePath 翻转后的图片途径
     * @return boolean 是否有处理翻转
     */
    public static boolean checkAndRotatePic(String filePath, String savePath) {
        try {
            // 检查是否有旋转, 有旋转重新调整
            int degree = BitmapHelper.readPictureDegree(filePath);
            if (degree > 0) {
                FileInputStream inputStream = new FileInputStream(filePath);
                Bitmap bitmap = BitmapHelper.compressBitmap(inputStream, 720, 1280);
                bitmap = BitmapHelper.rotate(bitmap, degree);
                FileUtil.saveBitmap(bitmap, savePath);
                return true;
            }
        } catch (Exception e) {
            Log.e(TAG, "Rotate bitmap failed. exception:" + e.toString(), e);
        }
        return false;
    }

}
