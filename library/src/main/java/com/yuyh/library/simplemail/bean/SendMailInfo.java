package com.yuyh.library.simplemail.bean;

import java.io.Serializable;
import java.util.List;
import java.util.Properties;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class SendMailInfo implements Serializable {

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
     * 发件人地址
     */
    public String fromAddress;
    /**
     * 邮件主题
     */
    public String subject;
    /**
     * 邮件内容
     */
    public String content;
    /**
     * 邮件附件
     */
    public List<Attachment> attachmentInfos;
    /**
     * 邮件的接收者（可以有多个）
     */
    public String[] receivers;

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
