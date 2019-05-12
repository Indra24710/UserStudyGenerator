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
import android.widget.Toast;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.PackageManager;



public class MainActivity extends AppCompatActivity {
Button schedule,cancel;
Context context;
SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
context=this;
    schedule=findViewById(R.id.schedule);
    cancel=findViewById(R.id.cancel);
    preferences=getSharedPreferences("Myprefs",Context.MODE_PRIVATE);
    preferences.getString(" l","");
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(
                Manifest.permission.PACKAGE_USAGE_STATS,
                context.getPackageName());
        if (hasPerm != PackageManager.PERMISSION_GRANTED) {
            Intent i=new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(i);
        }



schedule.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        setSchedule();
    }
});
cancel.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        setCancel();
    }
});
    }

    public void setSchedule(){
        ComponentName componentName=new ComponentName(this,test.class);
        JobInfo.Builder builder = new JobInfo.Builder(123, componentName);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
        builder.setPeriodic(15 * 60 * 1000);
       builder.setRequiresCharging(true);
        JobInfo info = builder.build();
       JobScheduler scheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
       scheduler.schedule(info);
//        Toast.makeText(this,"JOb scheduled",Toast.LENGTH_LONG).show();

    }

    public void setCancel(){
        JobScheduler scheduler=(JobScheduler)getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        //Toast.makeText(this,"JOb cancelled",Toast.LENGTH_LONG).show();
    }









}
