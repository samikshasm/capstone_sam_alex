package com.samalex.slucapstone.dto;

public class DrinkAnswer {
    private String cost;
    private String type;
    private int size;
    private String who;
    private String where;
    private int drinksPlanned;

    public DrinkAnswer() {
    }

    public DrinkAnswer(String cost, String type, int size, String who, String where, int drinksPlanned) {
        this.cost = cost;
        this.type = type;
        this.size = size;
        this.who = who;
        this.where = where;
        this.drinksPlanned = drinksPlanned;
    }

    public String getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public int getSize() {
        return size;
    }

    public String getWho() {
        return who;
    }

    public String getWhere() {
        return where;
    }

    public int getDrinksPlanned() {
        return drinksPlanned;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setDrinksPlanned(int drinksPlanned) {
        this.drinksPlanned = drinksPlanned;
    }
}
