package com.infinium.rajukoushik.traveldiaries;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DiaryWallActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String token;
    private String JSON_URL_WALLPOSTS = "http://192.168.43.178:8000/td/get/diary_posts/";
    private static ArrayList<PostObject> postList = new ArrayList<>();
    private static ArrayList<String> titlesList = new ArrayList<>();

    private JSONArray titles;
    private JSONArray texts;
    private ArrayList<String> textsList;
    private JSONArray names;
    private JSONArray picUrls;
    private ArrayList<String> namesList;
    private ArrayList<String> picList;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_wall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getWallPosts();

        value= getIntent().getStringExtra("diary_name");
       // Log.e("cghds",value);

        TextView diaryNamedisp = (TextView)findViewById(R.id.diaryNameDisp);
        diaryNamedisp.setText(value + " Diaries");




        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

    }

    private void pushDataToAdapter(ArrayList<PostObject> postList) {

        mAdapter = new DiaryWallRecyclerViewAdapter(postList,this);
        mRecyclerView.setAdapter(mAdapter);
        //end of card
    }

    private void getWallPosts() {

        PrefManger pf = new PrefManger(DiaryWallActivity.this);
        token = pf.getToken();





        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_WALLPOSTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jobj = new JSONObject(response);

                            jobj = new JSONObject(response);
                            titles = jobj.getJSONArray("post_titles");


                            titlesList = new ArrayList<String>();
                            JSONArray jsonArray = (JSONArray)titles;
                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                for (int i=0;i<len;i++){

                                    titlesList.add(jsonArray.get(i).toString());
                                }
                            }

                           // Log.e("test",titlesList.get(0));


                            //dfg

                            texts = jobj.getJSONArray("post_usernames");


                            textsList = new ArrayList<String>();
                            JSONArray jsonArray1 = (JSONArray)texts;
                            if (jsonArray1 != null) {
                                int len = jsonArray1.length();
                                for (int i=0;i<len;i++){
                                    textsList.add(jsonArray1.get(i).toString());
                                }
                            }

                            //Log.e("test",textsList.get(0));
                            //
                            names = jobj.getJSONArray("post_texts");


                            namesList = new ArrayList<String>();
                            JSONArray jsonArray2 = (JSONArray)names;
                            if (jsonArray2 != null) {
                                int len = jsonArray2.length();
                                for (int i=0;i<len;i++){
                                    namesList.add(jsonArray2.get(i).toString());
                                }
                            }
                            picUrls = jobj.getJSONArray("post_pic_url");

                            picList = new ArrayList<String>();
                            JSONArray jsonArray3 = (JSONArray)picUrls;
                            if(jsonArray3 != null){
                                int len = jsonArray3.length();
                                for(int i =0;i<len;i++){
                                    picList.add(jsonArray3.get(i).toString());
                                }
                            }


                            //Log.e("test",namesList.get(0));

                            if(namesList.size() == 0)
                            {
                                postList.add(0,new PostObject(0,"Default Post","Default Post","Default Post","http://sm.uploads.im/0ptiu.jpg"));
                            }

                            else {


                                for (int i = 0; i < namesList.size(); i++) {


                                    postList.add(i, new PostObject(i, textsList.get(i), titlesList.get(i), namesList.get(i), picList.get(i)));


                                }
                            }

                            pushDataToAdapter(postList);


                            //Toast.makeText(IntroActivity.this,response.toString(), Toast.LENGTH_LONG).show();



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
                        Toast.makeText(DiaryWallActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("token",token);

                params.put("diary_name",value);


                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

}
