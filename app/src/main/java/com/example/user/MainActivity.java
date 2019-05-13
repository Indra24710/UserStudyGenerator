package com.example.user;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
Button schedule,record,idea,data;
TextView age,gender,input;
Context context;
SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
context=this;
    schedule=findViewById(R.id.schedule);

    record=findViewById(R.id.record);
    data=findViewById(R.id.data);
    idea=findViewById(R.id.idea);
    age=findViewById(R.id.age);
    gender=findViewById(R.id.gender);
    input=findViewById(R.id.input);
        preferences=getSharedPreferences("Myprefs",Context.MODE_PRIVATE);
         editor=preferences.edit();



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






schedule.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,17,00,00);
        Calendar calendar1 =Calendar.getInstance();
        calendar1.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,17,00,00);
        long t1,t2;

        t1= calendar.getTimeInMillis();

        Log.d("test",String.valueOf(t1));
        t2=calendar1.getTimeInMillis();
        Log.d("test",String.valueOf(System.currentTimeMillis()));
        Log.d("test",String.valueOf(t2));
        setSchedule();
    }
});

    }

    public void setSchedule(){
        ComponentName componentName=new ComponentName(this,test.class);
        JobInfo.Builder builder = new JobInfo.Builder(123, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setRequiresCharging(true);
        builder.setPeriodic(6 * 60* 60 * 1000);
        JobInfo info = builder.build();
       JobScheduler scheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
       scheduler.schedule(info);
       Toast.makeText(this,"Thank you, you can close this app now",Toast.LENGTH_LONG).show();

    }

    public void setCancel(){
        JobScheduler scheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        //Toast.makeText(this,"JOb cancelled",Toast.LENGTH_LONG).show();
    }









}
