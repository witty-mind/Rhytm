package com.debasish.guitardhun.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.adapters.ItemAdapter;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.debasish.guitardhun.utils.LoaderUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeScreen extends AppCompatActivity {

    private TextView mTextMessage;
    ActionBar ab;
    @BindView(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    ArrayList<GuitarDetailsModel> guitarDetailsModels;
    @BindView(R.id.card_recycler_view) RecyclerView mRecyclerView;
    ItemAdapter itemAdapter;
    MenuItem shoppingCart;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //ab.setTitle("Home");
                    return true;
                case R.id.navigation_wishlist:
                   // ab.setTitle("DashBoard");
                    return true;
                case R.id.navigation_notifications:
                    //ab.setTitle("Profile");
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

        BottomNavigationView navigation =
                (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initViews();

        fetchGuitarDetails();
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
        itemAdapter = new ItemAdapter(guitarDetailsModels);
        mRecyclerView.setAdapter(itemAdapter);
    }

}
