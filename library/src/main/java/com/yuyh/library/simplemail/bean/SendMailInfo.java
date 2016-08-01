package com.yuyh.library.simplemail.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class SendMailInfo implements Serializable {
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
}
