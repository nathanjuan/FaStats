package com.example.android.my_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CreateMatchActivity extends AppCompatActivity {

    private Team team;
    private EditText etOpponent;
    private EditText etDate;
    private Spinner spinner;
    private ScrollView scrollView;
    private ArrayList<String> bleh;
    private View stat_one;
    private View stat_two;
    private View stat_three;
    private View stat_four;
    private View stat_five;
    private View stat_six;
    private EditText[] statNames;
    /*private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioGroup rg3;
    private RadioGroup rg4;
    private RadioGroup rg5;
    private RadioGroup rg6;*/
    private RadioGroup[] radioGroups;
    private Button matchCreate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_match);
        scrollView = findViewById(R.id.lv_create_stats);
        statNames = new EditText[6];
        radioGroups = new RadioGroup[6];
        stat_one = findViewById(R.id.stat1);
        statNames[0] = (EditText) stat_one.findViewById(R.id.et_stat_name);
        radioGroups[0] = (RadioGroup) stat_one.findViewById(R.id.rg_stat_type);
        stat_two = findViewById(R.id.stat2);
        statNames[1] = (EditText) stat_two.findViewById(R.id.et_stat_name);
        radioGroups[1] = (RadioGroup) stat_two.findViewById(R.id.rg_stat_type);
        stat_two.setVisibility(View.INVISIBLE);
        stat_three = findViewById(R.id.stat3);
        statNames[2] = (EditText) stat_three.findViewById(R.id.et_stat_name);
        radioGroups[2] = (RadioGroup) stat_three.findViewById(R.id.rg_stat_type);
        stat_three.setVisibility(View.INVISIBLE);
        stat_four = findViewById(R.id.stat4);
        statNames[3] = (EditText) stat_four.findViewById(R.id.et_stat_name);
        radioGroups[3] = (RadioGroup) stat_four.findViewById(R.id.rg_stat_type);
        stat_four.setVisibility(View.INVISIBLE);
        stat_five = findViewById(R.id.stat5);
        statNames[4] = (EditText) stat_five.findViewById(R.id.et_stat_name);
        radioGroups[4] = (RadioGroup) stat_five.findViewById(R.id.rg_stat_type);
        stat_five.setVisibility(View.INVISIBLE);
        stat_six = findViewById(R.id.stat6);
        statNames[5] = (EditText) stat_six.findViewById(R.id.et_stat_name);
        radioGroups[5] = (RadioGroup) stat_six.findViewById(R.id.rg_stat_type);
        stat_six.setVisibility(View.INVISIBLE);
        matchCreate = (Button) findViewById(R.id.bt_create_match);
        matchCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = spinner.getSelectedItemPosition() + 1;
                if (!etOpponent.getText().toString().isEmpty() && !etDate.getText().toString().isEmpty()){
                    Match match = new Match(etDate.getText().toString(), etOpponent.getText().toString(), team.getMembers());

                    ArrayList<Statistic> stats = new ArrayList<>();
                    for (int i = 0; i < x; i++){
                        stats.add(new Statistic(statNames[i].getText().toString(), statType(radioGroups[i])));
                    }
                    match.addStats(stats);
                    team.addMatch(match);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference(team.getTeamName());
                    myRef.setValue(team);
                    ((GlobalVariables) getApplication()).setTeam(team);
                    Intent intent = new Intent(CreateMatchActivity.this, MatchActivity.class);
                    ((GlobalVariables) getApplication()).setMatch(match);
                    startActivity(intent);
                }
            }
        });
        team = ((GlobalVariables) getApplication()).getTeam();
        super.onCreate(savedInstanceState);
        etOpponent = (EditText) findViewById(R.id.et_opponent);
        etDate = (EditText) findViewById(R.id.et_date);
        spinner = findViewById(R.id.spinner_num_stats);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.StatsNumbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stat_one.setVisibility(View.GONE);
                stat_two.setVisibility(View.GONE);
                stat_three.setVisibility(View.GONE);
                stat_four.setVisibility(View.GONE);
                stat_five.setVisibility(View.GONE);
                stat_six.setVisibility(View.GONE);
                int x = i + 1;
                for (int rep = 0; rep < x; rep++){
                    if (rep == 0){
                        stat_one.setVisibility(View.VISIBLE);
                    }
                    if (rep == 1){
                        stat_two.setVisibility(View.VISIBLE);
                    }
                    if (rep == 2){
                        stat_three.setVisibility(View.VISIBLE);
                    }
                    if (rep == 3){
                        stat_four.setVisibility(View.VISIBLE);
                    }
                    if (rep == 4){
                        stat_five.setVisibility(View.VISIBLE);
                    }
                    if (rep == 5){
                        stat_six.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private int statType(RadioGroup rg){
        int radioButtonID = rg.getCheckedRadioButtonId();
        View radioButton = rg.findViewById(radioButtonID);
        return rg.indexOfChild(radioButton);

    }
}
