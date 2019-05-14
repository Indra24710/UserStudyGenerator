package com.example.user;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.AsyncTask;
import android.util.Log;


import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class test extends JobService {
public Context context;

    @Override
    public boolean onStartJob(JobParameters params) {
        context=this;
        Log.d("test","startjob func");
       // Toast.makeText(this,"JOb started",Toast.LENGTH_LONG).show();
        sendmail obj=new sendmail();
        obj.execute();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("test","JOb stopped");
        //Toast.makeText(this,"JOb stopped",Toast.LENGTH_LONG).show();
        return true;
    }



public  class sendmail extends AsyncTask<String,String,Void> {
    public PackageManager packageManager;
    public UsageStatsManager usagestats;
    Session session;
    SharedPreferences preferences;
    String mon,tue,wed,thurs,fri,sat,sun;

    @Override
    protected Void doInBackground(String... strings) {


            packageManager = getPackageManager();
            usagestats = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
            preferences = getSharedPreferences("Myprefs", Context.MODE_PRIVATE);
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

            List<UsageStats> usage1 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000, System.currentTimeMillis());

        Iterator<UsageStats> iterator = usage1.iterator();
            UsageStats usagestat = null;
            try{
            mon="";
            while (iterator.hasNext()) {
                usagestat = iterator.next();
                if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                    mon = mon + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + String.format("%.2f",usagestat.getTotalTimeInForeground()/(60000.0) );
                }
            }}catch(PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }




            MimeMessage message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress("indra.kumar17326@gmail.com"));

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("indra.kumar24710@gmail.com"));

                message.setSubject("Age: " + preferences.getString("age", " ") + " Gender: " + preferences.getString("gender", " ") + " Edu/Prof field: " + preferences.getString("input", " "));
                message.setContent(" " +mon+" ","text/html; charset=utf-8");
                Transport.send(message);




            } catch (MessagingException e) {
                e.printStackTrace();
            }

     //  }
        return null;
    }
}}

