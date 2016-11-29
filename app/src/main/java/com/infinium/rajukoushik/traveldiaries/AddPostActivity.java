package com.infinium.rajukoushik.traveldiaries;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    File f;

    AutoCompleteTextView autoCompleteTextView1;

    private AutoCompleteTextView editTextDiaryName;

    public static final String KEY_POSTTITLE = "post_title";
    public static final String KEY_POSTTEXT = "post_text";
    public static final String KEY_DIARYNAME = "diary_name";
    public static final String KEY_URL_PIC = "post_pic_url";

    static ArrayList<String> diariesList;

    public static final String JSON_ARRAY = "diaries";
    static DiaryListManager diaryListManager;


    private JSONArray diaries = null;

    private String json;
    private DiaryListManager dlm;
    private static String picture_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        sendRequest();
        autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.list_of_diaries);


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


        ArrayList<String> diariesList = dlm.getDiariesList();

        Log.e("testtt",diariesList.get(0));
        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, diariesList);
    }


    protected void parseJSON(String json){
        JSONObject jsonObject=null;
        try {
            jsonObject = new JSONObject(json);
            diaries = jsonObject.getJSONArray(JSON_ARRAY);





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
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);


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
                params.put(KEY_URL_PIC,picture_url);



                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void addPicBtn(View view) {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImage = data.getData();

            Picasso.with(this).load(selectedImage).noPlaceholder().centerCrop().fit()
                    .into((ImageView) findViewById(R.id.addPicBtn));

         /* try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
            }
            catch (IOException e) {
                e.printStackTrace();
            }*/
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            Log.d("picturepath",picturePath);
            cursor.close();

            // imageButton.setImageBitmap(BitmapFactory.decodeFile(picturePath));
          /*  Bitmap bitmap = decodeFile(new File(picturePath));
            imageButton.setImageBitmap(bitmap);*/


            f = new File(picturePath);





            if (f!=null){



                imagesend();

            }



        }



    }

    public void imagesend()
    {
        Ion.with(getBaseContext()).load("POST","http://uploads.im/api").uploadProgressHandler(new ProgressCallback()
        {
            @Override
            public void onProgress(long uploaded, long total)
            {
                System.out.println("uploaded " + (int)uploaded + " Total: "+total);
            }
        }).setMultipartParameter("platform", "android").setMultipartFile("image", f).asString().setCallback(new FutureCallback<String>()
        {
            @Override
            public void onCompleted(Exception e, String result)
            {
                // Log.d("imageresult",result);

                try {

                    JSONObject jsonObject = new JSONObject(result);

                    String data = jsonObject.getString("data");

                    JSONObject inerJson = new JSONObject(data);

                    picture_url = inerJson.getString("img_url");

                    Log.d("picture_url",picture_url);

                    //progressBar.setVisibility(View.GONE);
                }
                catch (JSONException ee)
                {
                    Log.d("JSONException",ee+"");
                }


            }
        });
    }



}