package com.debasish.guitardhun.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Toast;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.R;
import com.debasish.guitardhun.adapters.ItemAdapter;
import com.debasish.guitardhun.models.CartModel;
import com.debasish.guitardhun.models.UserModel;
import com.debasish.guitardhun.utils.LoaderUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;


import butterknife.BindView;
import butterknife.OnClick;


public class DetailsScreen extends AppCompatActivity {

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.addToCart)
    ImageView ivAddToCartAcc;
    @BindView(R.id.fab)
    android.support.design.widget.FloatingActionButton fabAddtoCart;
    @BindView(R.id.ivGuitarImage)
    ImageView ivProductImage;
    @BindView(R.id.ivAccessImage)
    ImageView ivSccImage;
    @BindView(R.id.tvProductName)
    TextView tvProductName;
    @BindView(R.id.tvProductModel)
    TextView tvProductModel;
    @BindView(R.id.tvGuitarPrice)
    TextView tvPrice;
    @BindView(R.id.tvAccName)
    TextView tvAccName;
    @BindView(R.id.tvModel)
    TextView tvAccModel;
    @BindView(R.id.tvModelPrice)
    TextView tvAccPrice;
    @BindView(R.id.ivFavorite)
    ImageView ivFavorite;
    @BindView(R.id.inCart)
    ImageView ivCart;
    GuitarDetailsModel guitars;
    boolean isAddonClicked = false;
    DatabaseReference mDatabase;
    boolean isAvailable;

    @OnClick(R.id.fab)
    void addProductToCart() {
        LoaderUtils.showProgressBar(DetailsScreen.this, "Please wait while adding items to cart..");
        // Checking the existence of the product
        checkProductExistence();
        LoaderUtils.dismissProgress();
    }

    @OnClick(R.id.addToCart)
    void addAddOnsToCart() {
        LoaderUtils.showProgressBar(DetailsScreen.this, "Please wait while adding items to cart..");
        // Checking the existence of the product
        checkAddonExistence();
        LoaderUtils.dismissProgress();
    }

    @OnClick(R.id.ivBack)
    void goBack() {
        // Finishing the activity
        DetailsScreen.this.finish();
    }

    @OnClick(R.id.ivFavorite)
    void makeFavorite() {
        makeWishLIst();
    }

    @OnClick(R.id.inCart)
    void cart() {
        startActivity(new Intent(DetailsScreen.this, CartScreen.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);
        ButterKnife.bind(this);
        // Fetching product details
        fetchProductDetail();
    }

    public void makeWishLIst() {
        if (HomeScreen.favoriteArray.contains(guitars.getModelNo())) {
            int itemPos = ItemAdapter.getItemPos(guitars.getModelNo());
            HomeScreen.favoriteArray.remove(itemPos);
            isAvailable = true;
        } else {
            HomeScreen.favoriteArray.add(guitars.getModelNo());
        }

        // Refreshing user Favorites
        refreshUserFavorites();

        // Updating user Favorites
        updateUserFavorite(guitars, isAvailable, guitars.getModelNo());
    }

    public void setFavoriteImage(boolean status) {
        if (status) {
            ivFavorite.setBackgroundResource(R.drawable.ic_favorite_red_400_24dp);
        } else {
            ivFavorite.setBackgroundResource(R.drawable.ic_favorite_border_red_400_24dp);
        }

    }

    // Function responsible for refreshing the user Favorites
    public void refreshUserFavorites() {
        LoaderUtils.showProgressBar(DetailsScreen.this, "Please wait while updating..");
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(HomeScreen.userId).child("favorites").setValue(HomeScreen.favoriteArray);
    }

    // Function responsible for updating the user Favorites
    public void updateUserFavorite(GuitarDetailsModel guitarDetailsModel,
                                   boolean isAvailable, String modelNo) {
        if (!isAvailable) {
            mDatabase = FirebaseDatabase.getInstance().getReference("userwishlist");
            mDatabase.child(HomeScreen.userId).child(modelNo).setValue(guitarDetailsModel);
            setFavoriteImage(true);
        } else {
            mDatabase = FirebaseDatabase.getInstance().getReference("userwishlist");
            mDatabase.child(HomeScreen.userId).child(modelNo).removeValue();
            setFavoriteImage(false);
        }
        LoaderUtils.dismissProgress();
    }


    /**
     * Building the cart Model
     *
     * @param productType 1 for products and 0 for add ons
     */
    public CartModel buildCartModel(boolean productType) {
        CartModel cartModel = new CartModel();
        if (productType) {
            cartModel.setItemName(guitars.getName());
            cartModel.setItemModelNo(guitars.getModelNo());
            cartModel.setItemType(guitars.getType());
            cartModel.setItemPrice(guitars.getPrice());
            cartModel.setTotalAmt(guitars.getPrice());
            cartModel.setQty("1");
        } else {
            cartModel.setItemName(guitars.getAccessoriesModel().getName());
            cartModel.setItemModelNo(guitars.getAccessoriesModel().getModelNo());
            cartModel.setItemType(guitars.getAccessoriesModel().getType());
            cartModel.setItemPrice(guitars.getAccessoriesModel().getPrice());
            cartModel.setTotalAmt(guitars.getAccessoriesModel().getPrice());
            cartModel.setQty("1");
        }
        return cartModel;
    }

    public void checkProductExistence() {
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference("cart").child(HomeScreen.userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("Items", snapshot.toString());
                if (!snapshot.hasChild(guitars.getModelNo())) {
                    // Building the Cart Model
                    CartModel cartModel = buildCartModel(true);
                    // Storing the Cart Model
                    storeCartData(cartModel);
                } else {
                    startActivity(new Intent(DetailsScreen.this, CartScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    /**
     * Checking if the child is already exist
     */
    public void checkAddonExistence() {
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference("cart").child(HomeScreen.userId);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d("Items", snapshot.toString());
                if (!snapshot.hasChild(guitars.getAccessoriesModel().getModelNo())) {
                    // Building the Cart Model
                    CartModel cartModel = buildCartModel(false);
                    // Storing the Cart Model
                    storeCartData(cartModel);
                } else {
                    //Toast.makeText(DetailsScreen.this, "The Item is already exist in the cart.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DetailsScreen.this, CartScreen.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Function responsible for storing User Cart Data
    public void storeCartData(CartModel cartModel) {
        DatabaseReference mDatabase =
                FirebaseDatabase.getInstance().getReference("cart").child(HomeScreen.userId);
        mDatabase.child(cartModel.getItemModelNo()).setValue(cartModel);
        Toast.makeText(this, "Item successfully added to the cart.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DetailsScreen.this.finish();
    }

    public void fetchProductDetail() {
        LoaderUtils.showProgressBar(DetailsScreen.this, "Please wait while fetching the details..");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("guitars").child(HomeScreen.productId);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                guitars = dataSnapshot.getValue(GuitarDetailsModel.class);
                Log.d("HomeScreen", "Value is: " + guitars.toString());
                LoaderUtils.dismissProgress();
                // Setting all the details data
                setDataInView(guitars);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                LoaderUtils.dismissProgress();
                // Failed to read value
            }
        });
    }

    /**
     * Function responsible for setting the details data in the view
     *
     * @param guitarModel Guitar Details Model
     */
    public void setDataInView(GuitarDetailsModel guitarModel) {

        if (guitarModel != null) {
            tvProductName.setText(guitarModel.getName());
            tvProductModel.setText("Model: " + guitarModel.getModelNo() + " " + "( " + guitarModel.getType() + " )");
            tvPrice.setText("INR " + guitarModel.getPrice() + ".00");
            tvAccName.setText(guitarModel.getAccessoriesModel().getName());
            tvAccModel.setText("Model: " + guitarModel.getAccessoriesModel().getModelNo() + " " + "( " + guitarModel.getAccessoriesModel().getType() + " )");
            tvAccPrice.setText("INR " + guitarModel.getAccessoriesModel().getPrice() + ".00");
            String productName = guitarModel.getImage();
            String accessImage = guitarModel.getAccessoriesModel().getImage();


            if (productName != null) {
                Picasso.get().load(productName).into(ivProductImage);
            }

            if (accessImage != null) {
                Picasso.get().load(accessImage).into(ivSccImage);
            }

            if (HomeScreen.favoriteArray.contains(guitarModel.getModelNo())) {
                ivFavorite.setBackgroundResource(R.drawable.ic_favorite_red_400_24dp);
            }
        }
    }

    public void saveCartData(CartModel cartModel) {

    }
}
