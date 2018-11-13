package com.example.android.my_app;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class PlayerStatSummary extends AppCompatActivity {

    private Team team;
    private PlayerStatistics playStats;
    private String playerName;
    private Match m;
    private ListView listView;
    private TextView tvPlayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stat_summary);
        playerName = getIntent().getStringExtra("Player");
        team = ((GlobalVariables) getApplication()).getTeam();
        m = ((GlobalVariables) getApplication()).getMatch();
        playStats = m.getPlayerStatistics(playerName);
        tvPlayName = (TextView) findViewById(R.id.textView3);
        tvPlayName.setText(playerName);
        /*View view = findViewById(R.id.stat1);
        ProgressBar pBar = view.findViewById(R.id.progressBar);
        double in = playStats.getStat(0).getNumOne();
        double tot = playStats.getStat(0).getNumTwo() + in;
        double x = (double)(in / tot);
        int lol = (int) (x * 100.0);
        pBar.setProgress(lol);*/
        listView = (ListView) findViewById(R.id.lv_stats);
        List<Statistic> stats = playStats.getPlayerStats();
        StatSummaryAdapter adapter = new StatSummaryAdapter(this, stats);
        listView.setAdapter(adapter);

    }

    public class StatSummaryAdapter extends ArrayAdapter<Statistic> {

        private final Activity mContext;
        private final List<Statistic> itemName;
        public StatSummaryAdapter(@NonNull Activity context, List<Statistic> stats) {
            super(context, R.layout.stat_summary, stats);
            mContext = context;
            itemName = stats;
        }


        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            View view = inflater.inflate(R.layout.stat_summary, null, true);
            TextView statName = (TextView) view.findViewById(R.id.tv_summary_stat_name);
            TextView statPercent = (TextView) view.findViewById(R.id.tv_summary_stat_percentage);
            ProgressBar pBar = (ProgressBar) view.findViewById(R.id.pBarActual);
            ProgressBar backUp = view.findViewById(R.id.pBarBackground);
            statName.setText(itemName.get(position).getName());
            double in = itemName.get(position).getNumOne();
            double tot = itemName.get(position).getNumTwo() + in;
            double x = (double)(in / tot);
            int lol = (int) (x * 100.0);
            statPercent.setText(lol + "%");
            pBar.setProgress(lol);
            if (itemName.get(position).getNumTwo() < 0){
                pBar.setVisibility(View.GONE);
                backUp.setVisibility(View.GONE);
                statPercent.setText(itemName.get(position).getNumOne() + "");
            }
            return view;
        }
    }

}


