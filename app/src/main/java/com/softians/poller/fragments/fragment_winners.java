package com.softians.poller.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softians.poller.R;
import com.softians.poller.adapter.WinnerAdapter;
import com.softians.poller.model.WinnerDataModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_winners extends Fragment {


    RecyclerView winnersRecyclerView;
    LinearLayoutManager linearLayoutManager;
    WinnerAdapter winnersAdapter;
    List<WinnerDataModel> winnersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_winners,
                container, false);


        winnersRecyclerView = (RecyclerView)view.findViewById(R.id.winners_recycler_winner);
        winnersRecyclerView.setHasFixedSize(true);
        winnersList = new ArrayList<>();
        load_winners_from_server(0);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        winnersRecyclerView.setLayoutManager(linearLayoutManager);
        winnersAdapter = new WinnerAdapter(this.getContext(),winnersList);
        winnersRecyclerView.setAdapter(winnersAdapter);
        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==topicLists.size()-1);
            }
        });*/
        winnersRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==winnersList.size()-1)
                {
                    load_winners_from_server(winnersList.get(winnersList.size()-1).getId());
                }
            }
        });
        return view;
    }

    private void load_winners_from_server(final int id) {
        AsyncTask<Integer,Void,Void> winnertask= new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                //Request request =new Request.Builder().url(String.format("%s%s%d", CommonFloatingThings.links,"json_provider_for_recycler.php?id=",id)).build();
                Request request =new Request.Builder().url("http://192.168.1.104:802/poller/winners_feed.php?id="+id).build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray jArray = new JSONArray(response.body().string());
                    for(int i=0;i<jArray.length();i++)
                    {

                        JSONObject jsonObject = jArray.getJSONObject(i);
                        String longtostrDate = String.valueOf(jsonObject.getLong("pdate"));
                        WinnerDataModel winner = new WinnerDataModel(jsonObject.getInt("id"),jsonObject.getString("names"),jsonObject.getString("picture"),longtostrDate);
                        winnersList.add(winner);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException je)
                {
                    System.out.print("End of content");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                winnersAdapter.notifyDataSetChanged();
            }
        };
        winnertask.execute(id);
    }

}
