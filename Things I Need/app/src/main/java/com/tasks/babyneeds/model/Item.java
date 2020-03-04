package com.tasks.babyneeds.model;

public class Item {
    private int id;
    private String item;
    private int qty;
    private String color;
    private int  size;

    public Item(){
    }

    public Item(int id, String item, int qty, int size) {
        this.id = id;
        this.item = item;
        this.qty = qty;
        this.size = size;
    }

    public Item(int id, String item, int qty, String color, int size) {
        this.id = id;
        this.item = item;
        this.qty = qty;
        this.color = color;
        this.size = size;
    }

    public Item(String item, int qty, String color, int size) {
        this.item = item;
        this.qty = qty;
        this.color = color;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
