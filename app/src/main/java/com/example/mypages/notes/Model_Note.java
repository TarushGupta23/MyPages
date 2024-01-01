package com.example.mypages.notes;

import java.util.ArrayList;
import java.util.Date;

public class Model_Note {
    private String noteHeading, noteBody, key;
    private ArrayList<String> imgLocations;
    private Date dateCreated;


    public static final String EMPTY_STRING = "$$_empty_body_$$";
//    public static final String LIST_START = "$${";
//    public static final String LIST_END = "$$}";
//    public static final String LIST_BREAK = "$$,";

    Model_Note() {}
    Model_Note(Date date, String key) {
        dateCreated = date;
        noteHeading = "New Note";
        this.key = key;
        noteBody = EMPTY_STRING;
        imgLocations = new ArrayList<>();
    }

    public String getNoteHeading() {return noteHeading; }
    public String getNoteBody() {return noteBody;}
    public String getKey() {return key;}
    public ArrayList<String> getImgLocations() {return imgLocations;}
    public Date getDateCreated() {return dateCreated;}


    public void setNoteHeading(String noteHeading) {this.noteHeading = noteHeading;}
    public void setNoteBody(String noteBody) {this.noteBody = noteBody;}
    public void setKey(String key) {this.key = key;}
    public void setImgLocations(ArrayList<String> imgLocations) {this.imgLocations = imgLocations;}
    public void setDateCreated(Date dateCreated) {this.dateCreated = dateCreated;}
}
