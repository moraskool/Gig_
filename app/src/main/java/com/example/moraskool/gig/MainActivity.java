package com.example.moraskool.gig;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.moraskool.gig.Adapter.DessertAdapter;
import com.example.moraskool.gig.Model.Dessert;
import com.example.moraskool.gig.Model.Users;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/*
 * Created by moraskool on 16/05/2017.
 */

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Context mContext = this;

    private static final String TAG = MainActivity.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    private FirebaseDatabase mfirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference UsersRef;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseAuth mAuth;
    public String personName, personMail, uid;
    ImageButton fabBtn;
    View fabShadow;
    Toolbar toolbar;

    @Override
    public void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(firebaseAuthListener);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabs_header);

        // create an object of sharedPreferenceManager and get stored user data
        //sharedPrefManager = new SharedPrefManager(mContext);
        //personName = sharedPrefManager.getName();
        //personMail = sharedPrefManager.getUserEmail();

        configureSignIn();

        // Initialize FirebaseAuth


        mFirebaseAuth = FirebaseAuth.getInstance();


        // handle when there is a user to get the user profile
        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth mFirebaseAuth) {
                // Check if user is signed in (non-null) and update UI accordingly.
                FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // UID specific to the provider e.g google.com
                    uid = currentUser.getUid();
                    personName = currentUser.getDisplayName();
                     if (getSupportActionBar() != null)
                         getSupportActionBar().setTitle("Welcome, " + personName + "!");

                    Log.d(TAG,"onAuthStateChanged:signed_in:"+currentUser.getUid());

                } else {
                   goSignIn();
                }
            }
        };



        final Toolbar toolbar = (Toolbar) findViewById(R.id.htab_toolbar);
        setSupportActionBar(toolbar);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.htab_viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.htab_tabs);
        tabLayout.setupWithViewPager(viewPager);

        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.htab_collapse_toolbar);

        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.header);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {

                    int vibrantColor = palette.getVibrantColor(R.color.primary_500);
                    int vibrantDarkColor = palette.getDarkVibrantColor(R.color.primary_700);
                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);
                    collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            Log.e(TAG, "onCreate: failed to create bitmap from background", e.fillInStackTrace());
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.primary_500)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.primary_700)
            );
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
                Log.d(TAG, "onTabSelected: pos: " + tab.getPosition());

                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fabBtn = (ImageButton) findViewById(R.id.myfab_main_btn);
        fabShadow = findViewById(R.id.myfab_shadow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            fabShadow.setVisibility(View.GONE);
            fabBtn.setBackground(getDrawable(R.drawable.ripple_accent));
        }

        fabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 31/07/17 Add circular reveal
                Intent intent = new Intent(MainActivity.this, AddGigActivity.class);
                startActivity(intent);

            }
        });

    }

    //This method creates a new user on our own Firebase database
    //after a successful Authentication on Firebase
    //It also saves the user info to SharedPreference

    public void configureSignIn(){
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        // TODO : Create another "FeedsFragment" class for the each tabs,

        adapter.addFrag(new FeedsFragment(
                ContextCompat.getColor(this, R.color.amber_50)), "Gig Feeds");
        adapter.addFrag(new FeedsFragment(
                ContextCompat.getColor(this, R.color.purple_50)), "Your Gigs");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_navigator, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                showSettings();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSettings(){}

    public void goSignIn() {
        Intent intent = new Intent(this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(){

        FirebaseAuth.getInstance().signOut();

    }

    // handles the scrolling of each tab
    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        private void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    // handles each tab content, fill up later with dynamic data
    public static class FeedsFragment extends Fragment {
        int color;

        public FeedsFragment() {
        }

        @SuppressLint("ValidFragment")
        public FeedsFragment(int color) {
            this.color = color;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dummy_fragment, container, false);
            final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.dummyfrag_scrollableview);
            final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.dummyfrag_bg);
            frameLayout.setBackgroundColor(color);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);

            final String curtUserId;
            DatabaseReference mDatabaseGig;
            FirebaseAuth mFirebaseAuth;
            FirebaseDatabase mFirebaseDatabase;

            mFirebaseAuth= FirebaseAuth.getInstance();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mDatabaseGig = mFirebaseDatabase.getReference();
            FirebaseUser currUser = mFirebaseAuth.getCurrentUser();
            curtUserId = currUser.getUid();
            final String id = mDatabaseGig.push().getKey();
            final List<Dessert> dessertList;
            // get the gig database
            //mDatabaseGig = FirebaseDatabase.getInstance().getReferenceFromUrl();

            dessertList = new ArrayList<>();
           final DessertAdapter adapter = new DessertAdapter(getContext(), dessertList);
            recyclerView.setAdapter(adapter);

            mDatabaseGig.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    dessertList.clear();
                    for(DataSnapshot gigSnapshot: dataSnapshot.getChildren()){
                        Users user = new Users();
                        //user.setDessert(gigSnapshot.child("users").child.(curtUserId).getValue(Users.class).getDessert());
                        Dessert dessert = dataSnapshot
                                //.child("users")
                                //.child(curtUserId)
                                .child("Gig posts")
                               // .child(id)
                                .getValue(Dessert.class);
                        dessertList.add(dessert);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            // possible to put progress dialogue
            return view;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(){
        super.onStop();

        if(firebaseAuthListener != null){
            mFirebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }


   /* Todo : Implement to check if there is data
    private void checkIfEmpty(){
        if(Dessert.size == 0){
            recylerview.setVisibility(View.INVISIBLE);
            emptyText.setVisibility(View.VISIBLE);
        }else{
            recylerview.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.INVISIBLE);
        }
    }
   */
}

