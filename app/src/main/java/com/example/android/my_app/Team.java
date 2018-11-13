package com.example.android.my_app;

import java.util.ArrayList;

public class Team{
    private String teamName;
    private String sport;
    private ArrayList<String> members;
    private ArrayList<Integer> record;
    private ArrayList<Match> matches;

    public Team(){
        teamName = "";
        sport = "";
        members = new ArrayList<>();
        record = new ArrayList<>();
        matches = new ArrayList<>();
    }

    public Team(String teamName, String sport, ArrayList<String> tMembers){
        this.teamName = teamName;
        this.sport = sport;
        members = new ArrayList<>();
        for (int rep = 0; rep < tMembers.size(); rep++){
            members.add(tMembers.get(rep));
        }
        record = new ArrayList<>();
        record.add(0);
        record.add(0);
        matches = new ArrayList<>();
    }

    public Team(String teamName, String sport){
        this.teamName = teamName;
        this.sport = sport;
        members = new ArrayList<>();
        record = new ArrayList<>();
        record.add(0);
        record.add(0);
        matches = new ArrayList<>();
    }

    public String getTeamName(){
        return teamName;
    }

    public String getPlayer(int spot){
        return members.get(spot);
    }

    public String getSport(){
        return sport;
    }

    public Match getMatch(String date){
        for (Match m : matches){
            if (m.isCorrectMatch(date)){
                return m;
            }
        }
        return null;
    }

    public void updateRecord(int spot){
        record.set(spot, record.get(spot) + 1);
    }

    public void addMatch (Match m){
        matches.add(m);
    }

    public void addPlayer(String play) {
        members.add(play);
    }
    public String toString(){
        return teamName;
    }

    public ArrayList<Integer> getRecord(){
        return record;
    }

    public int getRecord(int spot){
        return record.get(spot);
    }


    public ArrayList<Match> getMatches(){
        return matches;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public int numMembers(){
        return members.size();
    }

    public void updateMatch(Match m){
        String date = m.getDate();
        for (int rep = 0; rep < matches.size(); rep++){
            if (date.equals(matches.get(rep).getDate())){
                matches.set(rep, m);
            }
        }
    }

    public int getMatchLoc(Match m){
        for (int rep = 0; rep < matches.size(); rep++){
            if (matches.get(rep).getDate().equals(m.getDate()) && matches.get(rep).getOpponent().equals(m.getOpponent())){
                return rep;
            }
        }

        return -1;
    }

    public void setMatches(ArrayList<Match> matches){
        this.matches = matches;
    }
}
