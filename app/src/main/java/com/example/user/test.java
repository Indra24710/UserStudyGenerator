package com.example.user;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
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
        Toast.makeText(this,"JOb started",Toast.LENGTH_LONG).show();
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,17,00,00);
        Calendar calendar1 =Calendar.getInstance();
        calendar1.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,19,00,00);
        long t1,t2;

        t1= calendar.getTimeInMillis();
        t2=calendar1.getTimeInMillis();
       // if((System.currentTimeMillis()>t1)&&(System.currentTimeMillis()<t2)) {
            Toast.makeText(this, "this works", Toast.LENGTH_LONG).show();
            sendmail obj=new sendmail();
            obj.execute();
            Log.d("test","job should have been completed");
      //  }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.d("test","JOb stopped");
        Toast.makeText(this,"JOb stopped",Toast.LENGTH_LONG).show();
        return true;
    }



public  class sendmail extends AsyncTask<String,String,Void> {
    public PackageManager packageManager;
    public UsageStatsManager usagestats;
    Session session;

    String s;

    @Override
    protected Void doInBackground(String... strings) {
        packageManager=getPackageManager();
        usagestats=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
        Properties prop=new Properties();
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.port","465");

        session= Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("inder.destination@gmail.com","ilovealltheuniverses");
            }
        });



        List<UsageStats> usage=usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,System.currentTimeMillis()-43200000,System.currentTimeMillis());
        Iterator<UsageStats> iterator=usage.iterator();
        UsageStats  usagestat=null;
        List<UsageStats> newusagestats=new ArrayList<>();
        while(iterator.hasNext()){
            usagestat=iterator.next();
            if(packageManager.getLaunchIntentForPackage(usagestat.getPackageName())!=null){
                newusagestats.add(usagestat);
            }
        }
        s="";
        Iterator<UsageStats> it=newusagestats.iterator();
        while(it.hasNext()){
            usagestat=it.next();
            try {
                s=s+" "+packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString()+" "+usagestat.getTotalTimeInForeground();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress("inder.destination@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse("indra.kumar24710@gmail.com"));
            message.setSubject("subject");
            message.setContent(s,"text/html; charset=utf-8");

            Transport.send(message);
         //   MediaPlayer mediaPlayer= MediaPlayer.create(context,Settings.System.DEFAULT_RINGTONE_URI);
           // mediaPlayer.start();


        } catch (MessagingException e) {
            e.printStackTrace();
        }


        return null;
    }
}}

