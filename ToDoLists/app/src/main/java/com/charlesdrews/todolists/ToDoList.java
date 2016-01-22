package com.charlesdrews.todolists;

import java.util.ArrayList;

/**
 * Models a to-do list with a name and an ArrayList of ToDoItem objects.
 * Includes methods to get/set name, get items list, add to items list, remove from items list.
 * Created by charlie on 1/20/16.
 */
public class ToDoList {
    private String name;
    private ArrayList<ToDoItem> items;

    public ToDoList(String name) {
        this.name = name;
        items = new ArrayList<ToDoItem>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
       this.name = name;
    }

    public ArrayList<ToDoItem> getItems() {
        return items;
    }

    public ArrayList<String> getItemTitles() {
        ArrayList<String> itemTitles = new ArrayList<String>();
        for (ToDoItem item : this.getItems()) {
            itemTitles.add(item.getTitle());
        }
        return itemTitles;
    }

    public ToDoItem getItemByTitle(String itemTitle) {
        for (ToDoItem item : this.getItems()) {
            if (item.getTitle().equals(itemTitle)) {
                return item;
            }
        }
        return null;
    }

    public void addItem(ToDoItem item) {
        items.add(item);
    }

    public void removeItem(ToDoItem item) {
        items.remove(item);
    }

    public void removeAllItems() {
        for (ToDoItem item : this.items) {
            this.removeItem(item);
        }
    }
}
