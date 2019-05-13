package com.example.user;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class data extends AppCompatActivity {
    TextView age,gender,input;
    ListView list;
    SharedPreferences preferences;
    PackageManager packageManager;
    UsageStatsManager usagestats;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.data);
preferences=getSharedPreferences("Myprefs",Context.MODE_PRIVATE);
    age=findViewById(R.id.agedata);
    gender=findViewById(R.id.genderdata);
    input=findViewById(R.id.inputdata);
    list=findViewById(R.id.list);
packageManager=getPackageManager();
usagestats=(UsageStatsManager)getSystemService(Context.USAGE_STATS_SERVICE);
    age.setText("Age: "+ preferences.getString("age",""));
    gender.setText("Gender: "+preferences.getString("gender",""));
    input.setText("Edu/Prof field: "+preferences.getString("input",""));
        List<UsageStats> usage=usagestats.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,System.currentTimeMillis()-43200000,System.currentTimeMillis());
        Iterator<UsageStats> iterator=usage.iterator();
        UsageStats  usagestat;
        List<UsageStats> newusagestats=new ArrayList<>();
        while(iterator.hasNext()){
            usagestat=iterator.next();
            if(packageManager.getLaunchIntentForPackage(usagestat.getPackageName())!=null){
                newusagestats.add(usagestat);
            }
        }


        CustomListAdapter itemsAdapter= new CustomListAdapter(this,newusagestats);

        list.setAdapter(itemsAdapter);



    }
}
