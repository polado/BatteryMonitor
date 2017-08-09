package com.pola_do.batterymonitor;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by PolaDo on 7/24/2017.
 */

public class GetTheme {

    Context context;

    public GetTheme(Context context) {
        this.context = context;
    }

    public int getTheme() {
        int theme = readTheme();
        switch (theme) {
            case R.id.theme_blue:
                return (R.style.AppThemeBlue);
            case R.id.theme_default:
                return (R.style.AppThemeDefault);
            case R.id.theme_indigo:
                return (R.style.AppThemeIndigo);
            case R.id.theme_pink:
                return (R.style.AppThemePink);
            case R.id.theme_purple:
                return (R.style.AppThemePurple);
            case R.id.theme_red:
                return (R.style.AppThemeRed);
            case R.id.theme_teal:
                return (R.style.AppThemeTeal);
            case R.id.theme_grey:
                return (R.style.AppThemeGrey);
            case R.id.theme_blue_grey:
                return (R.style.AppThemeBlueGrey);
            case R.id.theme_black:
                return (R.style.AppThemeBlack);
            case R.id.theme_dark_blue:
                return (R.style.AppThemeDarkBlue);
            default:
                return (R.style.AppThemeDefault);
        }
    }

    public int getThemeAccentColor() {
        int accentColor;
        int theme = readTheme();
        switch (theme) {
            case R.id.theme_blue:
                accentColor = context.getResources().getColor(R.color.colorAccentBlue);
                break;
            case R.id.theme_default:
                accentColor = context.getResources().getColor(R.color.colorAccent2);
                break;
            case R.id.theme_indigo:
                accentColor = context.getResources().getColor(R.color.colorAccentIndigo);
                break;
            case R.id.theme_pink:
                accentColor = context.getResources().getColor(R.color.colorAccentPink);
                break;
            case R.id.theme_purple:
                accentColor = context.getResources().getColor(R.color.colorAccentPurple);
                break;
            case R.id.theme_red:
                accentColor = context.getResources().getColor(R.color.colorAccentRed);
                break;
            case R.id.theme_teal:
                accentColor = context.getResources().getColor(R.color.colorAccentTeal);
                break;
            case R.id.theme_grey:
                accentColor = context.getResources().getColor(R.color.colorAccentGrey);
                break;
            case R.id.theme_blue_grey:
                accentColor = context.getResources().getColor(R.color.colorAccentBlueGrey);
                break;
            case R.id.theme_black:
                accentColor = context.getResources().getColor(R.color.colorAccentBlack);
                break;
            case R.id.theme_dark_blue:
                accentColor = context.getResources().getColor(R.color.colorAccentDarkBlue);
                break;
            default:
                accentColor = context.getResources().getColor(R.color.colorAccent2);
                break;
        }
        return accentColor;
    }

    private int readTheme() {
        String fileName = "theme";
        String ret = "-1";

        try {
            InputStream inputStream = context.openFileInput(fileName);

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
