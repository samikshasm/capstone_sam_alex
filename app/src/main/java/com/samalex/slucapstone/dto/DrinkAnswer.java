package com.samalex.slucapstone.dto;

public class DrinkAnswer {
    private String cost;
    private String type;
    private String size;
    private String who;
    private String where;
    private String drinksPlanned;

    public String getCost() {
        return cost;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getWho() {
        return who;
    }

    public String getWhere() {
        return where;
    }

    public String getDrinksPlanned() {
        return drinksPlanned;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setDrinksPlanned(String drinksPlanned) {
        this.drinksPlanned = drinksPlanned;
    }
}
