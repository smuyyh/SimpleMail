package com.yuyh.library.simplemail;

import android.content.Context;

import com.yuyh.library.simplemail.bean.Attachment;
import com.yuyh.library.simplemail.bean.LoginInfo;
import com.yuyh.library.simplemail.bean.ReceiverMailInfo;
import com.yuyh.library.simplemail.bean.SendMailInfo;
import com.yuyh.library.simplemail.constant.Constants;
import com.yuyh.library.simplemail.utils.MailAuthenticator;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

/**
 * @author yuyh.
 * @date 2016/8/1.
 */
public class MailHelper {

    private static MailHelper instance;
    private Context context;
    private List<ReceiverMailInfo> mailList;
    private HashMap<String, Integer> serviceHashMap;

    public static MailHelper getInstance(Context context) {
        if (instance == null)
            instance = new MailHelper(context);
        return instance;
    }

    public MailHelper(Context context) {
        this.context = context;
    }

    /**
     * 邮箱登录
     *
     * @param info
     * @return
     */
    public Session login(LoginInfo info) {
        // 创建密码验证器
        MailAuthenticator authenticator = null;
        if (info.validate) {
            authenticator = new MailAuthenticator(info.userName, info.password);
        }

        Session session = Session.getDefaultInstance(info.getProperties(), authenticator);

        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(info.mailServerHost, info.userName, info.password);
        } catch (MessagingException e) {
            e.printStackTrace();
            return null;
        }

        return session;
    }


    /**
     * 发送邮件
     *
     * @param mailInfo
     * @param session
     * @return
     */
    public boolean sendMail(SendMailInfo mailInfo, Session session) {
        try {
            Message mailMessage = new MimeMessage(session);

            // 发件人
            Address fromAddress = new InternetAddress(mailInfo.fromAddress);
            mailMessage.setFrom(fromAddress);

            // 收件人
            Address[] toAddress = null;
            String[] receivers = mailInfo.receivers;
            if (receivers != null) {
                toAddress = new InternetAddress[receivers.length];
                for (int i = 0; i < receivers.length; i++) {
                    toAddress[i] = new InternetAddress(receivers[i]);
                }
                mailMessage.setRecipients(Message.RecipientType.TO, toAddress);
            } else {
                return false;
            }

            mailMessage.setSubject(mailInfo.subject);
            mailMessage.setSentDate(new Date());

            Multipart multipart = new MimeMultipart();
            BodyPart mbpContent = new MimeBodyPart();
            mbpContent.setContent(mailInfo.content, "text/html;charset=gb2312");// 给BodyPart对象设置内容和格式/编码方式
            multipart.addBodyPart(mbpContent);

            FileDataSource fds;
            List<Attachment> list = mailInfo.attachmentInfos;
            if(list != null && !list.isEmpty()) {
                for (Attachment atta : list) {
                    fds = new FileDataSource(atta.filePath);
                    BodyPart mbpFile = new MimeBodyPart();
                    mbpFile.setDataHandler(new DataHandler(fds));
                    try {
                        mbpFile.setFileName(MimeUtility.encodeText(fds.getName()));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    multipart.addBodyPart(mbpFile);
                }
            }
            mailMessage.setContent(multipart);
            mailMessage.saveChanges();

            // 设置邮件支持多种格式
            MailcapCommandMap mcm = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
            mcm.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
            mcm.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
            mcm.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
            mcm.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
            mcm.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
            CommandMap.setDefaultCommandMap(mcm);

            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<ReceiverMailInfo> getAllMail(Constants.MailFolder mailFolder, LoginInfo info, Session session) throws MessagingException {
        List<ReceiverMailInfo> mailList = new ArrayList<>();
        // 连接服务器
        Store store = session.getStore("pop3");
        String temp = info.mailServerHost;
        String host = temp.replace("smtp", "pop");
        store.connect(host, info.userName, info.password);
        // 打开文件夹
        Folder folder = store.getFolder(mailFolder.value);
        folder.open(Folder.READ_ONLY);
        // 总的邮件数
        int mailCount = folder.getMessageCount();
        if (mailCount == 0) {
            folder.close(true);
            store.close();
            return null;
        } else {
            // 取得所有的邮件
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                // 自定义的邮件对象
                ReceiverMailInfo reciveMail = new ReceiverMailInfo((MimeMessage) messages[i]);
                mailList.add(reciveMail);// 添加到邮件列表中
            }
            return mailList;
        }
    }
}
