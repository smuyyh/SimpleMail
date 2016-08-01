package com.yuyh.library.simplemail.utils;

import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class StringUtils {

    public static String encodeGBK(String str) {
        try {
            String strEncode = StringUtils.getEncoding(str);
            String temp = new String(str.getBytes(strEncode), "GBK");
            return temp;
        } catch (java.io.IOException ex) {

            return null;
        }
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s = encode;
                return s;
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }
        encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    /**
     * 创建｛文字内容、字体颜色、字体大小｝分段文字集合体
     *
     * @param text
     * @param color
     * @param textSize
     * @return
     */
    public static SpannableStringBuilder creSpanString(String[] text, int[] color, int[] textSize) {
        if (text == null || color == null || textSize == null)
            throw new IllegalArgumentException("参数不能为空");
        if (text.length != color.length || text.length != textSize.length)
            throw new IllegalArgumentException("参数数组长度不一致");
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            for (int i = 0; i < text.length; i++) {
                SpannableString sp = new SpannableString(text[i]);
                sp.setSpan(new ForegroundColorSpan(color[i]), 0, sp.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                sp.setSpan(new AbsoluteSizeSpan(textSize[i], true), 0, sp.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                sb.append(sp);
            }
            return sb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb;
    }
}
