package com.softians.poller.fragments;


import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softians.poller.R;
import com.softians.poller.app.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class fragment_user_stats extends Fragment{
    private com.github.lzyzsd.circleprogress.DonutProgress pDonutProgress;
    private View pFirstView;
    private TextView pLastScore;
    private TextView pValueForTotalTests;
    private View pSecondView;
    private TextView pTestsTaken;
    public static String nots;
    public static int percent;
    //SharedPreferences sharedPref = (Tablay.class).getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
    //int userId = Integer.parseInt(sharedPref.getString("userId","0"));
    int userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        SharedPreferences sharedPref = getActivity().getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
        userId = Integer.parseInt(sharedPref.getString("userId","0"));


        View view = inflater.inflate(R.layout.fragment_fragment_user_stats,
                container, false);
        pDonutProgress = (com.github.lzyzsd.circleprogress.DonutProgress) view.findViewById(R.id.donutProgress);
        //pLastScore = (TextView) view.findViewById(R.id.LastScore);
        pValueForTotalTests = (TextView) view.findViewById(R.id.ValueForTotalTests);
        //pTestsTaken = (TextView) view.findViewById(R.id.TestsTaken);
        try {
            getContents(userId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pDonutProgress.setProgress((percent));
        pValueForTotalTests.setText(nots);
        return view;
    }


    private void getContents(final int id) throws ExecutionException, InterruptedException {
        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(Config.link+"stats.php?id="+id).build();

                try {
                    Response response = client.newCall(request).execute();
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    JSONObject jobs;
                    jobs = jsonArray.getJSONObject(0);
                    percent=jobs.getInt("percent");
                    nots=jobs.getString("nots");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException je)
                {
                    System.out.println("EOF");
                }
                return null;
            }
        };
        task.execute().get();

        //pTestsTaken.setText(nots);
    }


}


