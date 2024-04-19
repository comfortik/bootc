package com.example.myapplication;

public class Product {
    private String name;
    private int freshnessId;

    private String data;

    public Product(String name, int freshnessId, String data){
        this.name= name;
        this.freshnessId= freshnessId;
        this.data=data;
    }
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFreshnessId() {
        return freshnessId;
    }
    //id1- свежий
    //2- истекает срок
    //3- просрочен

    public void setFreshnessId(int freshnessId) {
        this.freshnessId = freshnessId;
    }
}
