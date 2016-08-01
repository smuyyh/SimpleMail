package com.yuyh.simplemail;

import android.app.Application;

import com.yuyh.library.simplemail.bean.LoginInfo;

import javax.mail.Session;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class MailApp extends Application {

    public static Session session;
    public static LoginInfo info;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
