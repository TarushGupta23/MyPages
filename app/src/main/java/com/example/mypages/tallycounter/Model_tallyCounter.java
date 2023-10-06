package com.example.mypages.tallycounter;

import android.content.Context;
import android.graphics.Color;

import androidx.core.content.ContextCompat;

import com.example.mypages.R;

public class Model_tallyCounter {

    private int count;
    private String counterName;
    private int primaryColor;
    private int secondaryColor;
    private boolean isLocked = false;
    private String key;

    public Model_tallyCounter() {
        // Default constructor required for Firebase
    }

    public Model_tallyCounter(int count, String counterName, int primaryColor, int secondaryColor, boolean isLocked) {
        this.count = count;
        this.counterName = counterName;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        this.isLocked = isLocked;
    }

    public Model_tallyCounter(int count, String counterName, boolean isLocked, Context context) {
        this.count = count;
        this.counterName = counterName;
        this.isLocked = isLocked;
        this.primaryColor = ContextCompat.getColor(context, R.color.deepBlue3);
        this.secondaryColor = ContextCompat.getColor(context, R.color.ice);
    }

    //Getters
    public int getCount() { return count; }
    public String getCounterName() { return counterName; }
    public int getPrimaryColor() { return primaryColor; }
    public int getSecondaryColor() { return secondaryColor; }
    public boolean isLocked() { return isLocked; }
    public String getKey() {return this.key;}

    //Setters
    public void setCount(int count) { this.count = count; }
    public void setCounterName(String counterName) { this.counterName = counterName; }
    public void setPrimaryColor(int primaryColor) { this.primaryColor = primaryColor; }
    public void setSecondaryColor(int secondaryColor) { this.secondaryColor = secondaryColor; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public void setKey(String key) { this.key = key; }
}
