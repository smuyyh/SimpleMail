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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String address = "smuyyh@126.com";
        String pwd = "";
        String host = "smtp." + address.substring(address.lastIndexOf("@") + 1);

        final SendMailInfo info = new SendMailInfo();
        info.mailServerHost = host;
        info.mailServerPort = "25";
        info.userName = address;
        info.password = pwd;
        info.validate = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Session session = MailHelper.getInstance(MainActivity.this).login(info);
                if(session != null){
                    try {
                        List<ReceiverMailInfo> list = MailHelper.getInstance(MainActivity.this).getAllMail(Constants.MailFolder.INBOX, info, session);
                        Log.i("TAG", list.size()+"");
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
