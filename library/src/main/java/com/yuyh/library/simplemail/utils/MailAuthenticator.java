package com.yuyh.library.simplemail.utils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * 邮件账号密码校验器
 *
 * @author yuyh.
 * @date 2016/8/1.
 */
public class MailAuthenticator extends Authenticator {
    private String userName = null;
    private String password = null;

    public MailAuthenticator(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(userName, password);
    }
}
