package com.infinium.rajukoushik.traveldiaries;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rajukoushik on 11/11/16.
 */
public class ParseJSON {
    static ArrayList<String> diariesList;

    public static final String JSON_ARRAY = "diaries";
   static DiaryListManager diaryListManager;


    private JSONArray diaries = null;

    private String json;
    private DiaryListManager dlm;
    public ParseJSON(String json , DiaryListManager dlm){
        this.json = json;
        this.dlm=dlm;
    }

    protected void parseJSON(){
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

            dlm.setDiariesList(diariesList);






        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
