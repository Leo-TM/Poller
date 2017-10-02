package com.softians.poller.activitys;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softians.poller.R;
import com.softians.poller.adapter.MySingleton;
import com.softians.poller.app.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import customfonts.MyEditText;
import customfonts.MyTextView;

public class LoginRegister extends AppCompatActivity {
    //checking how it goes
    ImageView logoclick ;
    MyTextView signin,signup;
    ConstraintLayout constraintLayout;
    MyEditText email,pas;
    public static String userId,userName;
    AlertDialog.Builder builder ;
    String log_url = Config.link+"login.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemClock.sleep(1000);
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        getSupportActionBar().hide();
        constraintLayout = (ConstraintLayout)findViewById(R.id.constraint);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        signin = (MyTextView)findViewById(R.id.sin);
        logoclick = (ImageView)findViewById(R.id.onaddi);
        signup = (MyTextView)findViewById(R.id.sign_up);
        email = (MyEditText) findViewById(R.id.usrtf);
        pas =(MyEditText)findViewById(R.id.pastf);
        builder = new AlertDialog.Builder(LoginRegister.this);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((email.getText().toString()).equals("") || (pas.getText().toString()).equals(""))
                {
                    builder.setTitle("Something went wrong");
                    builder.setMessage("Empty Field");
                    displayAlert("input_error");
                }
                else
                {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, log_url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code1 = jsonObject.getString("code");
                                String message1 = jsonObject.getString("message");
                                if(jsonObject.has("userId") && jsonObject.has("userName")) {
                                    userId = jsonObject.getString("userId");
                                    userName = jsonObject.getString("userName");
                                }
                                builder.setTitle("Server Response ...");
                                builder.setMessage(message1);
                                displayAlert(code1);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            SharedPreferences sharedPref = getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
                            Map<String,String>params = new HashMap<String, String>();
                            params.put("email",(email.getText().toString()));
                            params.put("user_password",(pas.getText().toString()));
                            params.put("token",sharedPref.getString("regId",null));
                            return params;
                        }
                    };
                    MySingleton.getInstance(LoginRegister.this).addToRequestQueue(stringRequest);
                    Intent ints = new Intent(LoginRegister.this,ProfilePage.class);
                    startActivity(ints);
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intents = new Intent(LoginRegister.this,Registration.class);
                startActivity(intents);
            }
        });
    }

    private void displayAlert(final String code) {

        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(code.equals("input_error"))
                {
                    email.setText("");
                    pas.setText("");
                }
                else if(code.equals("success"))
                {
                    SharedPreferences sharedPref = getSharedPreferences(Config.SHARED_PREF,MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("userId",userId);
                    editor.putString("userName",userName);
                    editor.apply();
                    Intent ints = new Intent (LoginRegister.this,ProfilePage.class);
                    startActivity(ints);
                    finish();
                }
                else if (code.equals("failed"))
                {
                    email.setText("");
                    pas.setText("");
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
