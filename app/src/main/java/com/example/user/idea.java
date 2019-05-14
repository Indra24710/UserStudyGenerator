package com.example.user;


import android.content.Context;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class idea extends AppCompatActivity {
   Button sendidea;
   public TextView inputidea;
   public Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.idea);
    context=this;
    sendidea=findViewById(R.id.sendidea);
    inputidea=findViewById(R.id.ideainput);
    sendidea.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

send obj=new send();
obj.execute();
            Toast.makeText(context,"Thanks a lot for your suggestions",Toast.LENGTH_LONG).show();

        }



    });



    }





    public  class send extends AsyncTask<String,String,Void> {

        Session session;


        @Override
        protected Void doInBackground(String... strings) {


            Properties prop = new Properties();
            prop.put("mail.smtp.host", "smtp.gmail.com");
            prop.put("mail.smtp.socketFactory.port", "465");
            prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.port", "465");

            session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("indra.kumar17326@gmail.com", "iloveGMAIL.COM");
                }
            });



            MimeMessage message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress("indra.kumar17326@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("indra.kumar24710@gmail.com,anjur99@gmail.com,aradhanajayaprakash@yahoo.com"));
                message.setSubject("Ideas/Suggestions");
                message.setContent(inputidea.getText().toString(), "text/html; charset=utf-8");
                Transport.send(message);
                inputidea.setText("");


            } catch (MessagingException e) {
                e.printStackTrace();
            }

            //  }
            return null;
        }
    }
}
