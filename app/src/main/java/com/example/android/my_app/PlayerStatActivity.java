package com.example.android.my_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PlayerStatActivity extends AppCompatActivity {

    private Team team;
    private Match match;
    private PlayerStatistics playStats;
    private TextView[] tvStatNames;
    private TextView[] statIn;
    private TextView[] statOut;
    private Button[] btStatIn;
    private Button[] btStatOut;
    private TextView playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        team = ((GlobalVariables) getApplication()).getTeam();
        match = ((GlobalVariables) getApplication()).getMatch();
        String player = getIntent().getStringExtra("Player");
        playStats = match.getPlayerStatistics(player);
        int numStats = playStats.getPlayerStats().size();
        chooseScreen(numStats);
        tvStatNames = new TextView[numStats];
        statIn = new TextView[numStats];
        statOut = new TextView[numStats];
        btStatIn = new Button[numStats];
        btStatOut = new Button[numStats];
        setStats(numStats);
        for (int rep = 0; rep < numStats; rep++){
            tvStatNames[rep].setText(playStats.getStat(rep).getName());
            statIn[rep].setText(playStats.getStat(rep).getNumOne() + "");
            statIn[rep].setTextColor(Color.GREEN);
            setStatTotal(rep);
            final int lol = rep;
            btStatIn[rep].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playStats.getStat(lol).increment(0, 1);
                    statIn[lol].setText(playStats.getStat(lol).getNumOne() + "");
                }
            });
        }
        playerName = findViewById(R.id.tv_player_name);
        playerName.setText(playStats.getName());

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.player_stat_finish, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ((GlobalVariables) getApplication()).setMatch(match);
        team.updateMatch(match);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(team.getTeamName());
        myRef.setValue(team);
        ((GlobalVariables) getApplication()).setTeam(team);
        Intent intent = new Intent(PlayerStatActivity.this, PlayerStatSummary.class);
        intent.putExtra("Player", playerName.getText());
        startActivity(intent);
        return true;
    }

    public void chooseScreen(int num){
        if (num == 1){
            setContentView(R.layout.stat_taker_one_stat);
        } else if (num == 2){
            setContentView(R.layout.stat_taker_two_stat);
        }else if (num == 3){
            setContentView(R.layout.stat_taker_three_stat);
        }else if (num == 4){
            setContentView(R.layout.stat_taker_four_stat);
        }else if (num == 5){
            setContentView(R.layout.stat_taker_five_stat);
        }else if (num == 6){
            setContentView(R.layout.stat_taker_six_stat);
        }
    }

    public void setStats(int num){
        if (num == 1){
            tvStatNames[0] = findViewById(R.id.tv_stat1_n);
            statIn[0] = findViewById(R.id.tv_stat1_made);
            statOut[0] = findViewById(R.id.tv_stat1_miss);
            btStatIn[0] = findViewById(R.id.stat1_button1);
            btStatOut[0] = findViewById(R.id.stat1_button2);
        } else if (num == 2){
            setStats(1);
            tvStatNames[1] = findViewById(R.id.tv_stat2_n);
            statIn[1] = findViewById(R.id.tv_stat2_made);
            statOut[1] = findViewById(R.id.tv_stat2_miss);
            btStatIn[1] = findViewById(R.id.stat2_button1);
            btStatOut[1] = findViewById(R.id.stat2_button2);
        }else if (num == 3){
            setStats(2);
            tvStatNames[2] = findViewById(R.id.tv_stat3_n);
            statIn[2] = findViewById(R.id.tv_stat3_made);
            statOut[2] = findViewById(R.id.tv_stat3_miss);
            btStatIn[2] = findViewById(R.id.stat3_button1);
            btStatOut[2] = findViewById(R.id.stat3_button2);
        }else if (num == 4){
            setStats(3);
            tvStatNames[3] = findViewById(R.id.tv_stat4_n);
            statIn[3] = findViewById(R.id.tv_stat4_made);
            statOut[3] = findViewById(R.id.tv_stat4_miss);
            btStatIn[3] = findViewById(R.id.stat4_button1);
            btStatOut[3] = findViewById(R.id.stat4_button2);
        }else if (num == 5){
            setStats(4);
            tvStatNames[4] = findViewById(R.id.tv_stat5_n);
            statIn[4] = findViewById(R.id.tv_stat5_made);
            statOut[4] = findViewById(R.id.tv_stat5_miss);
            btStatIn[4] = findViewById(R.id.stat5_button1);
            btStatOut[4] = findViewById(R.id.stat5_button2);
        }else if (num == 6){
            setStats(5);
            tvStatNames[5] = findViewById(R.id.tv_stat6_n);
            statIn[5] = findViewById(R.id.tv_stat6_made);
            statOut[5] = findViewById(R.id.tv_stat6_miss);
            btStatIn[5] = findViewById(R.id.stat6_button1);
            btStatOut[5] = findViewById(R.id.stat6_button2);
        }
    }

    public void setStatTotal(final int x){
        if (playStats.getStat(x).getNumTwo() < 0){
            statOut[x].setVisibility(View.GONE);
            btStatOut[x].setVisibility(View.GONE);
        } else if(playStats.getStat(x).getNumTwo() >= 0){
            statOut[x].setText(playStats.getStat(x).getNumTwo() + "");
            statOut[x].setTextColor(Color.RED);
            btStatOut[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playStats.getStat(x).increment(1, 1);
                    statOut[x].setText(playStats.getStat(x).getNumTwo() + "");
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

    }
}
