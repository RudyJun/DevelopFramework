package com.rudy.framework.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;

import com.rudy.framework.FrameWorkApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
/**
 * Created by RudyJun on 2016/11/23.
 */
public class FileUtil {

    /**
     * 保存图片到本地
     *
     * @param bitmap
     * @param path
     * @param fileName
     * @return 保存的路径，如果为null表示保存失败
     */
    public static String saveBitMap(Bitmap bitmap, String path, String fileName) {

        if (bitmap == null || path == null || fileName == null) {
            return null;
        }

        File storePath = new File(path);

        String result = saveBitMap(bitmap, storePath, fileName);

        return result;
    }

    /**
     * 保存图片到本地
     *
     * @param bitmap
     * @param path
     * @param fileName
     * @return
     */
    public static String saveBitMap(Bitmap bitmap, File path, String fileName) {

        if (bitmap == null || path == null || fileName == null) {
            return null;
        }

        if (!path.exists()) {
            if (!path.mkdirs()) {
                return null;
            }
        }

        File destFile = new File(path, fileName);
        String result = destFile.getAbsolutePath();
        OutputStream os = null;
        try {
            os = new FileOutputStream(destFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (IOException e) {
            result = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
        return result;
    }

    /**
     * 刷新图片到相册中
     *
     * @param context
     * @param path
     */
    public static void mediaRefresh(Activity context, File path) {
        MediaScannerConnection.scanFile(context, new String[]{path.getAbsolutePath()}, null, null);
    }

    /**
     * 刷新图片到相册中
     *
     * @param context
     * @param path
     */
    public static void mediaRefresh(Activity context, String path) {
        MediaScannerConnection.scanFile(context, new String[]{path}, null, null);
    }

    /**
     * 从指定文件中读取String
     *
     * @param fileName
     * @return
     */
    public static String readFromFile(File fileName) {

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName));

            String tempString;

            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
        } catch (IOException e) {
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
            }
        }

        return sb.toString();
    }

    /**
     * 把String写入指定文件
     *
     * @param fileName
     * @param text
     */
    public static void writeToFile(File fileName, String text) {

        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName));

            writer.write(text);
            writer.flush();

        } catch (IOException e) {
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
            }
        }

    }

    /**
     * 复制单个文件
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */

    public static void copyFile(File s, File t) {

        FileInputStream fi = null;

        FileOutputStream fo = null;

        FileChannel in = null;

        FileChannel out = null;

        try {

            fi = new FileInputStream(s);

            fo = new FileOutputStream(t);

            in = fi.getChannel();//得到对应的文件通道

            out = fo.getChannel();//得到对应的文件通道

            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {
                fi.close();

                in.close();

                fo.close();

                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static File getCacheDir() {
        return FrameWorkApplication.getApplication().getApplicationContext().getCacheDir();
    }

    public static File getExternalCacheDir() {
        return FrameWorkApplication.getApplication().getApplicationContext().getExternalCacheDir();
    }

    public static File getFileDir() {
        return FrameWorkApplication.getApplication().getApplicationContext().getFilesDir();
    }

    public static File getExternalFileDir() {
        return FrameWorkApplication.getApplication().getApplicationContext().getExternalFilesDir(null);
    }

    public static boolean saveBitmap(Bitmap bm, String picName) {
        try {
            File f = new File(picName);
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean fileExist(String path, String fileName) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        if (!file.isDirectory()) {
            return false;
        }

        String[] tempList = file.list();
        for (int i = 0; i < tempList.length; i++) {
            if (fileName.equals(tempList[i])) {
                return true;
            }
        }
        return false;

    }


    public static void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
            }
        }
    }

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
