package com.example.lx.floor100.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lx.floor100.R;
import com.example.lx.floor100.hud.RankItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class RankActivity extends AppCompatActivity {

    private RecyclerView rankRecyclerView;
    private List<RankItem> mRankList = new ArrayList<>();
    private Button exitRankButton;
    private String[] users = {"Ted Jimmy","Bill Carter","Newman Christian","Maureen North","Porter Elizabeth",
            "Conrad Pritt","Hannah Samson","Sara Rose","Novia Maxwell","Cheryl Dobbin"};
    private Random rand = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        loadRank();
        rankRecyclerView = (RecyclerView)findViewById(R.id.rank_recycler_view);
        rankRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RankAdapter adapter = new RankAdapter(mRankList);
        rankRecyclerView.setAdapter(adapter);
        exitRankButton = (Button) findViewById(R.id.exit_rank);
        exitRankButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RankActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadRank() {
        SharedPreferences option = getSharedPreferences("option",MODE_PRIVATE);
        String userId = option.getString("userId","");
        int score = option.getInt("score",0);
        if(userId.equals("")){
            for(int i=0;i<10;i++){
                RankItem rankItem = new RankItem();
                rankItem.setUserId(users[i]);
                rankItem.setScore(rand.nextInt(100));
                mRankList.add(rankItem);
            }
        }
        else{
            for(int i=0;i<10;i++){
                RankItem rankItem = new RankItem();
                rankItem.setUserId(users[i]);
                rankItem.setScore(rand.nextInt(100));
                mRankList.add(rankItem);
            }
            RankItem rankItem = new RankItem();
            rankItem.setUserId(userId);
            rankItem.setScore(score);
            mRankList.add(rankItem);
            Collections.sort(mRankList, new Comparator<RankItem>() {
                @Override
                public int compare(RankItem o1, RankItem o2) {
                    if(o1.getScore()<o2.getScore()){
                        return 1;
                    }
                    else
                        return -1;
                }
            });
        }
    }

    private class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder>{
        private List<RankItem> mRankList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView userId;
            TextView score;

            public ViewHolder(View view){
                super(view);
                userId = (TextView)view.findViewById(R.id.user_id);
                score = (TextView)view.findViewById(R.id.score);
            }
        }

        public RankAdapter(List<RankItem> rankList){
            mRankList = rankList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rank_item_layout,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            RankItem rankItem = mRankList.get(position);
            holder.userId.setText(rankItem.getUserId());
            holder.score.setText(Integer.toString(rankItem.getScore()));
        }

        @Override
        public int getItemCount() {
            return mRankList.size();
        }
    }
}
