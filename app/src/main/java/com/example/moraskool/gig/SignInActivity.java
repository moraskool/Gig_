package com.example.moraskool.gig;

/**
 * Created by moraskool on 19/05/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.moraskool.gig.Utils.SharedPrefManager;
import com.example.moraskool.gig.Utils.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
//import com.example.moraskool.gig.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    // private ActivitySignInBinding mBinding;
    private static final String TAG = SignInActivity.class.getSimpleName();

    String[] colors = {"#96CC7A", "#EA705D", "#66BBCC"};

    public static final String PREF_USER_FIRST_TIME = "user_first_time";
    boolean isUserFirstTime;

    // private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    private SignInButton mSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private ProgressBar progressBar;
    public String personName, personMail, uid;
    private String idToken;
    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;

    @Override
    public void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isUserFirstTime = Boolean.valueOf(Utils.readSharedSetting(SignInActivity.this, PREF_USER_FIRST_TIME, "true"));

        Intent introIntent = new Intent(SignInActivity.this, PagerActivity.class);
        introIntent.putExtra(PREF_USER_FIRST_TIME, isUserFirstTime);

        if (isUserFirstTime)
            startActivity(introIntent);

        setContentView(R.layout.activity_sign_in);

        // Assign fields
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this);

        configureSignIn();

        mFirebaseAuth = FirebaseAuth.getInstance();
        //this is where we start the Auth state Listener to listen for whether the user is signed in or not
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mFirebaseAuth) {
                // get signed in user
                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                //if user is signed in, we call a helper method to save the user details to Firebase
                if (currentUser != null) {
                    //go to main activity
                    goToMain();
                }
            }
        };

        progressBar = (ProgressBar) findViewById(R.id.progressbar);
    }

   /* private void createUserInFirebaseHelper(){

        //final String encodedEmail = Utils.encodeEmail(personMail);
        //create an object of Firebase database and pass the the Firebase URL
        final Firebase userLocation = new Firebase(Constants.FIREBASE_URL_USERS).child(personMail);

        //Add a Listerner to that above location
        userLocation.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    // Insert into Firebase database
                    Users user = new Users(personName, personMail, uid);
                    userLocation.setValue(user);
                    Toast.makeText(SignInActivity.this, "Account created!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

                Log.d(TAG, getString(R.string.log_error_occurred) + firebaseError.getMessage());
                //hideProgressDialog();
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN){
                }
                else {
                    Toast.makeText(SignInActivity.this, firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
*/
    public void configureSignIn(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(SignInActivity.this, "Oops! Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            default:
                return;
        }
    }

    // Start SignIn process
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
//                idToken = account.getIdToken();
//
//                // Save Data to SharedPreference
//                sharedPrefManager = new SharedPrefManager(mContext);
//                sharedPrefManager.saveIsLoggedIn(mContext, true);
//
//                sharedPrefManager.saveEmail(mContext, personMail);
//                sharedPrefManager.saveName(mContext, personName);
//                sharedPrefManager.saveToken(mContext, idToken);
                //sharedPrefManager.saveIsLoggedIn(mContext, true);

//              AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuthWithGoogle(account);
            } else {
                // Log message Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // Start authentication with Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        // TODO: Can implement another progress bar design here
        progressBar.setVisibility(View.VISIBLE);

        mSignInButton.setVisibility(View.GONE);

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        mSignInButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(SignInActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            //createUserInFirebaseHelper();
                            Toast.makeText(SignInActivity.this, "Login successful",
                                    Toast.LENGTH_SHORT).show();

                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            //finish();
                            // do stuff here. https://www.youtube.com/watch?v=cRC02_ERfbo
                        }

                    }
                });
    }


    public void goToMain(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStop(){
        super.onStop();

        if(firebaseAuthListener != null){
            mFirebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }
}


