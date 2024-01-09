package com.example.mypages.selectCreateRes;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

public class Model_selectCreate {
    private final String fieldName;
    private String intentData = "";
    @DrawableRes
    private final int id;
    private final Class targetActivity;

    public String getFieldName() {
        return fieldName;
    }
    public String getIntentData() {
        return intentData;
    }
    public int getId() {
        return id;
    }


    public Class getTargetActivity() {
        return targetActivity;
    }

    public Model_selectCreate(String name, @DrawableRes int id, Class activity) {
        this.fieldName = name;
        this.id = id;
        this.targetActivity = activity;
    }
    public Model_selectCreate(String name, @DrawableRes int id, Class activity, String intentData) {
        this.fieldName = name;
        this.id = id;
        this.targetActivity = activity;
        this.intentData = intentData;
    }
    public Model_selectCreate(String name, @DrawableRes int id) {
        this.fieldName = name;
        this.id = id;
        this.targetActivity = null;
    }
}
