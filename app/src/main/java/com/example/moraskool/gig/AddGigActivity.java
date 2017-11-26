package com.example.moraskool.gig;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moraskool.gig.Model.Dessert;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by moraskool on 29/06/2017.
 */

public class AddGigActivity  extends AppCompatActivity {

    private static String TAG = "AddGigActivity";
    private ImageButton saveBtn;
    private EditText gigName, gigDescrip, gigAmount;
    private String userID;


    //Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference myRef;


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
        saveBtn = (ImageButton) findViewById(R.id.mybtn_add);

        //gigAmount.addTextChangedListener(onTextChangedListener());


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef =  FirebaseDatabase.getInstance().getReference();

        //mMovieRef = mRef.child("Name");
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGig();
            }
        });

    }


    // real-time adding to the firebase database
    private void AddGig(){
        String name = gigName.getText().toString();
        String descrip = gigDescrip.getText().toString();
        String amount = gigAmount.getText().toString();

        if((!TextUtils.isEmpty(name))&&(!TextUtils.isEmpty(descrip) && (!TextUtils.isEmpty(amount))) ){

            FirebaseUser user = mAuth.getCurrentUser();
            userID = user.getUid();
            String id =  myRef.push().getKey();
            Dessert dessert = new Dessert( name, descrip, amount);
           // myRef.child(id).setValue(dessert);
            myRef.child("users").child(userID).child("Gig posts").child(id).setValue(dessert);
            Toast.makeText(this, "Posted! (^_^)",Toast.LENGTH_LONG).show();
            finish();

        // you can still sprlit these to check for each text field
        }else{
            Toast.makeText(this, "One or more field(s) missing!",Toast.LENGTH_LONG).show();
        }
    }

    // method to Auto-format the amount editText to 2 decimal places

    /*
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
                        originalString = originalString.replaceAll(".", ".");
                    }
                    longval = Long.parseLong(originalString);


                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);//(new Locale("en", "IN"))
                    formatter.applyPattern("########.##");
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
    */
}

