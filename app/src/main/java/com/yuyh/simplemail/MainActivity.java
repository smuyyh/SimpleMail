package com.yuyh.simplemail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.yuyh.library.simplemail.MailHelper;
import com.yuyh.library.simplemail.bean.ReceiverMailInfo;
import com.yuyh.library.simplemail.bean.SendMailInfo;
import com.yuyh.library.simplemail.constant.Constants;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.Session;

public class MainActivity extends AppCompatActivity {

    private MailHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = MailHelper.getInstance(MainActivity.this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Session session = MailApp.session;
                if(session != null){
                    try {
                        List<ReceiverMailInfo> list = helper.getAllMail(Constants.MailFolder.INBOX, MailApp.info, session);
                        Log.i("TAG", list.size()+"");

                        SendMailInfo info = new SendMailInfo();
                        info.fromAddress = MailApp.info.userName;
                        info.subject = "测试邮件";
                        info.content = "测试邮件 内容";
                        info.receivers = new String[]{"352091626@qq.com"};
                        helper.sendMail(info, session);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
