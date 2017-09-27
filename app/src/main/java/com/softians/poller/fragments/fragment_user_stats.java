package com.softians.poller.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.softians.poller.R;

import java.util.ArrayList;
import java.util.List;

public class fragment_user_stats extends Fragment{

    float rainfall[] = {98.8f,123.8f,161.6f,24.2f,52f,58.2f,35.4f,13.8f,78.4f,203.4f,240.2f,159.7f};
    String monthNames[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment_user_stats,
                container, false);


        List<PieEntry> pieEntries = new ArrayList<>();
        for(int i=0;i<rainfall.length;i++)
        {
            pieEntries.add(new PieEntry(rainfall[i],monthNames[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,"Rainfall from India");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        PieData data = new PieData(dataSet);
        PieChart chart  = (PieChart)view.findViewById(R.id.piechart1);
        chart.setData(data);
        chart.animateY(1000);
        chart.invalidate();

        return view;
    }


}


