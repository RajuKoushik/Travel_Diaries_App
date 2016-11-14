package com.infinium.rajukoushik.traveldiaries;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        String username = getIntent().getStringExtra("username");
        String first_name = getIntent().getStringExtra("username");
        TextView email = (TextView) findViewById(R.id.email);
        email.setText(username+"@traveldiaries.com");

        TextView bio = (TextView)findViewById(R.id.shortbio);
        bio.setText(username);



    }

    public void followBtn(View view) {

    }
}
