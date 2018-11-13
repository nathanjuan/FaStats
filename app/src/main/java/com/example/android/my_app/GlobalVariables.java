package com.example.android.my_app;

import android.app.Application;

import java.util.ArrayList;

public class GlobalVariables extends Application {
    private Team team;
    private Match match;
    private ArrayList<Team> teams;

    public Match getMatch(){
        return match;
    }

    public void setMatch(Match m){
        match = m;
    }

    public Team getTeam(){
        return team;
    }

    public void setTeam(Team t){
        team = t;
    }

    public ArrayList<Team> getTeams(){
        return teams;
    }

    public void setTeams(ArrayList<Team> ts){
        teams = ts;
    }

}
