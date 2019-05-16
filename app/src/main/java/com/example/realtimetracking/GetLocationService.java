package com.example.realtimetracking;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.google.android.gms.location.LocationResult;

public class GetLocationService extends BroadcastReceiver
{
    public static String string="com.example.realtimetracking.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent!=null)
        {
            String action=intent.getAction();
            if(string.equals(action))
            {
                LocationResult locationResult=LocationResult.extractResult(intent);
                if(locationResult!=null)
                {
                    Location location=locationResult.getLastLocation();
                    String value=location.getLatitude()+" , "+location.getLongitude();
                    AfterLogin2.getInstance().updateTextView(value);
                }
            }
        }

    }
}