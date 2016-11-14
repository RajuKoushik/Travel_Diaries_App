package com.infinium.rajukoushik.traveldiaries;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IntroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String JSON_URL_USERNAME = "http://192.168.43.178:8000/td/get/user_details/";
    public static final String JSON_URL_WALLPOSTS = "http://192.168.43.178:8000/td/get/wall_posts/";
    String username;
    static ArrayList<String> diariesList;
    AutoCompleteTextView autoCompleteTextView1;
    public static final String JSON_URL = "http://192.168.43.178:8000/td/get/all_diaries/";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";
    private static ArrayList<PostObject> dataset = new ArrayList<>();
    private static ArrayList<PostObject> postList = new ArrayList<>();
    private static ArrayList<String> titlesList = new ArrayList<>();

    private DiaryListManager dlm;


    String picUrl;
    private JSONArray diaries = null;

    String token;
    public static final String KEY_TOKEN = "token";
    public static final String JSON_ARRAY = "post_usernames";
    private JSONArray titles;
    private JSONArray texts;
    private ArrayList<String> textsList;
    private JSONArray names;
    private ArrayList<String> namesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final Intent intent = new Intent(this, AddDiaryActivity.class);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add Diary", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                startActivity(intent);



            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getUsernameAndProfilePic();

        getWallPosts();
        sendRequest();
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.list_of_diaries);




        //card
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



    }


    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {




        //ArrayList<String> diariesList = dlm.getDiariesList();

        Log.e("testtt",diariesList.get(0));
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, diariesList);
    }

    protected void parseJSON(String json){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            diaries = jsonObject.getJSONArray("diaries");





            diariesList = new ArrayList<String>();
            JSONArray jsonArray = (JSONArray)diaries;
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    diariesList.add(jsonArray.get(i).toString());
                }
            }
            Log.e("test",diariesList.get(0));

            autoCompleteTextView1.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, diariesList));



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(){

        final PrefManger pm = new PrefManger(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST,JSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //showJSON(response);

                        parseJSON(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IntroActivity.this,error.getMessage(), Toast.LENGTH_LONG).show();
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

    public void set_dataset(ArrayList<PostObject> a) {
        this.dataset = a;
    }

    private ArrayList<PostObject> getDataSet() {

        return this.dataset;
    }

    private void getWallPosts() {

        PrefManger pf = new PrefManger(IntroActivity.this);
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

                            Log.e("test",titlesList.get(0));


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

                            Log.e("test",textsList.get(0));
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

                            Log.e("test",namesList.get(0));


                            for(int i=0;i<namesList.size();i++){
                                postList.add(i,new PostObject(i,textsList.get(i),titlesList.get(i),namesList.get(i)));
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
                        Toast.makeText(IntroActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }

    private void pushDataToAdapter(ArrayList<PostObject> postList) {

        mAdapter = new WallRecyclerViewAdapter(postList,this);
        mRecyclerView.setAdapter(mAdapter);
        //end of card
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.intro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addPost(View view) {

        Intent intent = new Intent(this, AddPostActivity.class);

        startActivity(intent);




    }


    private void getUsernameAndProfilePic() {

        PrefManger pf = new PrefManger(IntroActivity.this);
        token = pf.getToken();



        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_USERNAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jobj = new JSONObject(response);

                            username = jobj.getString("username");
                            Toast.makeText(IntroActivity.this,username.toString(), Toast.LENGTH_LONG).show();

                            UserDetailsManager userDetailsManager = new UserDetailsManager();
                            userDetailsManager.setUsername(username);

                            Toast.makeText(IntroActivity.this,userDetailsManager.getUsername(), Toast.LENGTH_LONG).show();

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
                        Toast.makeText(IntroActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put(KEY_TOKEN,token);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
