package com.example.mypantry.GroceryList;

import android.os.Parcel;
import android.os.Parcelable;

public class GroceryItem implements Parcelable {

    private String pantryItem, groceryItem, id;

    public GroceryItem(){

    }

    public GroceryItem(String groceryItem, String id) {
        this.groceryItem = groceryItem;
        this.id = id;
    }


    protected GroceryItem(Parcel in) {
        groceryItem = in.readString();
        id = in.readString();
    }

    public static final Creator<GroceryItem> CREATOR = new Creator<GroceryItem>() {
        @Override
        public GroceryItem createFromParcel(Parcel in) {
            return new GroceryItem(in);
        }

        @Override
        public GroceryItem[] newArray(int size) {
            return new GroceryItem[size];
        }
    };

    public String getGroceryItem() {
        return groceryItem;
    }

    public void setGroceryItem(String groceryItem) {
        this.groceryItem = groceryItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(groceryItem);
        parcel.writeString(id);
    }
}
