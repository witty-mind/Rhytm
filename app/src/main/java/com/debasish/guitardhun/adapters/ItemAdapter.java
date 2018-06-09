package com.debasish.guitardhun.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;

import com.debasish.guitardhun.R;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class ItemAdapter extends
        RecyclerView.Adapter<ItemAdapter.ViewHolder>
        implements Filterable {

    private ArrayList<GuitarDetailsModel> mArrayList;
    private ArrayList<GuitarDetailsModel> mFilteredList;


    public ItemAdapter(ArrayList<GuitarDetailsModel> arrayList) {
        mArrayList = arrayList;
        mFilteredList = arrayList;
    }


    @NonNull
    @Override
    public ItemAdapter.ViewHolder
        onCreateViewHolder(@NonNull ViewGroup parent,
                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder,
                                 int position) {
        viewHolder.tv_name.setText(mFilteredList.get(position).getName());
        viewHolder.tv_model.setText("Model: "+mFilteredList.get(position).getModelNo());
        viewHolder.tv_price.setText("INR "+mFilteredList.get(position).getPrice()+".00");
        String guitarImage = mFilteredList.get(position).getImage();
        if(guitarImage != null){
            Picasso.get().load(guitarImage).into(viewHolder.guitarImage);
        }
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {
                    ArrayList<GuitarDetailsModel> filteredList = new ArrayList<>();
                    for (GuitarDetailsModel guitarDetails : mArrayList) {
                        if (guitarDetails.getModelNo().toLowerCase().contains(charString) ||
                                guitarDetails.getModelNo().toUpperCase().contains(charString) ||
                                guitarDetails.getName().toLowerCase().contains(charString) ||
                                guitarDetails.getName().toUpperCase().contains(charString) ||
                                guitarDetails.getType().toLowerCase().contains(charString) ||
                                guitarDetails.getType().toUpperCase().contains(charString)) {
                            filteredList.add(guitarDetails);
                            Log.d("Filtered Values are", filteredList.toString());
                        }
                    }
                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<GuitarDetailsModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_model,tv_price;
        private ImageView guitarImage;
        public ViewHolder(View view) {
            super(view);
            guitarImage = (ImageView) view.findViewById(R.id.ivGuitarIcon);
            tv_name = (TextView)view.findViewById(R.id.tvGuitarName);
            tv_model = (TextView)view.findViewById(R.id.tvGuitarModel);
            tv_price = (TextView)view.findViewById(R.id.tvGuitarPrice);
        }
    }
}
