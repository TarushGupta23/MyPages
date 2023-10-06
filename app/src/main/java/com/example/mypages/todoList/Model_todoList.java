package com.example.mypages.todoList;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.example.mypages.R;

import java.util.ArrayList;
import java.util.Date;

public class Model_todoList {
    Context context;
    private String title, description;
    private ArrayList<SubTask> subTaskList = new ArrayList<>();
    private ArrayList attachments = new ArrayList();
    private boolean isCompleted, isCompressed, notificationEnabled, repeatEnabled, hasDeadline;
    private int notificationBefore, notificationBeforeType; // beforeType => minutes or hr.s
    private int repeatType;
    private Date dateCreated, dateCompleted, deadLine;
    private int primaryColor, secondaryColor;
    private String key;


    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public ArrayList<SubTask> getSubTaskList() {return subTaskList;}
    public ArrayList getAttachments() {return attachments;}
    public boolean isCompleted() {return isCompleted;}
    public boolean isCompressed() {return isCompressed;}
    public Date getDateCreated() {return dateCreated;}
    public Date getDateCompleted() {return dateCompleted;}
    public int getPrimaryColor() {return primaryColor;}
    public int getSecondaryColor() {return secondaryColor;}
    public String getKey() {return key;}
    public Date getDeadLine() {return deadLine;}
    public boolean isNotificationEnabled() { return notificationEnabled; }
    public int getRepeatType() { return repeatType; }
    public int getNotificationBefore() {return notificationBefore;}
    public int getNotificationBeforeType() {return notificationBeforeType;}
    public boolean isRepeatEnabled() {return repeatEnabled;}
    public boolean isHasDeadline() {return hasDeadline;}

    public void setHasDeadline(boolean hasDeadline) {this.hasDeadline = hasDeadline;}
    public void setNotificationEnabled(boolean notificationEnabled) { this.notificationEnabled = notificationEnabled; }
    public void setRepeatType(int repeatType) { this.repeatType = repeatType; }
    public void setTitle(String title) {this.title = title;}
    public void setDescription(String description) {this.description = description;}
    public void setSubTaskList(ArrayList<SubTask> subTaskList) {this.subTaskList = subTaskList;}
    public void setAttachments(ArrayList attachments) {this.attachments = attachments;}
    public void setCompleted(boolean completed) {isCompleted = completed;}
    public void setCompressed(boolean compressed) {isCompressed = compressed;}
    public void setDateCreated(Date dateCreated) {this.dateCreated = dateCreated;}
    public void setDateCompleted(Date dateCompleted) {this.dateCompleted = dateCompleted;}
    public void setPrimaryColor(int primaryColor) {this.primaryColor = primaryColor;}
    public void setSecondaryColor(int secondaryColor) {this.secondaryColor = secondaryColor;}
    public void setKey(String key) {this.key = key;}
    public void setDeadLine(Date deadLine) {this.deadLine = deadLine;}
    public void setNotificationBefore(int notificationBefore) {this.notificationBefore = notificationBefore;}
    public void setNotificationBeforeType(int notificationBeforeType) {this.notificationBeforeType = notificationBeforeType;}
    public void setRepeatEnabled(boolean repeatEnabled) {this.repeatEnabled = repeatEnabled;}

    Model_todoList() {}
    Model_todoList(Context context) {
        this.context = context;
        this.primaryColor = ContextCompat.getColor(context, R.color.deepBlue3);
        this.secondaryColor = ContextCompat.getColor(context, R.color.ice);
    }

    static class SubTask {
        private String subTaskName;
        private boolean isCompleted;
        private Date completionDate = null;

        public String getSubTaskName() { return subTaskName; }
        public boolean isCompleted() { return isCompleted; }
        public Date getCompletionDate() { return completionDate; }

        public void setSubTaskName(String subTaskName) { this.subTaskName = subTaskName; }
        public void setCompleted(boolean completed) { isCompleted = completed; }
        public void setCompletionDate(Date completionDate) { this.completionDate = completionDate; }
    }
}
