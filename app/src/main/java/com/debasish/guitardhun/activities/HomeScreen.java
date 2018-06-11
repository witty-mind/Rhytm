package com.debasish.guitardhun.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import android.widget.RelativeLayout;
import android.widget.Button;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.adapters.ItemAdapter;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.debasish.guitardhun.models.UserModel;
import com.debasish.guitardhun.utils.LoaderUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeScreen extends AppCompatActivity implements ItemAdapter.ItemClickListener {

    private TextView mTextMessage;
    ActionBar ab;
    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    ArrayList<GuitarDetailsModel> guitarDetailsModels;
    @BindView(R.id.card_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.recycler_view_wishList) RecyclerView recycler_view_wishList;
    ItemAdapter itemAdapter;
    MenuItem shoppingCart, search;
    public static ArrayList<String> favoriteArray = new ArrayList<>();
    public static String userId = null, productId = null;
    String email, fullName;
    @BindView(R.id.layProfile) RelativeLayout layProfile;
    @BindView(R.id.tvFullName) TextView tvFullName;
    @BindView(R.id.tvEmailValue) TextView tvEmail;
    @BindView(R.id.tvUserId) TextView tvUserId;
    @BindView(R.id.btnLogout) Button btnLogout;

    @OnClick (R.id.btnLogout) void logOut(){
        logout();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //ab.setTitle("Home");
                    handleViewVisibility(1);
                    hideMenuItems(false);
                    toolbar.setTitle(R.string.title_activity_home_screen);
                    fetchGuitarDetails();
                    return true;
                /*case R.id.navigation_wishlist:
                   // ab.setTitle("DashBoard");
                    handleViewVisibility(2);
                    hideMenuItems(false);
                    toolbar.setTitle("My WishList");
                    fetchUserWishList();
                    return true;*/
                case R.id.navigation_notifications:
                    //ab.setTitle("Profile");
                    handleViewVisibility(3);
                    loadUserProfile();
                    toolbar.setTitle(R.string.title_profile);
                    hideMenuItems(true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ButterKnife.bind(this);
        // setting up action bar
        setSupportActionBar(toolbar);

        // Refreshing the views
        handleViewVisibility(1);

        // Setting the toolbar title
        toolbar.setTitle("Home");

        // Setting up Bottom nav bar
        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // initializing the Recycler view
        initViews();

        // Getting user Info
        getUserInfo();

        // Fetching the User Favorites
        fetchUserFavorites();
    }

    // HIding the search menu
    public void hideMenuItems(boolean status){
        if(status){
            search.setVisible(false);
        }else{
            search.setVisible(true);
        }
    }

    public void handleViewVisibility(int statusValue){
        if(statusValue == 1){
            recycler_view_wishList.setVisibility(RecyclerView.GONE);
            mRecyclerView.setVisibility(RecyclerView.VISIBLE);
            layProfile.setVisibility(View.GONE);
        }else if(statusValue == 2){
            mRecyclerView.setVisibility(RecyclerView.GONE);
            layProfile.setVisibility(View.GONE);
            recycler_view_wishList.setVisibility(RecyclerView.VISIBLE);
        }else if(statusValue == 3){
            layProfile.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(RecyclerView.GONE);
            recycler_view_wishList.setVisibility(RecyclerView.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        search = menu.findItem(R.id.search);
        shoppingCart = menu.findItem(R.id.cart);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        shoppingCart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(HomeScreen.this, CartScreen.class));
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                itemAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    // Initializing the views
    private void initViews(){
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

    }

    // Setting up the adapter
    public void setUpAdapter(){
        itemAdapter = new ItemAdapter(HomeScreen.this, guitarDetailsModels);
        mRecyclerView.setAdapter(itemAdapter);
        itemAdapter.setClickListener(this);
    }

    // Setting up the adapter
    public void setUpAdapterWishList(){
        itemAdapter = new ItemAdapter(HomeScreen.this, guitarDetailsModels);
        recycler_view_wishList.setAdapter(itemAdapter);
    }

    public void getUserInfo(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        userId = pref.getString("userId", null);
        email = pref.getString("userEmail", null);
        fullName = pref.getString("userFullName", null);
    }

    public static ArrayList<String> convertToList(String favoriteValue){
        ArrayList<String> favoriteArray = null;
        if(favoriteValue.contains(",")){
            favoriteValue.replace("]%*[", "");
            favoriteArray =
                    new ArrayList<String>(Arrays.asList(favoriteValue.split(",")));
        }else{
            favoriteArray = new ArrayList<>();
            favoriteArray.add(favoriteValue);
        }
        Log.d("Converted Value", favoriteArray.toString());
        return favoriteArray;
    }


    // Function responsible fro fetching the guitar details
    public void fetchUserFavorites(){
        LoaderUtils.showProgressBar(HomeScreen.this, "Please wait while fetching the details..");
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference("users").child(userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserModel value = dataSnapshot.getValue(UserModel.class);
                favoriteArray = value.getFavorites();

                if(favoriteArray == null){
                    favoriteArray = new ArrayList<>();
                }
                fetchGuitarDetails();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                //LoaderUtils.dismissProgress();
                fetchGuitarDetails();
                // Failed to read value
                Log.w("HomeScreen", "Failed to read value.", error.toException());
            }
        });
    }

    // Function responsible fro fetching the guitar details
    public void fetchUserWishList(){
        LoaderUtils.showProgressBar(HomeScreen.this, "Please wait while fetching the details..");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("userwishlist").child(userId);
        guitarDetailsModels = new ArrayList<>();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GuitarDetailsModel value = dataSnapshot.getValue(GuitarDetailsModel.class);
                Log.d("HomeScreen", "Value is: " + value.toString());
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    GuitarDetailsModel guitars = noteDataSnapshot.getValue(GuitarDetailsModel.class);
                    guitarDetailsModels.add(guitars);
                }

                setUpAdapterWishList();
                LoaderUtils.dismissProgress();
                Log.d("HomeScreen", "Value is: " + guitarDetailsModels);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                LoaderUtils.dismissProgress();
                // Failed to read value
                Log.w("HomeScreen", "Failed to read value.", error.toException());
            }
        });
    }

    // Function responsible fro fetching the guitar details
    public void fetchGuitarDetails(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("guitars");
        guitarDetailsModels = new ArrayList<>();
        setUpAdapter();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                GuitarDetailsModel value = dataSnapshot.getValue(GuitarDetailsModel.class);
                Log.d("HomeScreen", "Value is: " + value.toString());
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    GuitarDetailsModel guitars = noteDataSnapshot.getValue(GuitarDetailsModel.class);
                    guitarDetailsModels.add(guitars);
                    // Setting the adapter
                    itemAdapter.notifyDataSetChanged();
                }
                LoaderUtils.dismissProgress();
                Log.d("HomeScreen", "Value is: " + guitarDetailsModels);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                LoaderUtils.dismissProgress();
                // Failed to read value
                Log.w("HomeScreen", "Failed to read value.", error.toException());
            }
        });
    }

    // Function responsible for loading the suer profile Loading the User Profile
    public void loadUserProfile(){
        tvEmail.setText(email);
        tvFullName.setText(fullName);
        tvUserId.setText(userId);
    }

    // Function responsible for making the user logout
    public void logout(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        Toast.makeText(this, "Successfully Logged Out", Toast.LENGTH_SHORT).show();
        HomeScreen.this.finish();
        startActivity(new Intent(HomeScreen.this, LoginScreen.class));
    }

    @Override
    public void onClick(View v, int position) {
        final GuitarDetailsModel guitar = itemAdapter.mFilteredList.get(position);
        Intent i = new Intent(this, DetailsScreen.class);
        productId = guitar.getModelNo();
        startActivity(i);
    }
}
