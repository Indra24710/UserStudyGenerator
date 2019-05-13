package com.example.user;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.content.Context;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.ImageView;
import android.widget.TextView;


import java.util.Iterator;
import java.util.List;



class CustomListAdapter extends ArrayAdapter<UsageStats> {

    private PackageManager packageManager;
    CustomListAdapter(Context context, List<UsageStats> usageStats) {

        super(context, R.layout.listitem, usageStats);
        packageManager = context.getPackageManager();

    }
    public View getView(int position, View convertView, ViewGroup parent){


        LayoutInflater layoutInflater= LayoutInflater.from(getContext());
        View view= layoutInflater.inflate(R.layout.listitem,parent,false);

        TextView appname=(TextView)view.findViewById(R.id.Appname);
        TextView run=(TextView)view.findViewById(R.id.run);
        ImageView appicon=view.findViewById(R.id.appicon);

        if(((getItem(position).getTotalTimeInForeground())/1000)>30){
            appname.setTextColor(Color.RED);
        }else{
            appname.setTextColor(Color.rgb(47,160,30));
        }
        String s = String.format("%.2f",(getItem(position).getTotalTimeInForeground())/(60000.0) );
        run.setText("Total Usage Time: "+s+" mins");
        try {
            appicon.setImageDrawable(packageManager.getApplicationIcon(getItem(position).getPackageName()));
            appname.setText(packageManager.getApplicationLabel(packageManager.getApplicationInfo(getItem(position).getPackageName(),PackageManager.GET_META_DATA)).toString());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;





    }

}


