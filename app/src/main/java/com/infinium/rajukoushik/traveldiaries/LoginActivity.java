package com.infinium.rajukoushik.traveldiaries;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    public static final String LOGIN_URL = "http://192.168.43.137:8000/td/login/";
    public String UserName = "";
    public static final String KEY_USERNAME="username";
    public static final String KEY_PASSWORD="password";
    public String[] posttext=new String[1000];
    public String[] postitle=new String[1000];
    public String[] username=new String[1000];
    public String message;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    private String user_name;
    private String password;
    public int code;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = (EditText) findViewById(R.id.uIDBox);
        editTextPassword = (EditText) findViewById(R.id.passBox);


    }

    public void clickRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    public void btnLogin(View view) {

        userLogin();

    }

    private void userLogin() {
        user_name = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        UserName = user_name;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jobj = new JSONObject(response);

                            token = jobj.getString("token");

                            PrefManger prefManger = new PrefManger(LoginActivity.this);
                            prefManger.setToken(token);
                            //Intent intent = new Intent(LoginActivity.this, IntroActivity.class);
                            //startActivity(intent);


                            Toast.makeText(LoginActivity.this,message,Toast.LENGTH_LONG).show();
                            Log.e("tag", response);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_LONG ).show();


                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put(KEY_USERNAME,user_name);
                map.put(KEY_PASSWORD,password);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    public void btnNotNow(View view) {

        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);

    }
}
