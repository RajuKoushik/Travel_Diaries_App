package com.infinium.rajukoushik.traveldiaries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by rajukoushik on 11/11/16.
 */
public class ParseJSON {
    ArrayList<String> diariesList;

    public static final String JSON_ARRAY = "diaries";


    private JSONArray diaries = null;

    private String json;

    public ParseJSON(String json){
        this.json = json;
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

            DiaryListManager diaryListManager = new DiaryListManager();
            diaryListManager.setDiariesList(diariesList);






        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
