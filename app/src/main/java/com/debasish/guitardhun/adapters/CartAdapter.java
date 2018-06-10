package com.debasish.guitardhun.adapters;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.models.CartModel;

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

public class CartAdapter extends
        RecyclerView.Adapter<CartAdapter.MyViewHolder> implements OnItemSelectedListener {


    private ArrayList<CartModel> cartList;
    Context context;

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvModel, tvType, tvPrice;
        Spinner sQty;
        Button btnRemove;


        public MyViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvModel = (TextView) view.findViewById(R.id.tvModel);
            tvType = (TextView) view.findViewById(R.id.tvType);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            sQty = (Spinner) view.findViewById(R.id.sQty);
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
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CartModel cart = cartList.get(position);
        holder.tvName.setText(cart.getItemName());
        holder.tvModel.setText("Model - " +cart.getItemModelNo());
        holder.tvType.setText("Type - " +cart.getItemType());
        holder.tvPrice.setText("INR - " +cart.getItemPrice()+ ".00");
    }

    public void addValuetoSpinner(MyViewHolder viewHolder){
        ArrayList<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        list.add("6");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        viewHolder.sQty.setAdapter(dataAdapter);
        viewHolder.sQty.setOnItemSelectedListener(this);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
