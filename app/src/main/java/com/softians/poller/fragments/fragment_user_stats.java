package com.softians.poller.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.softians.poller.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class fragment_user_stats extends Fragment{
    private com.github.lzyzsd.circleprogress.DonutProgress pDonutProgress;
    private View pFirstView;
    private TextView pLastScore;
    private TextView pValueForTotalTests;
    private View pSecondView;
    private TextView pTestsTaken;
    public static String nots;
    public static int percent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_user_stats,
                container, false);
        pDonutProgress = (com.github.lzyzsd.circleprogress.DonutProgress) view.findViewById(R.id.donutProgress);
        //pLastScore = (TextView) view.findViewById(R.id.LastScore);
        pValueForTotalTests = (TextView) view.findViewById(R.id.ValueForTotalTests);
        //pTestsTaken = (TextView) view.findViewById(R.id.TestsTaken);
        try {
            getContents(1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("***bf setting",String.valueOf(percent));
        pDonutProgress.setProgress((percent));
        pValueForTotalTests.setText(nots);
        return view;
    }


    private void getContents(final int id) throws ExecutionException, InterruptedException {
        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.104:802/poller/stats.php?id="+id).build();

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


