package com.yuyh.simplemail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.yuyh.library.simplemail.MailHelper;
import com.yuyh.library.simplemail.bean.LoginInfo;

import javax.mail.Session;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;

    private EditText etMail;
    private EditText etPwd;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.login_progress);

        etMail = (EditText) findViewById(R.id.email);
        etPwd = (EditText) findViewById(R.id.password);

        btnLogin = (Button) findViewById(R.id.email_sign_in_button);
    }

    public void login(View view) {
        String mail = etMail.getText().toString().trim();
        String pwd = etPwd.getText().toString().trim();

        new LoginTask().execute(mail, pwd);
    }

    class LoginTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String address = params[0];
            String pwd = params[1];
            String host = "smtp." + address.substring(address.lastIndexOf("@") + 1);

            final LoginInfo info = new LoginInfo();
            info.mailServerHost = host;
            info.mailServerPort = "25";
            info.userName = address;
            info.password = pwd;
            info.validate = true;
            Session session = MailHelper.getInstance(LoginActivity.this).login(info);
            if (session != null) {
                MailApp.session = session;
                MailApp.info = info;
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }
}

