package com.softians.poller.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softians.poller.R;
import com.softians.poller.adapter.QuestionListAdapter;
import com.softians.poller.model.QuestionDataModel;
import com.victor.loading.rotate.RotateLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ShowQuestionList extends AppCompatActivity {
    RecyclerView recyclerView;
    QuestionListAdapter questionListAdapter;
    LinearLayoutManager linearLayoutManager;
    TextView individualTextView,statusText;
    RotateLoading rotateLoading;
    List<QuestionDataModel> questionListPutter;
    private Button submit;
    private static long end_timer;
    private static int topicId =0;
    public static Map<String,String> questionAnswerPair;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    Handler pHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_question_list);
        //getCurrentFocus().clearFocus();
        recyclerView = (RecyclerView)findViewById(R.id.recycler_for_questions);
        individualTextView=(TextView)findViewById(R.id.Individualtimer);
        pHandler.postDelayed(runnables,1000);

        recyclerView.setHasFixedSize(true);
        questionListPutter=new ArrayList<>();
        questionAnswerPair = new HashMap<String, String>();
        linearLayoutManager = new LinearLayoutManager(this);
        questionListAdapter = new QuestionListAdapter(this,questionListPutter);
        submit=(Button)findViewById(R.id.submit_answers);
        statusText = (TextView) findViewById(R.id.statusText);
        rotateLoading=(RotateLoading)findViewById(R.id.rotateloading);
        recyclerView.setLayoutManager(linearLayoutManager);
        builder = new AlertDialog.Builder(ShowQuestionList.this);
        recyclerView.setAdapter(questionListAdapter);
        Intent incoming = getIntent();
        topicId = incoming.getIntExtra("topicId",0);
        load_questions_from_server(0,topicId);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(linearLayoutManager.findLastCompletelyVisibleItemPosition()==questionListPutter.size()-1);
                {
                    load_questions_from_server(questionListPutter.get(questionListPutter.size()-1).getquestionId(),topicId);
                }
            }
        });
        recyclerView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        recyclerView.setFocusable(true);
        recyclerView.setFocusableInTouchMode(true);
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });




 /*       recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(getCurrentFocus() instanceof EditText)
                {
                    EditText bloodyEditText = (EditText) getCurrentFocus();
                    if(bloodyEditText.hasFocus())
                    {
                        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                        bloodyEditText.clearFocus();
                    }
                }
                return false;
            }
        });*/



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QuestionListAdapter.questionAnswerPair.put("userid","1");
                QuestionListAdapter.questionAnswerPair.put("topicid",String.valueOf(topicId));
                QuestionListAdapter.questionAnswerPair.put("submission_time",String.valueOf(System.currentTimeMillis()));

                ObjectMapper objectMapper = new ObjectMapper();
                String json="";
                objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                try {
                    json = objectMapper.writer().writeValueAsString(QuestionListAdapter.questionAnswerPair);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
                final String finalJson= json;
                Log.d("***JSON",finalJson);


                AsyncTask<String,Void,Void> task2 = new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().connectTimeout(10,TimeUnit.SECONDS).writeTimeout(10,TimeUnit.SECONDS).readTimeout(30,TimeUnit.SECONDS).build();
                        RequestBody requestBody2 = RequestBody.create(JSON,finalJson);
                        Request request2 =new Request.Builder().url("http://192.168.1.104:802/poller/jsonQuestionAnswer.php").post(requestBody2).build();

                        try {
                            Response response2 = okHttpClient.newCall(request2).execute();
                            String responseInStr = response2.body().string();
                            Log.d("***response",responseInStr.toString());
                            if((responseInStr.toString()).equals("YRS"))
                            {
                                new Thread()
                                {
                                    public void run()
                                    {
                                        ShowQuestionList.this.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                builder.setTitle("Done");
                                                builder.setMessage("Your response has been recorded");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent ints = new Intent (ShowQuestionList.this,Tablay.class);
                                                        startActivity(ints);
                                                    }
                                                });
                                                alertDialog = builder.create();
                                                alertDialog.show();
                                            }
                                        });
                                    }
                                }.start();

                            }
                            else if((responseInStr.toString()).equals("YRS"))
                            {

                                new Thread()
                                {
                                    public void run()
                                    {
                                        ShowQuestionList.this.runOnUiThread(new Runnable()
                                        {
                                            public void run()
                                            {
                                                builder.setTitle("Failed");
                                                builder.setMessage("Invalid user credentials");
                                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent ints = new Intent (ShowQuestionList.this,Tablay.class);
                                                        startActivity(ints);
                                                    }
                                                });
                                                alertDialog = builder.create();
                                                alertDialog.show();
                                            }
                                        });
                                    }
                                }.start();

                            }
                            else
                            {
                            new Thread()
                            {
                                public void run()
                                {
                                    ShowQuestionList.this.runOnUiThread(new Runnable()
                                    {
                                        public void run()
                                        {
                                            builder.setTitle("Server Down");
                                            builder.setMessage("Please try after sometime");
                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Intent ints = new Intent (ShowQuestionList.this,Tablay.class);
                                                    startActivity(ints);
                                                }
                                            });
                                            alertDialog = builder.create();
                                            alertDialog.show();
                                        }
                                    });
                                }
                            }.start();

                        }




                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        return null;
                    }
                };
                task2.execute("");





            }

        });

    }


    public void load_questions_from_server(final int id,final int topicId)
    {
        AsyncTask<Integer,Void,Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.104:802/poller/list_of_questions_feed.php?id="+id+"&topic="+topicId).build();
                try {
                    Response response = client.newCall(request).execute();
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    end_timer = jsonObject.getLong("end_tr");
                    Log.d("end timer",String.valueOf(end_timer));
                    QuestionDataModel questionDataModel;
                    for(int i=1;i<jsonArray.length();i++)
                    {
                        jsonObject = jsonArray.getJSONObject(i);
                        questionDataModel = new QuestionDataModel(jsonObject.getInt("question_id"),jsonObject.getString("questions"));
                        questionListPutter.add(questionDataModel);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (JSONException je)
                {
                    System.out.print("End of docs");
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                questionListAdapter.notifyDataSetChanged();
            }
        };
        task.execute(id);

    }



    private Runnable runnables = new Runnable() {
        @Override
        public void run() {
            long timediff = end_timer -System.currentTimeMillis();
            if(timediff > 0)
            {
                int seconds = (int) (timediff / 1000) % 60;
                int minutes = (int) ((timediff / (1000 * 60)) % 60);
                int hours = (int) ((timediff / (1000 * 60 * 60)) % 24);
                int days = (int)(timediff/(1000*60*60*24));

                //Log.d("***timediff",Long.toString(timeDiff));

                individualTextView.setText(days +" days "+hours + " hrs " + minutes + " mins " + seconds + " sec ");
                pHandler.postDelayed(this,1000);
            }
        }
    };

}
