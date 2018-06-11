package com.debasish.guitardhun.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.adapters.CartAdapter;
import com.debasish.guitardhun.adapters.ItemAdapter;
import com.debasish.guitardhun.models.CartModel;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.debasish.guitardhun.utils.LoaderUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.OnClick;

public class CartScreen extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    ArrayList<CartModel> cartModelArray;
    @BindView(R.id.card_recycler_view)
    RecyclerView cartList;
    CartAdapter cartAdapter;
    ArrayList<String> amountArray;
    @BindView(R.id.totalAmt) TextView tvTotalAmt;
    @BindView(R.id.totalLay) RelativeLayout layTotalAmount;
    public static Button btnPlaceOrder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_screen);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        btnPlaceOrder = (Button) findViewById(R.id.btnPay);
        initViews();
        fetchCartData();
    }

    // Function responsible fro fetching the guitar details
    public void fetchCartData() {
        LoaderUtils.showProgressBar(CartScreen.this, "Please wait while fetching the details..");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("cart").child(HomeScreen.userId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                cartModelArray = new ArrayList<>();
                amountArray = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {
                    CartModel guitars = noteDataSnapshot.getValue(CartModel.class);
                    amountArray.add(guitars.getTotalAmt());
                    cartModelArray.add(guitars);
                }

                // Setting the adapter
                setUpAdapter();

                LoaderUtils.dismissProgress();
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
        cartList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        cartList.setLayoutManager(layoutManager);
    }

    // Setting up the adapter
    public void setUpAdapter() {
        if(cartModelArray.size() != 0){
            cartAdapter = new CartAdapter(CartScreen.this, cartModelArray);
            cartList.setAdapter(cartAdapter);
            calculateTotalAmt();
            layTotalAmount.setVisibility(RelativeLayout.VISIBLE);
        }else{
            Toast.makeText(this, "There is no item in yor cart.", Toast.LENGTH_SHORT).show();
            layTotalAmount.setVisibility(RelativeLayout.GONE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Function responsible for calculating the total amount of the cart
    public void calculateTotalAmt(){
        int totalAmount = 0;
        for(int i = 0; i < amountArray.size(); i++){
            int value = Integer.parseInt(amountArray.get(i));
            totalAmount = totalAmount + value;
        }
        tvTotalAmt.setText("Total - INR "+ String.valueOf(totalAmount)+ ".00");
    }
}
