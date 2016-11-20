package com.infinium.rajukoushik.traveldiaries;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddDiaryActivity extends AppCompatActivity {

    public static final String ADDPOST_URL = "http://192.168.43.178:8000/td/post/post_diary/";
    String status;
    String tempToken;

    private EditText editTextDiaryName;
    private EditText editTextDiaryBrief;


    AutoCompleteTextView autoCompleteTextView1;




    public static final String KEY_DIARYBRIEF = "diary_brief";
    public static final String KEY_DIARYNAME = "diary_name";
    public static final String KEY_TOKEN = "token";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);



        editTextDiaryName = (EditText) findViewById(R.id.diaryName);
        editTextDiaryBrief = (EditText) findViewById(R.id.diaryBrief);
    }



    public void addDIaryBtn(View view) {

        addDiary();
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }

    private void addDiary() {

        final String diary_name = editTextDiaryName.getText().toString().trim();
        final String diary_brief= editTextDiaryBrief.getText().toString().trim();


        final PrefManger pm = new PrefManger(this);
        Log.e("tag", pm.getToken());

        tempToken  = pm.getToken();





        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDPOST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            status = jobj.getString("status");

                            Toast.makeText(AddDiaryActivity.this,status.toString(), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddDiaryActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,tempToken);
                params.put(KEY_DIARYNAME,diary_name);
                params.put(KEY_DIARYBRIEF,diary_brief);




                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
