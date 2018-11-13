package com.example.android.my_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {
    private Team team;
    private Match match;
    private ArrayList<String> players;
    private ListView playersView;
    private TextView date;
    private TextView matchStatus;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
        team = ((GlobalVariables) getApplication()).getTeam();
        match = ((GlobalVariables) getApplication()).getMatch();
        setUpStats();
        //Log.d("gay", match.getPlayerStatistics(0).getName());
        //Log.d("gay", match.getPlayerStatistics(0).getStat(0).getNumOne() + "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(team.getTeamName()).child("matches").child(team.getMatchLoc(match) + "");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                match = dataSnapshot.getValue(Match.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        TextView teamName = (TextView) findViewById(R.id.tv_team_name_m);
        teamName.setText(team.getTeamName());
        TextView opponentName = (TextView) findViewById(R.id.tv_opponent);
        opponentName.setText(match.getOpponent());
        players = match.getPlayers();
        playersView = (ListView) findViewById(R.id.player_lv_match);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, players);
        playersView.setAdapter(adapter);
        date = (TextView) findViewById(R.id.tv_match_date);
        date.setText(match.getDate());
        matchStatus = (TextView) findViewById(R.id.tv_match_status);
        matchStatus.setText(match.getMatchStatus());
        playersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.print(match.getPlayerStatistics(i).isFinished() + "");
                 if (match.getPlayerStatistics(i).isFinished() && (match.getPlayerStatistics(i).getStat(0).getNumOne() == 0 || match.getPlayerStatistics(i).getStat(0).getNumTwo() == 0)) {
                     Toast.makeText(MatchActivity.this, "In Progress!", Toast.LENGTH_SHORT).show();
                     return;
                 } else if (match.getPlayerStatistics(i).isFinished()){
                    ((GlobalVariables) getApplication()).setTeam(team);
                    ((GlobalVariables) getApplication()).setMatch(match);
                    Intent intent = new Intent(MatchActivity.this, PlayerStatSummary.class);
                    intent.putExtra("Player", players.get(i));
                    startActivity(intent);
                    return;
                } else if (match.getPlayerStatistics(i).isFinished() == false){
                    match.getPlayerStatistics(i).statsDone(true);
                    team.updateMatch(match);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(team.getTeamName()).child("matches").child(team.getMatchLoc(match) + "").child("playerStatistics").child(i + "");
                    myRef.child("finished").setValue(true);
                    ((GlobalVariables) getApplication()).setTeam(team);
                    ((GlobalVariables) getApplication()).setMatch(match);
                    Intent intent = new Intent(MatchActivity.this, PlayerStatActivity.class);
                    intent.putExtra("Player", players.get(i));
                    startActivity(intent);
                }
            }
        });

        DatabaseReference refs = database.getReference(team.getTeamName()).child("matches").child(team.getMatchLoc(match) + "").child("playerStatistics");
        for (int rep = 0; rep < players.size(); rep++){
            final int lol = rep;
            DatabaseReference rf = refs.child(String.valueOf(rep));
            rf.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean isDone = (boolean) dataSnapshot.child("finished").getValue();
                    match.getPlayerStatistics(lol).statsDone(isDone);
                    PlayerStatistics pStats = dataSnapshot.getValue(PlayerStatistics.class);
                        for (int x = 0; x < match.getPlayerStatistics(0).getPlayerStats().size(); x++){
                            System.out.print(dataSnapshot.child("playerStats").child(x + "").child("numOne").getValue(Integer.class));
                            match.getPlayerStatistics(lol).getStat(x).setNumOne(dataSnapshot.child("playerStats").child(x + "").child("numOne").getValue(Integer.class));
                            match.getPlayerStatistics(lol).getStat(x).setNumTwo(dataSnapshot.child("playerStats").child(x + "").child("numTwo").getValue(Integer.class));
                        }
                    team.updateMatch(match);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
        });
        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        final String userName = prefs.getString("Username", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Owners").child(team.getTeamName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals(userName) && match.getMatchStatus().equals("In Progress")){
                    getMenuInflater().inflate(R.menu.match_finish, menu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.finish_button){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(MatchActivity.this);
            final View dCreateView = getLayoutInflater().inflate(R.layout.match_finish, null);
            final RadioGroup radioGroup = (RadioGroup) dCreateView.findViewById(R.id.radioGroup);
            final Button createButton = (Button) dCreateView.findViewById(R.id.bt_finish_match);
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (radioGroup.getCheckedRadioButtonId() == R.id.finish_match_win){
                        match.setMatchStatus("Win");
                        matchStatus.setText("Win");
                        team.updateRecord(0);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dd = database.getReference(team.getTeamName()).child("matches").child(team.getMatchLoc(match) + "").child("matchStatus");
                        dd.setValue("Win");
                    } else if(radioGroup.getCheckedRadioButtonId() == R.id.finish_match_loss){
                        match.setMatchStatus("Loss");
                        matchStatus.setText("Loss");
                        team.updateRecord(1);
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference dd = database.getReference(team.getTeamName()).child("matches").child(team.getMatchLoc(match) + "").child("matchStatus");
                        dd.setValue("Loss");
                    }
                    team.updateMatch(match);
                    item.setVisible(false);
                    ((GlobalVariables) getApplication()).setTeam(team);
                    dialog.dismiss();


                }
            });
            mBuilder.setView(dCreateView);
            dialog = mBuilder.create();
            dialog.show();
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        team = ((GlobalVariables) getApplication()).getTeam();
    }

    public void setUpStats(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(team.getTeamName()).child("matches").child(team.getMatchLoc(match) + "").child("playerStatistics");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int x = 0;
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    PlayerStatistics ps = new PlayerStatistics(child.child("name").getValue(String.class), (boolean) child.child("finished").getValue());
                    DatabaseReference ref2 = child.child("playerStats").getRef();
                    final int rep = x;
                    ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<Statistic> stats = new ArrayList<>();
                            for (DataSnapshot child2 : dataSnapshot.getChildren()){
                                ArrayList<Integer> nums = new ArrayList<>();
                                nums.add(child2.child("numOne").getValue(Integer.class));
                                nums.add(child2.child("numTwo").getValue(Integer.class));
                                Statistic s = new Statistic(child2.child("name").getValue(String.class), nums);
                                stats.add(s);
                                Log.d("gay", s.getNumOne() + " " + s.getNumTwo());
                            }
                            match.getPlayerStatistics(rep).setPlayerStats(stats);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    x++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
