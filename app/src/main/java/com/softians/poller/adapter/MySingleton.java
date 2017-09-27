package com.softians.poller.adapter;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by HP on 02-09-2017.
 */

public class MySingleton {
    private static MySingleton myInstance;
    private RequestQueue requestQueue;
    private Context mContext;

    private MySingleton(Context context)
    {
        mContext = context;
        requestQueue=getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if(requestQueue ==null)
        {
            requestQueue  = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }
    public static synchronized MySingleton getInstance(Context context)
    {
        if (myInstance == null)
        {
            myInstance = new MySingleton(context);
        }
        return myInstance;
    }
    public <T>void addToRequestQueue(Request<T> request)
    {
        requestQueue.add(request);
    }
}
