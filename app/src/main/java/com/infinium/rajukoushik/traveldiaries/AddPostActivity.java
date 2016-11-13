package com.infinium.rajukoushik.traveldiaries;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
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


public class AddPostActivity extends AppCompatActivity {

    public static final String JSON_URL = "http://192.168.43.178:8000/td/get/all_diaries/";

    public static final String ADDPOST_URL = "http://192.168.43.178:8000/td/post/experience/";
    String status;

    private EditText editTextPostTitle;
    private EditText editTextPostText;
    private TextView name;
    private EditText editTextFirstName;
    private EditText editTextLastName;

    AutoCompleteTextView autoCompleteTextView1;

    private AutoCompleteTextView editTextDiaryName;

    public static final String KEY_POSTTITLE = "post_title";
    public static final String KEY_POSTTEXT = "post_text";
    public static final String KEY_DIARYNAME = "diary_name";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        sendRequest();
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.list_of_diaries);
        autoCompleteTextView1.setAdapter(getEmailAddressAdapter(this));

        TextView Username = (TextView) findViewById(R.id.Username);
        UserDetailsManager udm = new UserDetailsManager();
        Toast.makeText(AddPostActivity.this,udm.getUsername(), Toast.LENGTH_LONG).show();

        Username.setText(udm.getUsername());

        editTextPostTitle = (EditText) findViewById(R.id.postTilte);
        editTextPostText = (EditText) findViewById(R.id.postText);
        DiaryListManager dlm = new DiaryListManager();
        //Log.e("testing",dlm.getDiariesList().get(0));





    }



    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        DiaryListManager dlm = new DiaryListManager();
        ArrayList<String> diariesList = dlm.getDiariesList();


        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, diariesList);
    }

    private void sendRequest(){

        final PrefManger pm = new PrefManger(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL,
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
                        Toast.makeText(AddPostActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",pm.getToken());



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void addPostBtn(View view) {

        addPOST();


    }

    private void addPOST() {

        final String post_title = editTextPostTitle.getText().toString().trim();
        final String post_text= editTextPostText.getText().toString().trim();
        final String diary_name = autoCompleteTextView1.getText().toString().trim();


        final PrefManger pm = new PrefManger(this);



        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADDPOST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jobj = new JSONObject(response);
                            status = jobj.getString("status");

                            Toast.makeText(AddPostActivity.this,status.toString(), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddPostActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",pm.getToken());
                params.put(KEY_POSTTITLE,post_title);
                params.put(KEY_POSTTEXT,post_text);
                params.put(KEY_DIARYNAME, diary_name);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
