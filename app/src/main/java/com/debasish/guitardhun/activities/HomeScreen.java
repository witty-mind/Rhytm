package com.debasish.guitardhun.activities;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class HomeScreen extends AppCompatActivity {

    private TextView mTextMessage;
    ActionBar ab;
    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    ArrayList<GuitarDetailsModel> guitarDetailsModels;
    @BindView(R.id.card_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.recycler_view_wishList) RecyclerView recycler_view_wishList;
    ItemAdapter itemAdapter;
    MenuItem shoppingCart;
    public static ArrayList<String> favoriteArray = new ArrayList<>();
    public static String userId = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //ab.setTitle("Home");
                    handleViewVisibility(1);
                    fetchGuitarDetails();
                    return true;
                case R.id.navigation_wishlist:
                   // ab.setTitle("DashBoard");
                    handleViewVisibility(2);
                    fetchUserWishList();
                    return true;
                case R.id.navigation_notifications:
                    //ab.setTitle("Profile");
                    handleViewVisibility(3);
                    fetchUserWishList();
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

        setSupportActionBar(toolbar);

        handleViewVisibility(1);

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initViews();

        getUserInfo();

        fetchUserFavorites();
    }

    public void handleViewVisibility(int statusValue){
        if(statusValue == 1){
            recycler_view_wishList.setVisibility(RecyclerView.GONE);
            mRecyclerView.setVisibility(RecyclerView.VISIBLE);
        }else if(statusValue == 2){
            mRecyclerView.setVisibility(RecyclerView.GONE);
            recycler_view_wishList.setVisibility(RecyclerView.VISIBLE);
        }else if(statusValue == 3){
            mRecyclerView.setVisibility(RecyclerView.GONE);
            recycler_view_wishList.setVisibility(RecyclerView.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem search = menu.findItem(R.id.search);
        shoppingCart = menu.findItem(R.id.cart);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        shoppingCart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                Toast.makeText(HomeScreen.this, "Cart is Clicked", Toast.LENGTH_SHORT).show();
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

    // Function responsible fro fetching the guitar details
    public void fetchGuitarDetails(){
        LoaderUtils.showProgressBar(HomeScreen.this, "Please wait while fetching the details..");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("guitars");
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
                    // Setting the adapter
                    setUpAdapter();
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


    // Initializing the views
    private void initViews(){
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
    }

    // Setting up the adapter
    public void setUpAdapter(){
        recycler_view_wishList.setAdapter(null);
        itemAdapter = new ItemAdapter(HomeScreen.this, guitarDetailsModels);
        mRecyclerView.setAdapter(itemAdapter);
    }

    // Setting up the adapter
    public void setUpAdapterWishList(){
        mRecyclerView.setAdapter(null);
        itemAdapter = new ItemAdapter(HomeScreen.this, guitarDetailsModels);
        recycler_view_wishList.setAdapter(itemAdapter);
    }

    public void getUserInfo(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0);
        userId = pref.getString("userId", null);
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

}
