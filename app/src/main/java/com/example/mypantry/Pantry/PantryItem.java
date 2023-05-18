package com.example.mypantry.Pantry;

public class PantryItem {

    private String pantryItem, details, id, date, group;

    public PantryItem(){

    }

    public PantryItem(String pantryItem, String details, String id, String date) {
        this.pantryItem = pantryItem;
        this.details = details;
        this.id = id;
        this.date = date;
    }

    public String getPantryItem() {
        return pantryItem;
    }

    public void setPantryItem(String pantryItem) {
        this.pantryItem = pantryItem;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
