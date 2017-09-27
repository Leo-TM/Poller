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
import com.softians.poller.adapter.CustomAdapterTopics;
import com.softians.poller.model.TopicList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class fragment_question extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    CustomAdapterTopics customAdapter;
    List<TopicList> topicLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_question_topics,
                container, false);


        recyclerView = (RecyclerView)view.findViewById(R.id.topics_recycler_view);
        recyclerView.setHasFixedSize(true);
        topicLists = new ArrayList<>();
        load_topic_from_server(0);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        customAdapter = new CustomAdapterTopics(this.getContext(),topicLists);
        recyclerView.setAdapter(customAdapter);
        /*recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==topicLists.size()-1);
            }
        });*/
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==topicLists.size()-1)
                {
                    load_topic_from_server(topicLists.get(topicLists.size()-1).getId());
                }
            }
        });
        return view;
    }

    private void load_topic_from_server(final int id) {
        AsyncTask<Integer,Void,Void> task= new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                //Request request =new Request.Builder().url(String.format("%s%s%d", CommonFloatingThings.links,"json_provider_for_recycler.php?id=",id)).build();
                Request request =new Request.Builder().url("http://192.168.1.104:802/poller/json_provider_for_recycler.php?id="+id).build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray jArray = new JSONArray(response.body().string());
                    for(int i=0;i<jArray.length();i++)
                    {

                        JSONObject jsonObject = jArray.getJSONObject(i);
                        String longtostr = String.valueOf(jsonObject.getLong("end_tr"));
                        TopicList topic = new TopicList(jsonObject.getInt("id"),longtostr,jsonObject.getString("topics"));
                        topicLists.add(topic);

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
                customAdapter.notifyDataSetChanged();
            }
        };
        task.execute(id);
    }


    @Override
    public void onClick(View v) {
    /*    switch (v.getId()) {

            case R.id.card_view:
                //"Block SMSs" button pressed

                Intent intent=new Intent(getActivity(), QuestionActivity.class);
                startActivity(intent);

//                Toast.makeText(getActivity(), "Click", Toast.LENGTH_LONG).show();
                break;

        }*/
    }
}
