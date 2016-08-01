package com.yuyh.library.simplemail.bean;

import com.yuyh.library.simplemail.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class ReceiverMailInfo implements Serializable {

    private MimeMessage mimeMessage = null;
    private StringBuffer mailContent = new StringBuffer();// 邮件内容
    private String dataFormat = "yyyy-MM-dd HH:mm:ss";
    private String charset;
    private boolean html;
    private ArrayList<String> attachments = new ArrayList<String>();
    private ArrayList<InputStream> attachmentsInputStreams = new ArrayList<InputStream>();

    public ReceiverMailInfo(MimeMessage mimeMessage) {
        this.mimeMessage = mimeMessage;
        try {
            charset = parseCharset(mimeMessage.getContentType());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析邮件字符集编码
     *
     * @param contentType
     * @return
     */
    private String parseCharset(String contentType) {
        if (!contentType.contains("charset")) {
            return null;
        }
        if (contentType.contains("gbk")) {
            return "GBK";
        } else if (contentType.contains("GB2312") || contentType.contains("gb18030")) {
            return "gb2312";
        } else {
            String sub = contentType.substring(contentType.indexOf("charset") + 8).replace("\"", "");
            if (sub.contains(";")) {
                return sub.substring(0, sub.indexOf(";"));
            } else {
                return sub;
            }
        }
    }

    /**
     * 解析流
     *
     * @param is
     * @return
     * @throws IOException
     * @throws MessagingException
     */
    public String parseInputStream(InputStream is) throws IOException, MessagingException {
        StringBuffer str = new StringBuffer();
        byte[] readByte = new byte[1024];
        int count;
        try {
            while ((count = is.read(readByte)) != -1) {
                if (charset == null) {
                    str.append(new String(readByte, 0, count, "GBK"));
                } else {
                    str.append(new String(readByte, 0, count, charset));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    /**
     * 获取发件人姓名和地址
     *
     * @return
     * @throws Exception
     */
    public String getFrom() throws Exception {
        InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
        String addr = address[0].getAddress();
        String name = address[0].getPersonal();
        if (addr == null) {
            addr = "";
        }
        if (name == null) {
            name = "";
        } else if (charset == null) {
            name = StringUtils.encodeGBK(name);
        }
        String nameAddr = name + "<" + addr + ">";
        return nameAddr;
    }

    /**
     * 获取收件人姓名和地址
     *
     * @param type TO:收件人
     *             CC:抄送人
     *             BCC:密送人
     * @return
     * @throws Exception
     */
    public String getMailAddress(Message.RecipientType type) throws Exception {
        String mailAddr = "";
        InternetAddress[] address = (InternetAddress[]) mimeMessage.getRecipients(type);
        if (address != null) {
            for (int i = 0; i < address.length; i++) {
                String mailaddress = address[i].getAddress();
                if (mailaddress != null) {
                    mailaddress = MimeUtility.decodeText(mailaddress);
                } else {
                    mailaddress = "";
                }
                String name = address[i].getPersonal();
                if (name != null) {
                    name = MimeUtility.decodeText(name);
                } else {
                    name = "";
                }
                mailAddr = name + "<" + mailaddress + ">";
            }
        }
        return mailAddr;
    }

    /**
     * 获取邮件标题
     *
     * @return
     */
    public String getSubject() {
        String subject = "";
        try {
            subject = mimeMessage.getSubject();
            if (subject.indexOf("=?gb18030?") != -1) {
                subject = subject.replace("gb18030", "gb2312");
            }
            subject = MimeUtility.decodeText(subject);
            if (charset == null) {
                subject = StringUtils.encodeGBK(subject);
            }
        } catch (Exception e) {
        }
        return subject;
    }

    /**
     * 取得邮件日期
     *
     * @throws MessagingException
     */
    public String getSentData() throws MessagingException {
        Date sentdata = mimeMessage.getSentDate();
        if (sentdata != null) {
            SimpleDateFormat format = new SimpleDateFormat(dataFormat, Locale.CHINA);
            return format.format(sentdata);
        } else {
            return "未知";
        }
    }

    /**
     * 取得邮件内容
     *
     * @throws Exception
     */
    public String getMailContent() throws Exception {
        compileMailContent((Part) mimeMessage);
        String content = mailContent.toString();
        if (content.indexOf("<html>") != -1) {
            html = true;
        }
        mailContent.setLength(0);
        return content;
    }

    private void compileMailContent(Part part) throws Exception {
        String contentType = part.getContentType();
        boolean connName = false;
        if (contentType.indexOf("name") != -1) {
            connName = true;
        }
        if (part.isMimeType("text/plain") && !connName) {
            //String content = parseInputStream((InputStream) part.getContent());
            String content = part.getContent().toString();
            mailContent.append(content);
        } else if (part.isMimeType("text/html") && !connName) {
            html = true;
            //String content = parseInputStream((InputStream) part.getContent());
            String content = part.getContent().toString();
            mailContent.append(content);
        } else if (part.isMimeType("multipart/*") || part.isMimeType("message/rfc822")) {
            if (part.getContent() instanceof Multipart) {
                Multipart multipart = (Multipart) part.getContent();
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    compileMailContent(multipart.getBodyPart(i));
                }
            } else {
                Multipart multipart = new MimeMultipart(new ByteArrayDataSource(part.getInputStream(), "multipart/*"));
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    compileMailContent(multipart.getBodyPart(i));
                }
            }
        } else if (part.getDisposition() != null && part.getDisposition().equals(Part.ATTACHMENT)) {
            // 获取附件
            String filename = part.getFileName();
            if (filename != null) {
                if (filename.indexOf("=?gb18030?") != -1) {
                    filename = filename.replace("gb18030", "gb2312");
                }
                filename = MimeUtility.decodeText(filename);
                attachments.add(filename);
                attachmentsInputStreams.add(part.getInputStream());
            }
        }
    }

    /**
     * 是否有回执
     *
     * @throws MessagingException
     */
    public boolean getReplySign() throws MessagingException {
        boolean replySign = false;
        String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
        if (needreply != null) {
            replySign = true;
        }
        return replySign;
    }

    /**
     * 获取「message-ID」
     *
     * @throws MessagingException
     */
    public String getMessageID() throws MessagingException {
        return mimeMessage.getMessageID();
    }

    /**
     * 是否新邮件
     *
     * @throws MessagingException
     */
    public boolean isNew() throws MessagingException {
        boolean isnew = false;
        Flags flags = ((Message) mimeMessage).getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                break;
            }
        }
        return isnew;
    }
}
