package com.infinium.rajukoushik.traveldiaries;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class ProfileActivity extends AppCompatActivity {
    private static String LOG_TAG = "WallRecyclerViewAdapter";
    public static final String JSON_URL_USERNAME = "http://192.168.43.178:8000/td/follow/";
    private static String token;
    private static String KEY_TOKEN = "token";
    private static String first_name;
    private static String last_name;
    static Context context;
    private ArrayList<PostObject> mDataset;
    private static WallRecyclerViewAdapter.MyClickListener myClickListener;
    static String username;
    private String status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        username = getIntent().getStringExtra("username");
        String first_name = getIntent().getStringExtra("username");
        TextView email = (TextView) findViewById(R.id.email);
        email.setText(username+"@traveldiaries.com");

        TextView bio = (TextView)findViewById(R.id.shortbio);
        bio.setText(username);



    }

    public void followBtn(View view) {
        PrefManger pf = new PrefManger(this);
        token = pf.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_USERNAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jobj = new JSONObject(response);

                            status = jobj.getString("status");
                            Log.e("teststts",response);

                            //Toast.makeText(IntroActivity.this,username.toString(), Toast.LENGTH_LONG).show();

                            //UserDetailsManager userDetailsManager = new UserDetailsManager();
                            //userDetailsManager.setUsername(username);

                            //Toast.makeText(IntroActivity.this,userDetailsManager.getUsername(), Toast.LENGTH_LONG).show();

                            // userDetailsManager.setFirstname(jobj.getString("firstname"));
                            //userDetailsManager.setLastname(jobj.getString("lastname"));

                            //uncomment after image integration
                            //picUrl = jobj.getString("picUrl");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(IntroActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);
                params.put("followee_name",username);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
