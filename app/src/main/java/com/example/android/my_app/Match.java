package com.example.android.my_app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Match implements Serializable {
    private String date;
    private String opponent;
    private ArrayList<PlayerStatistics> playerStatistics;
    private String matchStatus;

    public Match(){
        date = "";
        opponent = "";
        playerStatistics = new ArrayList<>();
        matchStatus = "" ;
    }

    public Match(String date, String opponent, ArrayList<String> plays){
        this.date = date;
        this.opponent = opponent;
        playerStatistics = new ArrayList<>();
        for (int rep = 0; rep < plays.size(); rep++){
            playerStatistics.add(new PlayerStatistics(plays.get(rep)));
        }
        matchStatus = "In Progress";
    }


    public String getDate(){
        return date;
    }

    public List<Statistic> getPlayerStats(String player){
        for (int x = 0; x < playerStatistics.size(); x++){
            if (playerStatistics.get(x).getName().equals(player)){
                return playerStatistics.get(x).getPlayerStats();
            }
        }
        return null;
    }

    public void setPlayerStats(int x, PlayerStatistics ps){
        playerStatistics.get(x).setPlayerStats(ps.getPlayerStats());
    }

    public void setStats(String player, ArrayList<Statistic> stat){
        for (int x = 0; x < playerStatistics.size(); x++){
            if (playerStatistics.get(x).getName().equals(player)){
                playerStatistics.get(x).setPlayerStats(stat);
            }
        }
    }

    public void addStats(List<Statistic> s){
        for (int rep = 0; rep < playerStatistics.size(); rep++){
            for (int x = 0; x < s.size(); x++){
                playerStatistics.get(rep).addStat(new Statistic(s.get(x).getName(), s.get(x).getType()));
            }
        }
    }

    public PlayerStatistics getPlayerStatistics(String name){
        for (int rep = 0; rep < playerStatistics.size(); rep++){
            if (playerStatistics.get(rep).getName().equals(name)){
                return playerStatistics.get(rep);
            }
        }
        return null;
    }

    public PlayerStatistics getPlayerStatistics(int i){
        return playerStatistics.get(i);
    }


    public boolean isCorrectMatch(String day){
        return date.equals(day);
    }

    public String toString(){
        return "VS " + opponent + ": " + date;
    }

    public String getMatchStatus(){
        return matchStatus;
    }

    public void setMatchStatus(String str){
        matchStatus = str;
    }

    public String getOpponent(){
        return opponent;
    }

    public ArrayList<String> getPlayers(){
        ArrayList<String> players = new ArrayList<>();
        for (int rep = 0; rep < playerStatistics.size(); rep++){
            players.add(playerStatistics.get(rep).getName());
        }
        return players;
    }

    public int getPlayerLoc(String name){
        for (int rep = 0; rep < playerStatistics.size(); rep++){
            if (playerStatistics.get(rep).getName().equals(name)){
                return rep;
            }
        }
        return 0;
    }

    public List<PlayerStatistics> getPlayerStatistics(){
        return playerStatistics;
    }
}
