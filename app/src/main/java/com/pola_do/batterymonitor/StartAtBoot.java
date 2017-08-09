package com.pola_do.batterymonitor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pola_do.batterymonitor.BatteryMonitorService;

/**
 * Created by PolaDo on 7/22/2017.
 */

public class StartAtBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent serviceIntent = new Intent(context, BatteryMonitorService.class);
            context.startService(serviceIntent);
        }
    }
}
