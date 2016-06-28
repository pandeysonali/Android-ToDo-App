package com.sonalipandey.ado;

public class ToDoItem {

    public Long _id; // for cupboard

    public String itemName;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Long getId() {
        return _id;
    }

    public void setId(Long _id) {
        this._id = _id;
    }


    public ToDoItem() {
        this.itemName = "noName";
    }

    //public ToDoItem(String name, Integer cuteValue) {
      //  this.itemName = name;
    //}


}