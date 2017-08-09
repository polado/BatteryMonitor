package com.pola_do.batterymonitor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    TextView tvPercentageText;
    TextView tvPercentage, tvTemperature, tvStatus, tvHealth, tvVoltage, tvCurrent;

    int level, scale, status, percent, chargePlug, temperature, health;
    int tPercent = -1, tTemperature = -1, tStatus = -1, tVolt = -1, tHealth = -1;
    Long tCurr = (long) -1;
    int voltage;
    String technology;
    Long current = (long) 0;
    String volt, tech, curr;
    String outStatus, plug, temp, batteryHealth;
    float batteryPct;

    int accentColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        {
            setTheme(new GetTheme(MainActivity.this).getTheme());
//            int theme = readTheme();
//            switch (theme) {
//                case R.id.theme_blue:
//                    setTheme(R.style.AppThemeBlue);
//                    break;
//                case R.id.theme_default:
//                    setTheme(R.style.AppThemeDefault);
//                    break;
//                case R.id.theme_indigo:
//                    setTheme(R.style.AppThemeIndigo);
//                    break;
//                case R.id.theme_pink:
//                    setTheme(R.style.AppThemePink);
//                    break;
//                case R.id.theme_purple:
//                    setTheme(R.style.AppThemePurple);
//                    break;
//                case R.id.theme_red:
//                    setTheme(R.style.AppThemeRed);
//                    break;
//                case R.id.theme_teal:
//                    setTheme(R.style.AppThemeTeal);
//                    break;
//                case R.id.theme_grey:
//                    setTheme(R.style.AppThemeGrey);
//                    break;
//                case R.id.theme_blue_grey:
//                    setTheme(R.style.AppThemeBlueGrey);
//                    break;
//                case R.id.theme_black:
//                    setTheme(R.style.AppThemeBlack);
//                    break;
//                case R.id.theme_dark_blue:
//                    setTheme(R.style.AppThemeDarkBlue);
//                    break;
//                default:
//                    setTheme(R.style.AppThemeDefault);
//                    break;
//            }
            stopService(new Intent(this, BatteryMonitorService.class));
            startService(new Intent(this, BatteryMonitorService.class));
        }
        super.onCreate(savedInstanceState);

        Log.i("onCreate", "onCreate");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInMultiWindowMode()) {
                setContentView(R.layout.activity_main_2);
//                updateBatteryStatus();
            } else {
                setContentView(R.layout.activity_main);
//                updateBatteryStatus();
            }
        } else
            setContentView(R.layout.activity_main);

        accentColor = new GetTheme(MainActivity.this).getThemeAccentColor();
        setupCards();

        startService(new Intent(this, BatteryMonitorService.class));

        tvPercentageText = (TextView) findViewById(R.id.tv_percentage_text);

        initializeTV();

        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    void initializeTV() {
        tvPercentage = (TextView) findViewById(R.id.tv_percentage);
        tvTemperature = (TextView) findViewById(R.id.tv_temprature);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvHealth = (TextView) findViewById(R.id.tv_health);
        tvVoltage = (TextView) findViewById(R.id.tv_voltage);
        tvCurrent = (TextView) findViewById(R.id.tv_current);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("onStart", "onStart");
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);
        if (isInMultiWindowMode) {

            Log.i("onMultiWindowMode", "true");
            setContentView(R.layout.activity_main_2);
//            updateBatteryStatus();
//        recreate();
            setupCards();
        } else {
            Log.i("onMultiWindowMode", "false");
            setContentView(R.layout.activity_main);
//            updateBatteryStatus();
//        recreate();
            setupCards();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "onResume");
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("onPause", "onPause");
        unregisterReceiver(batteryReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.theme_blue:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_default:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_indigo:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_pink:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_purple:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_red:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_teal:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_grey:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_blue_grey:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_black:
                writeTheme(item.getItemId());
                recreate();
                return true;
            case R.id.theme_dark_blue:
                writeTheme(item.getItemId());
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void writeTheme(int theme) {
        String fileName = "theme";
        String data = String.valueOf(theme);

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void setupCards() {
        Log.i("setup", "setup");
        int orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE: {
                final CardView cardView2 = (CardView) findViewById(R.id.cardView2);
                cardView2.setCardBackgroundColor(accentColor);
                cardView2.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView2.setVisibility(View.INVISIBLE);
                        slideToTop(cardView2);
                    }
                });
                final CardView cardView3 = (CardView) findViewById(R.id.cardView3);
                cardView3.setCardBackgroundColor(accentColor);
                cardView3.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView3.setVisibility(View.INVISIBLE);
                        slideToTop(cardView3);
                    }
                });

                final CardView cardView4 = (CardView) findViewById(R.id.cardView4);
                cardView4.setCardBackgroundColor(accentColor);
                cardView4.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView4.setVisibility(View.INVISIBLE);
                        slideToTop(cardView4);
//                        final Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                slideToTop(cardView4);
//                            }
//                        }, 200);
                    }
                });
                final CardView cardView5 = (CardView) findViewById(R.id.cardView5);
                cardView5.setCardBackgroundColor(accentColor);
                cardView5.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView5.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView5);
                            }
                        }, 200);
                    }
                });

                final CardView cardView6 = (CardView) findViewById(R.id.cardView6);
                cardView6.setCardBackgroundColor(accentColor);
                cardView6.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView6.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView6);
                            }
                        }, 200);
                    }
                });
                final CardView cardView7 = (CardView) findViewById(R.id.cardView7);
                cardView7.setCardBackgroundColor(accentColor);
                cardView7.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView7.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView7);
                            }
                        }, 200);
                    }
                });
                break;
            }
            default: {
                final CardView cardView2 = (CardView) findViewById(R.id.cardView2);
                cardView2.setCardBackgroundColor(accentColor);
                cardView2.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView2.setVisibility(View.INVISIBLE);
                        slideToTop(cardView2);
                    }
                });
                final CardView cardView3 = (CardView) findViewById(R.id.cardView3);
                cardView3.setCardBackgroundColor(accentColor);
                cardView3.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView3.setVisibility(View.INVISIBLE);
                        slideToTop(cardView3);
                    }
                });

                final CardView cardView4 = (CardView) findViewById(R.id.cardView4);
                cardView4.setCardBackgroundColor(accentColor);
                cardView4.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView4.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView4);
                            }
                        }, 200);
                    }
                });
                final CardView cardView5 = (CardView) findViewById(R.id.cardView5);
                cardView5.setCardBackgroundColor(accentColor);
                cardView5.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView5.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView5);
                            }
                        }, 200);
                    }
                });

                final CardView cardView6 = (CardView) findViewById(R.id.cardView6);
                cardView6.setCardBackgroundColor(accentColor);
                cardView6.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView6.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView6);
                            }
                        }, 400);
                    }
                });
                final CardView cardView7 = (CardView) findViewById(R.id.cardView7);
                cardView7.setCardBackgroundColor(accentColor);
                cardView7.post(new Runnable() {
                    @Override
                    public void run() {
                        cardView7.setVisibility(View.INVISIBLE);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                slideToTop(cardView7);
                            }
                        }, 400);
                    }
                });
                break;
            }
        }
    }

    public static void slideToTop(View view) {
        TranslateAnimation animate = new TranslateAnimation(0, 0, view.getHeight(), 0);
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
        view.setVisibility(View.VISIBLE);
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

    private boolean currentChange() {
        if (!Objects.equals(current, tCurr)) {
            tCurr = current;
            return true;
        }
        return false;
    }

    private boolean voltageChange() {
        if (voltage != tVolt) {
            tVolt = voltage;
            return true;
        }
        return false;
    }

    private boolean healthChange() {
        if (health != tHealth) {
            tHealth = health;
            return true;
        }
        return false;
    }

    private boolean statusChange() {
        if (status != tStatus) {
            tStatus = status;
            return true;
        }
        return false;
    }

    private boolean temperatureChange() {
        if (temperature != tTemperature) {
            tTemperature = temperature;
            return true;
        }
        return false;
    }

    private boolean percentChange() {
        if (percent != tPercent) {
            tPercent = percent;
            return true;
        }
        return false;
    }

    void updateCards() {

        initializeTV();

        if (percentChange()) {
            tvPercentage.post(new Runnable() {
                @Override
                public void run() {
                    tvPercentage.setTextColor(accentColor);
                    tvPercentage.setText(String.valueOf(percent + "%"));
                    circularReveal(tvPercentage);
                }
            });
        }
        if (temperatureChange()) {
            tvTemperature.post(new Runnable() {
                @Override
                public void run() {
                    tvTemperature.setTextColor(accentColor);
                    tvTemperature.setText(temp);
                    circularReveal(tvTemperature);
                }
            });
        }
        if (statusChange()) {
            tvStatus.post(new Runnable() {
                @Override
                public void run() {
                    tvStatus.setTextColor(accentColor);
                    tvStatus.setText(outStatus);
                    circularReveal(tvStatus);
                }
            });
        }
        if (healthChange()) {
            tvHealth.post(new Runnable() {
                @Override
                public void run() {
                    tvHealth.setTextColor(accentColor);
                    tvHealth.setText(batteryHealth);
                    circularReveal(tvHealth);
                }
            });
        }
        if (voltageChange()) {
            tvVoltage.post(new Runnable() {
                @Override
                public void run() {
                    tvVoltage.setTextColor(accentColor);
                    tvVoltage.setText(String.valueOf(((double) voltage / 1000) + "V"));
                    circularReveal(tvVoltage);
                }
            });
        }
        if (currentChange()) {
            tvCurrent.post(new Runnable() {
                @Override
                public void run() {
                    tvCurrent.setTextColor(accentColor);
                    tvCurrent.setText(String.valueOf((current / 1000) + "mA"));
                    circularReveal(tvCurrent);
                }
            });
        }
    }

    void updateBatteryStatus() {
        batteryPct = level / (float) scale;
        percent = (int) (batteryPct * 100);
        if (health == BatteryManager.BATTERY_HEALTH_COLD)
            batteryHealth = "Cold";
        else if (health == BatteryManager.BATTERY_HEALTH_DEAD)
            batteryHealth = "Dead";
        else if (health == BatteryManager.BATTERY_HEALTH_GOOD)
            batteryHealth = "Good";
        else if (health == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE)
            batteryHealth = "Over Voltage";
        else if (health == BatteryManager.BATTERY_HEALTH_OVERHEAT)
            batteryHealth = "Overheat";
        else if (health == BatteryManager.BATTERY_HEALTH_UNKNOWN)
            batteryHealth = "Unknown";
        else if (health == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE)
            batteryHealth = "Unspecified Failure";

        plug = "";
        if (chargePlug == BatteryManager.BATTERY_PLUGGED_USB) {
            plug = "(USB)";
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_AC) {
            plug = "(AC)";
        } else if (chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            plug = "(Wireless)";
        }

        outStatus = "Not Charging";
        if (status == BatteryManager.BATTERY_STATUS_FULL) {
            outStatus = "Buttery is full";
        } else if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
            outStatus = "Charging";
        }

        outStatus += " " + plug;
        outStatus = outStatus.trim();

        temp = "[" + ((float) temperature / 10) + Character.toString((char) 176) + "C]";

        updateCards();
    }

    void circularReveal(final View v) {
        final int centerX = (v.getLeft() + v.getRight()) / 2;
        final int centerY = (v.getTop() + v.getBottom()) / 2;
        final float radius = Math.max(v.getWidth(), v.getHeight()) * 2.0f;

        ViewAnimationUtils.createCircularReveal(
                (View) v.getParent(), // View
                centerX, // centerX
                centerY, // centerY
                0, // startRadius
                radius // endRadius
        ).setDuration(1000).start();

//        Animator reveal = ViewAnimationUtils.createCircularReveal(
//                v, // View
//                centerX, // centerX
//                centerY, // centerY
//                radius, // startRadius
//                0 // endRadius
//        ).setDuration(500);
//        // Add a listener
//        reveal.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                super.onAnimationEnd(animation);
//                percentage.setText(outStatus + "\n" + percent + "% " + temp + "\n" + batteryHealth);
//                ViewAnimationUtils.createCircularReveal(
//                        v, // View
//                        centerX, // centerX
//                        centerY, // centerY
//                        0, // startRadius
//                        radius // endRadius
//                ).setDuration(1500).start();
//            }
//        });
//        // Start the circular reveal animation
//        reveal.start();
//
//// If the view is invisible state
//        if (v.getVisibility() == View.INVISIBLE) {
//            v.setVisibility(View.VISIBLE);
//
//            // Call requires API level 21
//            ViewAnimationUtils.createCircularReveal(
//                    v, // View
//                    centerX, // centerX
//                    centerY, // centerY
//                    0, // startRadius
//                    radius // endRadius
//            ).start();
//        } else { // If the view is visible state
//            // Call requires API level 21
//            // Initialize a new circular reveal animation instance
//            Animator reveal = ViewAnimationUtils.createCircularReveal(
//                    v, // View
//                    centerX, // centerX
//                    centerY, // centerY
//                    radius, // startRadius
//                    0 // endRadius
//            );
//            // Add a listener
//            reveal.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    v.setVisibility(View.INVISIBLE);
//                }
//            });
//            // Start the circular reveal animation
//            reveal.start();
//        }
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
                        .setContentText(percent + "%")
                        .setContentIntent(resultPendingIntent)
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
            health = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);

            Bundle bundle = batteryStatus.getExtras();
            String str = bundle.toString();
            Log.i("Battery Info", str);

            BatteryManager mBatteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
            Long avgCurrent = null, currentNow = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                avgCurrent = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_AVERAGE);
                currentNow = mBatteryManager.getLongProperty(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW);
            }
            Log.d("Battery Info 2", "BATTERY_PROPERTY_CURRENT_AVERAGE = " + avgCurrent + "mAh");
            Log.d("Battery Info 2", "BATTERY_PROPERTY_CURRENT_NOW =  " + currentNow + "mAh");

            technology = batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
//            current = batteryStatus.getIntExtra(String.valueOf(BatteryManager.BATTERY_PROPERTY_CURRENT_NOW), -3);
            current = currentNow;
//            voltage = avgCurrent;
            updateBatteryStatus();
        }
    };
}
