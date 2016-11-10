package com.infinium.rajukoushik.traveldiaries;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;


public class AddPost extends AppCompatActivity {

    public static final String JSON_URL = "http://";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        sendRequest();
        AutoCompleteTextView autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.list_of_diaries);
        autoCompleteTextView1.setAdapter(getEmailAddressAdapter(this));



    }

    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        DiaryListManager dlm = new DiaryListManager();
        ArrayList<String> diariesList = dlm.getDiariesList();


        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, diariesList);
    }

    private void sendRequest(){

        StringRequest stringRequest = new StringRequest(JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showJSON(response);

                        ParseJSON pj = new ParseJSON(response);
                        pj.parseJSON();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPost.this,error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
