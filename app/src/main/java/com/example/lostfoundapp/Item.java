package com.example.lostfoundapp;

public class Item {

    private int id; // 物品ID
    private String itemName; // 物品名称
    private String description; // 物品描述
    private String date; // 日期
    private String location; // 位置描述（如地址）
    private double latitude; // 纬度
    private double longitude; // 经度

    // 默认构造函数
    public Item() {
    }

    // 含参数的构造函数（不含经纬度）
    public Item(String itemName, String description, String date, String location) {
        this.itemName = itemName;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    // 含参数的构造函数（含经纬度）
    public Item(String itemName, String description, String date, String location, double latitude, double longitude) {
        this.itemName = itemName;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // 获取物品ID
    public int getId() {
        return id;
    }

    // 设置物品ID
    public void setId(int id) {
        this.id = id;
    }

    // 获取物品名称
    public String getItemName() {
        return itemName;
    }

    // 设置物品名称
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // 获取物品描述
    public String getDescription() {
        return description;
    }

    // 设置物品描述
    public void setDescription(String description) {
        this.description = description;
    }

    // 获取日期
    public String getDate() {
        return date;
    }

    // 设置日期
    public void setDate(String date) {
        this.date = date;
    }

    // 获取位置描述
    public String getLocation() {
        return location;
    }

    // 设置位置描述
    public void setLocation(String location) {
        this.location = location;
    }

    // 获取纬度
    public double getLatitude() {
        return latitude;
    }

    // 设置纬度
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    // 获取经度
    public double getLongitude() {
        return longitude;
    }

    // 设置经度
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
