package com.example.mypages.simpleList;

import java.util.List;

public class Model_simpleListItem {
    private String listTitle, key;
    private List<Object> data;

    public Model_simpleListItem(String listTitle, String key, List<Object> data) {
        this.listTitle = listTitle;
        this.key = key;
        this.data = data;
    }

    public Model_simpleListItem() {}

    public String getListTitle() { return listTitle; }
    public String getKey() { return key; }
    public List<Object> getData() { return data; }

    public void setListTitle(String listTitle) { this.listTitle = listTitle; }
    public void setKey(String key) { this.key = key; }
    public void setData(List<Object> data) { this.data = data; }
}
