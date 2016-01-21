package com.charlesdrews.todolists;

/**
 * Models a to-do list item with getters/setters for title, detail, & listName strings.
 * Created by charlie on 1/20/16.
 */
public class ToDoItem {
    private String title;
    private String detail;
    private String listName;

    public ToDoItem(String title, String detail, String listName) {
        this.title = title;
        this.detail = detail;
        this.listName = listName;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public String getListName() {
        return listName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }
}
