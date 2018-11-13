package com.example.android.my_app;

import java.util.ArrayList;

public class PlayerStatistics {
    private String name;
    private ArrayList<Statistic> playerStats;
    private boolean isFinished;

    public PlayerStatistics(){
        name = "";
        playerStats = new ArrayList<>(10);
        isFinished = false;
    }

    public PlayerStatistics(String n, ArrayList<Statistic> pStats){
        name = n;
        playerStats = pStats;
        isFinished = false;
    }

    public PlayerStatistics(String n){
        name = n;
        playerStats = new ArrayList<>();
        isFinished = false;
    }

    public PlayerStatistics(String n, boolean finished){
        name = n;
        playerStats = new ArrayList<>();
        isFinished = finished;
    }


    public void setPlayerStats(ArrayList<Statistic> stats){
        playerStats = new ArrayList<>();
        for (int rep = 0; rep < stats.size(); rep++){
            playerStats.add(stats.get(rep));
        }

    }


    public void addStat(Statistic s){
        playerStats.add(s);
    }

    public String getName(){
        return name;
    }

    public int getCount (Statistic s, int x){
        return 0;
    }

    public Statistic getStat(int loc){

        return playerStats.get(loc);
    }

    public ArrayList<Statistic> getPlayerStats() {
        return playerStats;
    }

    public boolean isFinished(){
        return isFinished;
    }

    public void statsDone(boolean bool){
        isFinished = bool;
    }

    public boolean allNotCompleted(){
        for (Statistic s: playerStats){
            if (s.getNumOne() > 0 || s.getNumTwo() > 0){
                return false;
            }
        }
        return true;
    }
}
