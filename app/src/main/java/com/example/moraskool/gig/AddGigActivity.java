package com.example.moraskool.gig;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moraskool.gig.Model.Dessert;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by moraskool on 29/06/2017.
 */

public class AddGigActivity  extends AppCompatActivity {

   private ImageButton saveBtn;
    private EditText gigName, gigDescrip, gigAmount;
     DatabaseReference mDatabaseGig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gig);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // change the color of the toolbar title (It was black before)
        ab.setTitle(Html.fromHtml("<font color='#FFFFFF'>Add Gig </font>"));
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        gigName = (EditText)findViewById(R.id.gig_name);
        gigDescrip = (EditText)findViewById(R.id.gig_description);

        gigAmount = (EditText) findViewById(R.id.gig_amnt);
        gigAmount.addTextChangedListener(onTextChangedListener());

        saveBtn = (ImageButton) findViewById(R.id.mybtn_add);
        mDatabaseGig = FirebaseDatabase.getInstance().getReference("Gig Posts");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AddGig();
            }
        });

    }

    // real-time adding to the firebase database
    private void AddGig(){
        String name = gigName.getText().toString().trim();
        String descrip = gigDescrip.getText().toString().trim();
        String amount = gigAmount.getText().toString().trim();

        if((!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(descrip) && (!TextUtils.isEmpty(amount))) ){

            String id =  mDatabaseGig.push().getKey();
            Dessert dessert = new Dessert( name, descrip, amount);
            mDatabaseGig.child(id).setValue(dessert);
            Toast.makeText(this, "Posted! (^_^)",Toast.LENGTH_LONG).show();
            finish();

            //Todo make a toast for gig added here
        // you can still sprlit these to check for each text field
        }else{
            Toast.makeText(this, "One or more field(s) missing!",Toast.LENGTH_LONG).show();
        }
    }

    // method to Auto-format the amount editText to 2 decimal places
    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                gigAmount.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(".")) {
                        originalString = originalString.replaceAll(".", " ");
                    }
                    longval = Long.parseLong(originalString);


                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);//(new Locale("en", "IN"))
                    formatter.applyPattern("########.0#");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    gigAmount.setText(formattedString);
                    gigAmount.setSelection(gigAmount.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                gigAmount.addTextChangedListener(this);
            }
        };
    }
}

