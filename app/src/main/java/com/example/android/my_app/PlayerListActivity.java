package com.example.android.my_app;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlayerListActivity extends AppCompatActivity {
    private ArrayList<String> players;
    private ListView listView;
    ArrayAdapter adapter;
    Team team;
    AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        team = ((GlobalVariables) getApplication()).getTeam();
        FirebaseDatabase asdf = FirebaseDatabase.getInstance();
        DatabaseReference lol = asdf.getReference(team.getTeamName()).child("members");
        players = team.getMembers();
        listView = (ListView) findViewById(R.id.players_list);
        adapter = new ArrayAdapter(PlayerListActivity.this, android.R.layout.simple_list_item_1, players);
        listView.setAdapter(adapter);
        lol.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                players = (ArrayList<String>)dataSnapshot.getValue();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.players_add, menu);
        return true;
    }

    public void updateList(Team t){
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.player_add_option){
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlayerListActivity.this);
            View dCreateView = getLayoutInflater().inflate(R.layout.add_player_dialog, null);
            final EditText playerNameEditText = (EditText) dCreateView.findViewById(R.id.et_player_name);
            Button addButton = (Button) dCreateView.findViewById(R.id.add_player_bt);
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!playerNameEditText.getText().toString().isEmpty())
                    {
                        team.addPlayer(playerNameEditText.getText().toString());
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference(team.getTeamName()).child("members");
                        myRef.setValue(team.getMembers());
                        ((GlobalVariables) getApplication()).setTeam(team);
                        dialog.dismiss();
                        adapter.notifyDataSetChanged();
                    }
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
        adapter.notifyDataSetChanged();

    }
}

