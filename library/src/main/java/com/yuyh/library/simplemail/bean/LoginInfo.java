package com.yuyh.library.simplemail.bean;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class LoginInfo implements Serializable {

    /**
     * 发件服务器地址和端口
     */
    public String mailServerHost;
    public String mailServerPort = "25";
    /**
     * 发件用户名与密码
     */
    public String userName;
    public String password;
    /**
     * 是否需要身份验证
     */
    public boolean validate = false;

    /**
     * 获得邮件会话属性
     */
    public Properties getProperties() {
        Properties p = new Properties();
        p.put("mail.smtp.host", this.mailServerHost);
        p.put("mail.smtp.port", this.mailServerPort);
        p.put("mail.smtp.auth", validate ? "true" : "false");
        return p;
    }
}
