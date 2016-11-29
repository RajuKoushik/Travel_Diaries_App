package com.infinium.rajukoushik.traveldiaries;

/**
 * Created by rajukoushik on 17/10/16.
 */
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WallRecyclerViewAdapter extends RecyclerView
        .Adapter<WallRecyclerViewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "WallRecyclerViewAdapter";
    public static final String JSON_URL_USERNAME = "http://192.168.43.178:8000/td/get/user_profile/";
    private static String token;
    private static String KEY_TOKEN = "token";
    private static String first_name;
    private static String last_name;
    static Context context;
    private ArrayList<PostObject> mDataset;
    private ArrayList<String> picList;
    private static MyClickListener myClickListener;
    static String username;

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView username_post;
        TextView post_title;
        TextView post_text;
        ImageView post_image;


        public DataObjectHolder(View itemView) {
            super(itemView);
            username_post = (TextView) itemView.findViewById(R.id.username_post);
            post_title = (TextView) itemView.findViewById(R.id.post_title);
            post_text =(TextView) itemView.findViewById(R.id.post_text);
            post_image = (ImageView)itemView.findViewById(R.id.imgView1);

            username_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String temp = (String) username_post.getText();
                    sendUserProfileRequest(temp);
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("username",username);
                    intent.putExtra("first_name",first_name);
                    intent.putExtra("last_name",last_name);
                    context.startActivity(intent);
                }
            });

            //username_post.setOnClickListener();
            Log.i(LOG_TAG, "Adding Listener");
            //itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public WallRecyclerViewAdapter(ArrayList<PostObject> myDataset, Context context) {
        mDataset = myDataset;
        this.context = context;
    }

    public static void sendUserProfileRequest(final String name)
    {
        PrefManger pf = new PrefManger(context);
        token = pf.getToken();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_USERNAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jobj = new JSONObject(response);

                            username = jobj.getString("username");
                            first_name = jobj.getString("first_name");
                            last_name = jobj.getString("last_name");
                            Log.e("teststts",username);

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
                params.put("name",name);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);



        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        holder.username_post.setText(mDataset.get(position).getmText1());
        holder.post_title.setText(mDataset.get(position).getmText2());
        holder.post_text.setText(mDataset.get(position).getmText3());


        String url = mDataset.get(position).getmText4();

        Picasso.with(context).load(url).fit().error(R.drawable.ic_history_black_24dp).placeholder(R.drawable.progress_animation).into(holder.post_image);

        //WallPicassoImageLoadTask imageLoadTask = new WallPicassoImageLoadTask(url,holder.post_image,context);



        //imageLoadTask.execute();




       // new ImageLoadTask(url, holder.post_image).execute();



    }

    public PostObject getDataObject(int i)
    {
        return mDataset.get(i);
    }

    public void addItem(PostObject dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}