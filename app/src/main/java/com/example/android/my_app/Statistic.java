package com.example.android.my_app;

import java.util.ArrayList;

public class Statistic {
    private String name;
    private ArrayList<Integer> count;


    public Statistic(){
        name = "";
        count = new ArrayList<>(2);
        count.add(0);
        count.add(0);
    }

    public Statistic(String name, ArrayList<Integer> c){
        this.name = name;
        count = new ArrayList<>();
        count.add(c.get(0));
        count.add(c.get(1));
    }

    public Statistic(String name, int type){ //type = 1: total; type = 2: made/missed
        this.name = name;
        count = new ArrayList<>(2);
        if (type == 0){
            count.add(0);
            count.add(-1);
        } else{
            count.add(0);
            count.add(0);
        }
    }

    public String getName(){
        return name;
    }

    public ArrayList<Integer> getCount(){
        return count;
    }

    public void increment(int spot, int val){
        if (spot > 1 || spot < 0){
            return;
        }
        int tot = count.get(spot);
        count.set(spot, tot + val);
    }

    public int getNumOne(){
        return count.get(0);
    }

    public int getNumTwo(){
        return count.get(1);
    }

    public int getType(){
        if (count.get(1) < 0){
            return 0;
        }
        return 1;
    }

    public void setNumOne(int x){
        count.set(0, x);
    }

    public void setNumTwo(int x){
        count.set(1, x);
    }
}
