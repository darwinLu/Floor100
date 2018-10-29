package com.example.lx.floor100;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lx.floor100.entity.RankItem;
import com.example.lx.floor100.hud.Rank;

import java.util.ArrayList;
import java.util.List;

public class RankActivity extends AppCompatActivity {

    private RecyclerView rankRecyclerView;
    private List<RankItem> mRankList = new ArrayList<>();
    private Button exitRankButton;

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
        for(int i=0;i<10;i++){
            RankItem rankItem = new RankItem();
            rankItem.setUserId("user"+i);
            rankItem.setScore(i);
            mRankList.add(rankItem);
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
