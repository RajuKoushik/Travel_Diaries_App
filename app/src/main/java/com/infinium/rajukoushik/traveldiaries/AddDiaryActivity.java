package com.infinium.rajukoushik.traveldiaries;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

public class AddDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary);

        AutoCompleteTextView autoCompleteTextView1 = (AutoCompleteTextView) findViewById(R.id.list_of_diaries);
        autoCompleteTextView1.setAdapter(getEmailAddressAdapter(this));
    }

    private ArrayAdapter<String> getEmailAddressAdapter(Context context) {
        DiaryListManager dlm = new DiaryListManager();
        ArrayList<String> diariesList = dlm.getDiariesList();


        return new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, diariesList);
    }
}
