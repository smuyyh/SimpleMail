# SimpleMail
### 一个基于POP3和SMTP协议的邮件客户端。


## 登录
```java
String address = "smuyyh@126.com";
String pwd = "xxxxxxxxxx";
String host = "smtp." + address.substring(address.lastIndexOf("@") + 1);

final LoginInfo info = new LoginInfo();
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
            // 登录成功
        }
    }
}).start();
```

## 收件箱
```java
try {
    List<ReceiverMailInfo> list = MailHelper.getInstance(MainActivity.this).getAllMail(Constants.MailFolder.INBOX, info, session);
    Log.i("TAG", list.size()+"");
} catch (MessagingException e) {
        e.printStackTrace();
}

```

## 发送邮件
```java
SendMailInfo info = new SendMailInfo();
info.fromAddress = address;
info.subject = "测试邮件";
info.content = "测试邮件 内容";
info.receivers = new String[]{"352091626@qq.com"};
MailHelper.getInstance(MainActivity.this).sendMail(info, session);
```