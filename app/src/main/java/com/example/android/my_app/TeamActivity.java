package com.example.android.my_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {
    private Team team;
    private TextView teamNameView;
    private TextView sportView;
    private TextView recordView;
    private MatchAdapter matchArrayAdapter;
    private ArrayList<Match> matches;
    private ListView teamListView;
    private Button createMatch;
    private boolean isOwner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        isOwner = false;
        team = ((GlobalVariables) getApplication()).getTeam();
        View teamInfoView = findViewById(R.id.team_info);
        View matchListView = findViewById(R.id.match_list);
        teamNameView = (TextView) teamInfoView.findViewById(R.id.tv_team_name);
        sportView = (TextView) teamInfoView.findViewById(R.id.tv_sport);
        recordView = (TextView) teamInfoView.findViewById(R.id.tv_record);
        teamNameView.setText(team.getTeamName());
        matches = team.getMatches();
        createMatch = (Button) matchListView.findViewById(R.id.create_match_button);
        createMatch.setVisibility(View.INVISIBLE);
        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        final String userName = prefs.getString("Username", "");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Owners").child(team.getTeamName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(String.class).equals(userName)){
                    createMatch.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DatabaseReference ref1 = database.getReference(team.getTeamName()).child("record");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String record = dataSnapshot.child("0").getValue() + " - " + dataSnapshot.child("1").getValue();
                recordView.setText(record + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        teamListView = (ListView) matchListView.findViewById(R.id.match_list_boi);
        String record = team.getRecord(0) + " - " + team.getRecord(1);
        recordView.setText(record);
        sportView.setText(team.getSport());
        matchArrayAdapter = new MatchAdapter(this, matches);
        teamListView.setAdapter(matchArrayAdapter);
        teamListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TeamActivity.this, MatchActivity.class);
                Match m = (Match)teamListView.getItemAtPosition(i);
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference rf = firebaseDatabase.getReference(team.getTeamName()).child("matches").child(i + "");
                rf.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        ((GlobalVariables) getApplication()).setMatch(dataSnapshot.getValue(Match.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                startActivity(intent);
            }
        });
        createMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeamActivity.this, CreateMatchActivity.class);
                startActivity(intent);
            }
        });
        FirebaseDatabase asdf = FirebaseDatabase.getInstance();
        DatabaseReference lol = asdf.getReference(team.getTeamName()).child("teamName");
        lol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                teamNameView.setText(dataSnapshot.getValue() + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        final Menu menu1 = menu;
        final MenuInflater inflater = getMenuInflater();
        SharedPreferences prefs = getSharedPreferences("userdetails", MODE_PRIVATE);
        final String userName = prefs.getString("Username", "");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Owners").child(team.getTeamName());
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("queef", dataSnapshot.getValue(String.class));
                if (dataSnapshot.getValue(String.class).equals(userName)){
                    Log.d("queef", dataSnapshot.getValue(String.class));
                    getMenuInflater().inflate(R.menu.players, menu1);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.player_list){
        Intent intent = new Intent(TeamActivity.this, PlayerListActivity.class);
        startActivity(intent);
        }
    return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        team = ((GlobalVariables) getApplication()).getTeam();
        matchArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public class MatchAdapter extends ArrayAdapter<Match> {

        private final Activity mContext;
        private final ArrayList<Match> itemName;
        public MatchAdapter(@NonNull Activity context, ArrayList<Match> match) {
            super(context, R.layout.match_view, match);
            mContext = context;
            itemName = match;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            View view = inflater.inflate(R.layout.match_view, null, true);
            TextView opponentName = (TextView) view.findViewById(R.id.textView4);
            TextView date = (TextView) view.findViewById(R.id.textView5);

            opponentName.setText(itemName.get(position).getOpponent() + "");
            date.setText(itemName.get(position).getDate());
            return view;
        }
    }

}
