package com.example.moraskool.gig;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by moraskool on 30/12/2017.
 */

public class ViewGigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gig);
        Bundle extras = getIntent().getExtras();

    }
}
