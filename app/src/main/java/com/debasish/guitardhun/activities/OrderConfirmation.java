package com.debasish.guitardhun.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.debasish.guitardhun.R;
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

public class OrderConfirmation extends AppCompatActivity {
    @BindView (R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.btnGoBack) Button btnSuccess;

    @OnClick (R.id.btnGoBack) void goBack(){
        OrderConfirmation.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        OrderConfirmation.this.finish();
        return true;
    }


}
