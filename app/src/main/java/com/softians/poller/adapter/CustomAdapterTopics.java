package com.softians.poller.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softians.poller.R;
import com.softians.poller.activitys.ShowQuestionList;
import com.softians.poller.model.TopicList;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by HP on 05-09-2017.
 */


public class CustomAdapterTopics extends RecyclerView.Adapter<CustomAdapterTopics.ViewHolder> {
    private Context pContext;
    private List<TopicList> topicLists;
    private final List<ViewHolder> viewHolderlist;
    private Handler pHandler = new Handler();

    private Runnable updateRemainingTimeRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (viewHolderlist)
            {
                long currentTime = System.currentTimeMillis();
                for(ViewHolder holder : viewHolderlist)
                {
                    holder.updateTimeRemaining(currentTime);
                }
            }
        }
    };

    public CustomAdapterTopics(Context pContext, List<TopicList> topicLists)
    {
        super();
        this.pContext=pContext;
        this.topicLists=topicLists;
        viewHolderlist = new ArrayList<>();
        startUpdateTimer();
    }
    private void startUpdateTimer()
    {
        Timer tmr = new Timer();
        tmr.schedule(new TimerTask() {
            @Override
            public void run() {
                pHandler.post(updateRemainingTimeRunnable);
            }
        },1000,1000);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_card_view,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.pTimer.setTag(position);
        holder.setData(topicLists.get(position));
        if(position%2==0)
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#ffce00"));
        }
        else
        {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#011f4b"));
        }
        synchronized (viewHolderlist)
        {
            viewHolderlist.add(holder);
        }
        holder.updateTimeRemaining(System.currentTimeMillis());
    }

    @Override
    public int getItemCount() {
        return topicLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {
        public TextView pTimer,pTopics;
        TopicList mTopicList;
        CardView cardView;

        public void setData(TopicList topl)
        {
            mTopicList=topl;
            pTopics.setText(mTopicList.getpTopicName());
            updateTimeRemaining(System.currentTimeMillis());
        }
        public void updateTimeRemaining( long timesNow)
        {
            long getEndtime = Long.parseLong(mTopicList.getpEndTimer());
            long timeDiff = (getEndtime-timesNow);
            if(timeDiff>0) {
                int seconds = (int) (timeDiff / 1000) % 60;
                int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
                int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
                int days = (int)(timeDiff/(1000*60*60*24));

                //Log.d("***timediff",Long.toString(timeDiff));

                pTimer.setText(days +" days "+hours + " hrs " + minutes + " mins " + seconds + " sec ");
            }
            else
            {
                int pPosition = (int)pTimer.getTag();
                removeAt(pPosition);
            }


        }
        public ViewHolder(View itemView) {
            super(itemView);
            pTimer = (TextView)itemView.findViewById(R.id.timerID);
            pTopics = (TextView)itemView.findViewById(R.id.topicID);
            cardView = (CardView)itemView.findViewById(R.id.topic_card_view);
            Typeface number = Typeface.createFromAsset(pContext.getAssets(), "fonts/number.ttf");
            pTimer.setTypeface(number);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int sendTopicId = topicLists.get(getAdapterPosition()).getId();
            Intent toQuestion = new Intent(pContext, ShowQuestionList.class);
            toQuestion.putExtra("topicId",sendTopicId);
            pContext.startActivity(toQuestion);
        }
    }


    public void removeAt(int position) {
        topicLists.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, topicLists.size());
    }


}
