package com.debasish.guitardhun.adapters;

import com.afollestad.materialdialogs.MaterialDialog;
import com.debasish.guitardhun.R;
import com.debasish.guitardhun.activities.CartScreen;
import com.debasish.guitardhun.activities.HomeScreen;
import com.debasish.guitardhun.activities.OrderConfirmation;
import com.debasish.guitardhun.models.CartModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Button;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.app.Activity;

import com.debasish.guitardhun.utils.RandomNumberGenerator;

public class CartAdapter extends
        RecyclerView.Adapter<CartAdapter.MyViewHolder> {


    private ArrayList<CartModel> cartList;
    Context context;
    DatabaseReference mDatabase =
            FirebaseDatabase.getInstance().getReference("cart").child(HomeScreen.userId);
    DatabaseReference mDatabaseOrder =
            FirebaseDatabase.getInstance().getReference("orders").child(HomeScreen.userId);
    public static int orderId;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvModel, tvType, tvPrice;
        TextView sQty;
        Button btnRemove;


        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvModel = (TextView) view.findViewById(R.id.tvModel);
            tvType = (TextView) view.findViewById(R.id.tvType);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            sQty = (TextView) view.findViewById(R.id.sQty);
            btnRemove = (Button) view.findViewById(R.id.btnRemove);

        }
    }

    public CartAdapter(Context ctx, ArrayList<CartModel> cartListData) {
        this.context = ctx;
        this.cartList = cartListData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_row_design, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        CartModel cart = cartList.get(position);
        holder.tvName.setText(cart.getItemName());
        holder.tvModel.setText("Model - " + cart.getItemModelNo());
        holder.tvType.setText("Type - " + cart.getItemType());
        holder.tvPrice.setText("INR " + cart.getItemPrice() + ".00");
        holder.sQty.setText("Qty - " + cart.getQty());

        holder.sQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;
                final String itemPrice = cartList.get(pos).getItemPrice();
                final String modelNo = cartList.get(pos).getItemModelNo();
                new MaterialDialog.Builder(context)
                        .title("Select Qty")
                        .items(R.array.qty_arrays)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog,
                                                    View view, int which,
                                                    final CharSequence text) {
                                //Toast.makeText(context, which + ": " + text, Toast.LENGTH_SHORT).show();
                                holder.sQty.setText("Qty - " + text);
                                updateQty(modelNo, text.toString(), itemPrice);
                            }
                        })
                        .show();
            }
        });

        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;
                final String modelNo = cartList.get(pos).getItemModelNo();
                removeItem(modelNo, pos);
            }
        });

        CartScreen.btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartScreen.btnPlaceOrder.setEnabled(false);
                ArrayList<CartModel> orderData = new ArrayList<>();
                for (int i = 0; i < cartList.size(); i++) {
                    orderData.add(cartList.get(i));
                }

                orderId = RandomNumberGenerator.getRandomNumber();

                // Placing the order
                mDatabaseOrder.child(String.valueOf(orderId)).setValue(orderData);

                // Removing user Data from the cart
                removeUserCart();

                // Enabling the Place Order Button
                CartScreen.btnPlaceOrder.setEnabled(true);

                // Moving the use to the next screen
                redirectUser();
            }
        });
    }

    // Redirecting the user
    public void redirectUser() {
        context.startActivity(new Intent(context, OrderConfirmation.class));
        ((Activity) context).finish();
    }

    // Function responsible for storing User Cart Data
    public void updateQty(String productModelNo, String qty, String pricePerQty) {
        mDatabase.child(productModelNo).child("qty").setValue(qty);
        mDatabase.child(productModelNo).child("totalAmt").setValue(calculateTotalAmount(pricePerQty, qty));
    }

    // Function responsible for removing a particular product
    public void removeItem(String productModelNo, int position) {
        mDatabase.child(productModelNo).getRef().removeValue();
        cartList.remove(position);
        notifyDataSetChanged();
    }

    // Function responsible for removing the cart information
    public void removeUserCart() {
        mDatabase.getRef().removeValue();
    }

    // Calculating the total Amount
    public String calculateTotalAmount(String pricePerItem, String qty) {
        int ppItem = Integer.parseInt(pricePerItem);
        int qtyItem = Integer.parseInt(qty);
        int totalAmt = ppItem * qtyItem;
        return String.valueOf(totalAmt);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
