package com.yuyh.library.simplemail.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class Email implements Serializable {

    public String messageID;
    public String from;
    public String to;
    public String cc;
    public String bcc;
    public String subject;
    public String sentdata;
    public String content;
    public boolean replysign;
    public boolean html;
    public boolean news;
    public ArrayList<String> attachments;
    public String charset;

}