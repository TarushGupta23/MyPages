package com.example.mypages.notes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Model_Note implements Serializable {
    private String noteHeading, noteBody, key, password;
    private ArrayList<String> imgLocations;
    private Date dateCreated;
    private boolean isLocked;

    public static final String EMPTY_STRING = "$$_empty_body_$$";

    Model_Note() {}
    Model_Note(Date date, String key) {
        dateCreated = date;
        noteHeading = "New Note";
        this.key = key;
        noteBody = EMPTY_STRING;
        imgLocations = new ArrayList<>();
        isLocked = false;
    }

    public String getNoteHeading() {return noteHeading; }
    public String getNoteBody() {return noteBody;}
    public String getKey() {return key;}
    public ArrayList<String> getImgLocations() {return imgLocations;}
    public Date getDateCreated() {return dateCreated;}
    public String getPassword() {return password;}
    public boolean isLocked() {return isLocked;}

    public void setNoteHeading(String noteHeading) {this.noteHeading = noteHeading;}
    public void setNoteBody(String noteBody) {this.noteBody = noteBody;}
    public void setKey(String key) {this.key = key;}
    public void setImgLocations(ArrayList<String> imgLocations) {this.imgLocations = imgLocations;}
    public void setDateCreated(Date dateCreated) {this.dateCreated = dateCreated;}
    public void setPassword(String password) {this.password = password;}
    public void setLocked(boolean locked) {isLocked = locked;}

    public boolean isNew() {
        return noteHeading.equals("New Note") && noteBody.equals(EMPTY_STRING);
    }
}
