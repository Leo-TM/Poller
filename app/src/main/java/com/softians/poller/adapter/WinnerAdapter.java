package com.softians.poller.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.softians.poller.R;
import com.softians.poller.model.WinnerDataModel;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by HP on 16-09-2017.
 */

public class WinnerAdapter extends RecyclerView.Adapter<WinnerAdapter.ViewHolder> {
    private Context pContext;
    private List<WinnerDataModel> pWinnersRetriever;
    public WinnerAdapter(Context pContext,List pWinnersRetriever)
    {
        this.pContext=pContext;
        this.pWinnersRetriever=pWinnersRetriever;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.winner_cardview,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //holder.winnersName.setText(pWinnersRetriever.get(position).getName()+" has won");
        holder.winnersName.setText(String.format("%s%s",pWinnersRetriever.get(position).getName()," has won"));
        Log.d("**Imagesssss",pWinnersRetriever.get(position).getImage().toString());
        Picasso.with(pContext).load(pWinnersRetriever.get(position).getImage()).into(holder.profilePic);

    }

    @Override
    public int getItemCount() {
        return pWinnersRetriever.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public CardView winnersCardView;
        public ImageView profilePic;
        public TextView winnersName;
        public ViewHolder(View itemView) {
            super(itemView);
            winnersCardView =(CardView)itemView.findViewById(R.id.winners_cardview);
            profilePic = (ImageView)itemView.findViewById(R.id.profilePic);
            winnersName = (TextView )itemView.findViewById(R.id.winnersName);
        }
    }
}

