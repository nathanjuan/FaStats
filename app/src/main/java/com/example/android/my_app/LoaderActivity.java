package com.example.android.my_app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class LoaderActivity extends AppCompatActivity {
    private ArrayList<Team> teams;
    private ListView listView;
    private TeamAdapter teamAdapter;
    private AlertDialog dialog;
    private TextView noTeamsView;
    private FirebaseDatabase database;
    private SharedPreferences savedInfo;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        savedInfo = getSharedPreferences("userdetails", MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        teams = new ArrayList<>();
        userName = savedInfo.getString("Username", "");
        if (userName.equals("")){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoaderActivity.this);
            View dCreateView = getLayoutInflater().inflate(R.layout.setup_dialog, null);
            mBuilder.setView(dCreateView);
            dialog = mBuilder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            final EditText et = dCreateView.findViewById(R.id.editText);
            Button but = dCreateView.findViewById(R.id.button2);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!et.getText().toString().isEmpty()){
                        final String s = et.getText().toString();
                        final DatabaseReference dataref = database.getReference("Usernames");
                        dataref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(s)){
                                    Toast.makeText(LoaderActivity.this, "Username Taken", Toast.LENGTH_SHORT).show();
                                } else {
                                    dataref.child(s).setValue(s);
                                    userName = s;
                                    dialog.dismiss();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }
        for (int x = 0; x < savedInfo.getInt("Number of Teams", 0); x++){
            final int rep = x;
            String teamS = savedInfo.getString("Team " + x, "");
            Gson gson = new Gson();
            Team t = gson.fromJson(teamS, Team.class);
            DatabaseReference dr = database.getReference(t.getTeamName()).child("matches");
            teams.add(t);
            dr.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ArrayList<Match> m = new ArrayList<>();
                    for (DataSnapshot child: dataSnapshot.getChildren()){
                        m.add(child.getValue(Match.class));
                    }
                    teams.get(rep).setMatches(m);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //teams.add(t);
        }
        //for(int rep = 0; rep < teams.size(); rep++){
        //    DatabaseReference dbr = database.getReference(teams.get());
        //}
        String s = teams.size() + "";
        Log.d("gay", s);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        noTeamsView = (TextView) findViewById(R.id.no_team_message);
        listView= (ListView) findViewById(R.id.recycler_view);
        teamAdapter = new TeamAdapter(this, teams);
        listView.setAdapter(teamAdapter);
        if (teams.size() < 1){
            noTeamsView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            noTeamsView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(LoaderActivity.this, TeamActivity.class);
                database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference(listView.getItemAtPosition(i).toString());
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Team t = dataSnapshot.getValue(Team.class);
                        ((GlobalVariables) getApplication()).setTeam(t);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                ((GlobalVariables) getApplication()).setTeam((Team)listView.getItemAtPosition(i));
                startActivity(intent);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.teams_menu, menu);
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        teamAdapter.notifyDataSetChanged();
        if (teams.size() > 0){
            noTeamsView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor ed = savedInfo.edit();
        Gson gson = new Gson();
        ed.putInt("Number of Teams", teams.size());
        ed.putString("Username", userName);
        for (int x = 0; x < teams.size(); x++){
            String xd = "Team " + x;
            ed.putString(xd, gson.toJson(teams.get(x)));
        }
        ed.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.team_create){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoaderActivity.this);
            View dCreateView = getLayoutInflater().inflate(R.layout.create_team_dialog, null);
            final EditText dTeamName = (EditText) dCreateView.findViewById(R.id.editTextName);
            final EditText dSport = (EditText) dCreateView.findViewById(R.id.editTextSport);
            Button createButton = (Button) dCreateView.findViewById(R.id.createTeam);
            createButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!dTeamName.getText().toString().isEmpty() && !dSport.getText().toString().isEmpty())
                    {
                        Team newT = new Team(dTeamName.getText().toString(),dSport.getText().toString());
                        teams.add(newT);
                        ((GlobalVariables) getApplication()).setTeams(teams);
                        SharedPreferences.Editor prefsEditor = savedInfo.edit();
                        Gson gson = new Gson();
                        //prefsEditor.putString("Team " + teams.size(), gson.toJson(newT));
                        //prefsEditor.putInt("Number of Teams", savedInfo.getInt("Number of Teams", 0) + 1);
                        //prefsEditor.apply();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(newT.getTeamName());
                        myRef.setValue(newT);
                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ((GlobalVariables) getApplication()).setTeam(dataSnapshot.getValue(Team.class));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        DatabaseReference teamRef = database.getReference("Owners");
                        teamRef.child(newT.getTeamName()).setValue(userName);
                        ((GlobalVariables) getApplication()).setTeam(newT);
                        Toast.makeText(LoaderActivity.this, "Team Created!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoaderActivity.this, TeamActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }

                }
            });
            mBuilder.setView(dCreateView);
            dialog = mBuilder.create();
            dialog.show();
            return true;
        } else if (itemId == R.id.team_join){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoaderActivity.this);
            View dCreateView = getLayoutInflater().inflate(R.layout.join_team_dialog, null);
            final EditText textName = dCreateView.findViewById(R.id.editTextName);
            Button bt = dCreateView.findViewById(R.id.joinTeam);
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase fireBase = FirebaseDatabase.getInstance();
                    final String teamName = textName.getText().toString();
                    final DatabaseReference dbr = fireBase.getReference(teamName);
                    dialog.dismiss();
                        dbr.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child("teamName").getValue() == null){
                                    dbr.removeValue();
                                    Toast.makeText(LoaderActivity.this, "No Team Exists", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Team t = dataSnapshot.getValue(Team.class);
                                    teamAdapter.add(t);
                                    ((GlobalVariables) getApplication()).setTeam(t);
                                    Intent intent = new Intent(LoaderActivity.this, TeamActivity.class);
                                    startActivity(intent);
                                    SharedPreferences.Editor prefsEditor = savedInfo.edit();
                                    Gson gson = new Gson();
                                    //prefsEditor.putString("Team " + teams.size(), gson.toJson(t));
                                    //prefsEditor.putInt("Number of Teams", savedInfo.getInt("Number of Teams", 0) + 1);
                                    //prefsEditor.apply();
                                    Toast.makeText(LoaderActivity.this, "Joined Team!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                }
            });
            mBuilder.setView(dCreateView);
            dialog = mBuilder.create();
            dialog.show();
            return true;
        }
        return false;
    }


    public class TeamAdapter extends ArrayAdapter<Team> {

        private final Activity mContext;
        private final List<Team> itemName;
        public TeamAdapter(@NonNull Activity context, List<Team> teams) {
            super(context, R.layout.stat_summary, teams);
            mContext = context;
            itemName = teams;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            View view = inflater.inflate(R.layout.team_view, null, true);
            TextView teamName = (TextView) view.findViewById(R.id.tv_team_name_s);
            TextView sportName = (TextView) view.findViewById(R.id.tv_sport_name);
            TextView record = (TextView) view.findViewById(R.id.tv_record);
            teamName.setText(itemName.get(position).getTeamName() + "");
            sportName.setText(itemName.get(position).getSport());
            if (itemName.get(position).getSport().equals("Basketball")){
                sportName.setText("Tennis");
            }
            ArrayList<Integer>  r = (ArrayList<Integer>) itemName.get(position).getRecord();
            record.setText(r.get(0) + " - " + r.get(1));
            return view;
        }
    }
}
