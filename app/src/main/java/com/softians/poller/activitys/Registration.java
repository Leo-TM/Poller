package com.softians.poller.activitys;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.softians.poller.R;
import com.softians.poller.adapter.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    private ImageView pOnaddi;
    private customfonts.MyEditText pEmail_id;
    private customfonts.MyEditText pPhone_no;
    private customfonts.MyEditText pAddress;
    private customfonts.MyEditText pPassword;
    private customfonts.MyEditText pConfirm_password;
    private customfonts.MyTextView pSign_up_button;
    AlertDialog.Builder builder ;
    String email,phone,address,password,cnfpassword;
    String reg_url = "http://192.168.1.101:802/poller/register.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        bindViews();
    }
    private void bindViews() {

        pOnaddi = (ImageView) findViewById(R.id.onaddi);
        pEmail_id = (customfonts.MyEditText) findViewById(R.id.email_id);
        pPhone_no = (customfonts.MyEditText) findViewById(R.id.phone_no);
        pAddress = (customfonts.MyEditText) findViewById(R.id.address);
        pPassword = (customfonts.MyEditText) findViewById(R.id.password);
        pConfirm_password = (customfonts.MyEditText) findViewById(R.id.confirm_password);
        pSign_up_button = (customfonts.MyTextView) findViewById(R.id.sign_up_button);
        builder = new AlertDialog.Builder(Registration.this);
        pSign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = pEmail_id.getText().toString();
                phone = pPhone_no.getText().toString();
                address = pAddress.getText().toString();
                password = pPassword.getText().toString();
                cnfpassword = pConfirm_password.getText().toString();

                if(email.equals("")||phone.equals("")||address.equals("")||password.equals("")||cnfpassword.equals(""))
                {
                    builder.setTitle("Something went wrong");
                    builder.setMessage("You must fill all fields");
                    displayAlert("input_error");
                }
                else
                {
                    if(!(password.equals(cnfpassword)))
                    {
                        builder.setTitle("Something went wrong");
                        builder.setMessage("Your passwords not matching");
                        displayAlert("input_error");
                    }
                    else
                    {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    String code1 = jsonObject.getString("code");
                                    String message1 = jsonObject.getString("message");
                                    builder.setTitle("SErver Response ...");
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
                                Map<String,String>params = new HashMap<String, String>();
                                params.put("email",email);
                                params.put("phone",phone);
                                params.put("address",address);
                                params.put("password",password);
                                return params;
                            }
                        };
                        MySingleton.getInstance(Registration.this).addToRequestQueue(stringRequest);
                    }
                }

            }
        });
    }
    public void displayAlert(final String code )
    {
        builder.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(code.equals("input_error"))
                {
                    pPassword.setText("");
                    pConfirm_password.setText("");
                }
                else if(code.equals("reg_success"))
                {
                    finish();
                }
                else if (code.equals("reg_failed"))
                {
                    pEmail_id.setText("");
                    pAddress.setText("");
                    pPhone_no.setText("");
                    pPassword.setText("");
                    pConfirm_password.setText("");
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
