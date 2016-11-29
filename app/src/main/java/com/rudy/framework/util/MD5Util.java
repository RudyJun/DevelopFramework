package com.rudy.framework.util;

import java.security.MessageDigest;

/**
 * Created by RudyJun on 2016/11/23.
 */
public class MD5Util {
    private static final String TAG = "MD5Util";
    public final static String MD5(String s) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


//    /**
//     * 对一个文件获取md5值
//     *
//     * @return md5串
//     */
//    public static String getMD5(File file) {
//        if (md5Digest == null) {
//            try {
//                md5Digest = MessageDigest.getInstance("MD5");
//            } catch (NoSuchAlgorithmException ne) {
//            }
//            if (md5Digest == null) {
//                return null;
//            }
//        }
//        FileInputStream fileInputStream = null;
//        try {
//            fileInputStream = new FileInputStream(file);
//            byte[] buffer = new byte[8192];
//            int length;
//            while ((length = fileInputStream.read(buffer)) != -1) {
//                md5Digest.update(buffer, 0, length);
//            }
//            return new String(Hex.encodeHex(md5Digest.digest(), false));
//        } catch (Exception e) {
//            Log.e(TAG, "Get file md5 failed.  exception:" + e.getMessage(), e);
//            return null;
//        } finally {
//            try {
//                if (fileInputStream != null)
//                    fileInputStream.close();
//            } catch (IOException e) {
//            }
//        }
//    }
}