package com.example.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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



public class MainActivity extends AppCompatActivity {
Button schedule,record,idea,data,details;
TextView age,gender,input;
Context context;
AlertDialog.Builder builder;
SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //eliminating title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);//will hide the title
        getSupportActionBar().hide(); //hide the title bar



        setContentView(R.layout.main);
    context=this;


   //initializing views
    schedule=findViewById(R.id.schedule);
details=findViewById(R.id.details);
    record=findViewById(R.id.record);
    data=findViewById(R.id.data);
    idea=findViewById(R.id.idea);
    age=findViewById(R.id.age);
    gender=findViewById(R.id.gender);
    input=findViewById(R.id.input);

        preferences=getSharedPreferences("Myprefs",Context.MODE_PRIVATE);
         editor=preferences.edit();

        preferences = getSharedPreferences("Myprefs", Context.MODE_PRIVATE);


         //getting the user details
    record.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            editor.clear();
            editor.putString("age",age.getText().toString());
            editor.putString("gender",gender.getText().toString());
            editor.putString("input",input.getText().toString());
            editor.commit();
            Toast.makeText(context,"Details has been recorded",Toast.LENGTH_LONG).show();
        }
    });


        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.PACKAGE_USAGE_STATS,
                context.getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            Intent i=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(i);
        }




        //showing the user their recorded data
        data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,data.class);
                startActivity(i);
            }
        });


//getting user ideas

idea.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i=new Intent(MainActivity.this,idea.class);
        startActivity(i);
    }
});



//starting the job
schedule.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        Toast.makeText(context,"Please leave the app to run in the background for few mins",Toast.LENGTH_LONG).show();

        sendmail obj=new sendmail();
obj.execute();

        setSchedule();

    }
});


//terms and conditions alert
builder=new AlertDialog.Builder(MainActivity.this);
details.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

builder.setMessage("The primary aim of this app is to collect and send your app usage data to us. We strongly support privacy and so the data we collect is strictly limited to the total duration for which you use a particular app in a day and for the past week and basic details such as your age, gender and field of occupation or education. You can always view the data that is being sent from your phone by clicking the \"Data\" button which will show you exactly that very data that we get to see. This small data collection drill is a part of something bigger, an initiative to find a method to ensure the \"Responsible use of Mobile Phones\" . At the end of the day its completely your choice , you can opt out of this initiative by simple uninstalling this app or you can contribute to this cause by first giving us \"UsageStat\" access in the Settings screen that appears when opening the app and then hit the \"Send\" button below after recording your details in the fields below .After this all you have to do is nothing , just close the app and thats it , you have already played a major role by helping us in understanding the different usage patterns of different users by sending us your data. You can also further support us by hitting the  \"Idea\" button to send us your own ideas about how we can make \"Using the Smartphone\"more responsible. Thanks a lot for your support.");
builder.setPositiveButton("Cool", new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
    }
});
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Hola!");
        alert.show();

    }
});

    }

    public void setSchedule(){
        ComponentName componentName=new ComponentName(this,test.class);
        JobInfo.Builder builder = new JobInfo.Builder(123, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
       builder.setRequiresCharging(true);
        //builder.setRequiresDeviceIdle(true);
        builder.setPeriodic(15 * 60 * 1000);
        JobInfo info = builder.build();
       JobScheduler scheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
       scheduler.schedule(info);

    }



    //async task to send weekly data

    public  class sendmail extends AsyncTask<String,String,Void> {
        public PackageManager packageManager;
        public UsageStatsManager usagestats;
        Session session;
        SharedPreferences preferences;
        String mon,tue,wed,thurs,fri,sat,sun;

        @Override
        protected Void doInBackground(String... strings) {

            preferences = getSharedPreferences("Myprefs", Context.MODE_PRIVATE);
           packageManager = getPackageManager();
            usagestats = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

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
            List<UsageStats> usage2 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000*2, System.currentTimeMillis()-86400000);
            List<UsageStats> usage3 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000*3, System.currentTimeMillis()-86400000*2);
            List<UsageStats> usage4 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000*4, System.currentTimeMillis()-86400000*3);
            List<UsageStats> usage5 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000*5, System.currentTimeMillis()-86400000*4);
            List<UsageStats> usage6 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000*6, System.currentTimeMillis()-86400000*5);
            List<UsageStats> usage7 = usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, System.currentTimeMillis() - 86400000*7, System.currentTimeMillis()-86400000*6);
            NumberFormat formatter = new DecimalFormat("#0.00");

            Iterator<UsageStats> iterator = usage1.iterator();
            UsageStats usagestat = null;
            try{
                mon="";
                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        mon = mon + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0)) +" ";
                    }
                }

                tue="";
                iterator=usage2.iterator();
                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        tue = tue + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0))+" " ;
                    }
                }


                wed="";
                iterator=usage3.iterator();
                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        wed = wed + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0)) +" ";
                    }
                }


                thurs="";
                iterator=usage4.iterator();
                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        thurs=thurs + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0)) +" ";;
                    }
                }


                fri="";
                iterator=usage5.iterator();

                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        fri = fri + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0)) +" ";
                    }
                }



                iterator=usage6.iterator();
                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        sat = sat + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0)) +" ";
                    }
                }




                iterator=usage7.iterator();
                while (iterator.hasNext()) {
                    usagestat = iterator.next();
                    if (packageManager.getLaunchIntentForPackage(usagestat.getPackageName()) != null) {
                        sun = sun + " " + packageManager.getApplicationLabel(packageManager.getApplicationInfo(usagestat.getPackageName(), PackageManager.GET_META_DATA)).toString() + " " + formatter.format(usagestat.getTotalTimeInForeground()/(60000.0))+" " ;
                    }
                }}catch (PackageManager.NameNotFoundException e){
                e.printStackTrace();
            }


            MimeMessage message = new MimeMessage(session);

            try {
                message.setFrom(new InternetAddress("indra.kumar17326@gmail.com"));

                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("indra.kumar24710@gmail.com"));

                message.setSubject("Age: " + preferences.getString("age", " ") + " Gender: " + preferences.getString("gender", " ") + " Edu/Prof field: " + preferences.getString("input", " "));
               message.setContent(" Day1: "+mon+" /n  day2: "+tue+" /n day3: "+wed+" /n day4:  "+thurs+" /n  day5: "+fri+" /n day6: "+sat+" /n  day7: "+sun, "text/html; charset=utf-8");

                Transport.send(message);

            } catch (MessagingException e) {
                e.printStackTrace();
            }

            //  }
            return null;
        }
    }







}
