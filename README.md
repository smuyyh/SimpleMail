# SimpleMail
### 一个基于POP3和SMTP协议的邮件客户端。


## 登录
```java
String address = "smuyyh@126.com";
String pwd = "xxxxxxxxxx";
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
```

## 收件箱
```java
```

## 发送邮件
```java
```