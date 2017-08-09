package com.pola_do.batterymonitor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import static android.app.Notification.CATEGORY_SERVICE;
import static android.app.Notification.PRIORITY_HIGH;

/**
 * Created by PolaDo on 7/4/2017.
 */

public class BatteryMonitorService extends Service {
    int level, scale, status, percent, chargePlug, temperature;
    String outStatus, plug, temp;
    float batteryPct;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(batteryReceiver);
    }

    void updateBatteryStatus() {
        batteryPct = level / (float) scale;
        percent = (int) (batteryPct * 100);

        plug = "";
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            plug = "(USB)";
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            plug = "(AC)";
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            plug = "(Wireless)";
        }

//        System.out.println(BatteryManager.BATTERY_STATUS_FULL);
        outStatus = "Not Charging";
        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            outStatus = "Buttery is full";
        } else if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            outStatus = "Charging";
        }

        outStatus += " " + plug;
        outStatus = outStatus.trim();

        temp = "[" + ((float) temperature / 10) + Character.toString((char) 176) + "C]";

        notification();
    }

    void notification() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setContentTitle(outStatus)
//                        .setSmallIcon(R.drawable.ic_stat_100)
                        .setContentText(percent + "% " + temp)
                        .setContentIntent(resultPendingIntent)
                        .setCategory(CATEGORY_SERVICE)
                        .setPriority(PRIORITY_HIGH)
                        .setColor(new GetTheme(BatteryMonitorService.this).getThemeAccentColor())
//                        .setColor(getResources().getColor(R.color.colorAccent2))
//                        .setSubText("This is sub text")
//                        .setStyle(new NotificationCompat.BigTextStyle()
//                                .bigText("This text replaces the notification’s default text"))
                        .setOngoing(true);

        mBuilder.setSmallIcon(getResources().getIdentifier("ic_stat_" + percent, "drawable", this.getPackageName()));

        int mNotificationId = 001;
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent batteryStatus) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
            batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

            updateBatteryStatus();
        }
    };

    private int getThemeAccentColor() {
        int accentColor;
        int theme = readTheme();
        switch (theme) {
            case R.id.theme_blue:
                accentColor = getResources().getColor(R.color.colorAccentBlue);
                break;
            case R.id.theme_default:
                accentColor = getResources().getColor(R.color.colorAccent2);
                break;
            case R.id.theme_indigo:
                accentColor = getResources().getColor(R.color.colorAccentIndigo);
                break;
            case R.id.theme_pink:
                accentColor = getResources().getColor(R.color.colorAccentPink);
                break;
            case R.id.theme_purple:
                accentColor = getResources().getColor(R.color.colorAccentPurple);
                break;
            case R.id.theme_red:
                accentColor = getResources().getColor(R.color.colorAccentRed);
                break;
            case R.id.theme_teal:
                accentColor = getResources().getColor(R.color.colorAccentTeal);
                break;
            case R.id.theme_grey:
                accentColor = getResources().getColor(R.color.colorAccentGrey);
                break;
            case R.id.theme_blue_grey:
                accentColor = getResources().getColor(R.color.colorAccentBlueGrey);
                break;
            case R.id.theme_black:
                accentColor = getResources().getColor(R.color.colorAccentBlack);
                break;
            case R.id.theme_dark_blue:
                accentColor = getResources().getColor(R.color.colorAccentDarkBlue);
                break;
            default:
                accentColor = getResources().getColor(R.color.colorAccent2);
                break;
        }
        return accentColor;
    }

    private int readTheme() {
        String fileName = "theme";
        String ret = "-1";

        try {
            InputStream inputStream = openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("theme activity", "File not found: " + e.toString());
            ret = "-1";
        } catch (IOException e) {
            Log.e("theme activity", "Can not read file: " + e.toString());
            ret = "-1";
        }

        return Integer.parseInt(ret);
    }
}
