package com.debasish.guitardhun.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.debasish.guitardhun.activities.HomeScreen;
import com.debasish.guitardhun.models.GuitarDetailsModel;
import com.debasish.guitardhun.utils.LoaderUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;

public class ItemAdapter extends
        RecyclerView.Adapter<ItemAdapter.ViewHolder>
        implements Filterable  {

    private ArrayList<GuitarDetailsModel> mArrayList;
    public static ArrayList<GuitarDetailsModel> mFilteredList;
    Context context;
    DatabaseReference mDatabase;
    private ItemClickListener clickListener;


    public ItemAdapter(Context ctx, ArrayList<GuitarDetailsModel> arrayList) {
        this.mArrayList = arrayList;
        this.mFilteredList = arrayList;
        this.context = ctx;
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
    public void onBindViewHolder(@NonNull final ItemAdapter.ViewHolder viewHolder,
                                 final int position) {
        viewHolder.tv_name.setText(mFilteredList.get(position).getName());
        viewHolder.tv_model.setText("Model: "+ mFilteredList.get(position).getModelNo());
        viewHolder.tv_price.setText("INR "+ mFilteredList.get(position).getPrice()+".00");
        String guitarImage = mFilteredList.get(position).getImage();
        String guitarModel = mFilteredList.get(position).getModelNo().trim();

        if(guitarImage != null){
            Picasso.get().load(guitarImage).into(viewHolder.ivGuitarImage);
        }

        if(HomeScreen.favoriteArray != null &&
                HomeScreen.favoriteArray.size() > 0 &&
                HomeScreen.favoriteArray.contains(guitarModel)){
            setFavoriteImage(viewHolder, true);
        }

        viewHolder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = position;
                boolean isAvailable = false;

                GuitarDetailsModel guitarDetailsModel = mFilteredList.get(pos);
                String modelNo = guitarDetailsModel.getModelNo();
                if(HomeScreen.favoriteArray.contains(mFilteredList.get(pos).getModelNo())){
                    int itemPos = getItemPos(mFilteredList.get(pos).getModelNo());
                    HomeScreen.favoriteArray.remove(itemPos);
                    isAvailable = true;
                }else{
                    HomeScreen.favoriteArray.add(mFilteredList.get(pos).getModelNo());
                }

                // Refreshing user Favorites
                refreshUserFavorites();

                // Updating user Favorites
                updateUserFavorite(guitarDetailsModel, isAvailable, modelNo, viewHolder);
            }
        });
    }


    // Function responsible for refreshing the user Favorites
    public void refreshUserFavorites(){
        LoaderUtils.showProgressBar(context, "Please wait while updating..");
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        mDatabase.child(HomeScreen.userId).child("favorites").setValue(HomeScreen.favoriteArray);
    }

    // Function responsible for updating the user Favorites
    public void updateUserFavorite(GuitarDetailsModel guitarDetailsModel,
                                   boolean isAvailable, String modelNo, ViewHolder viewHolder){
        if(!isAvailable){
            mDatabase = FirebaseDatabase.getInstance().getReference("userwishlist");
            mDatabase.child(HomeScreen.userId).child(modelNo).setValue(guitarDetailsModel);
            // Refreshing the Image
            setFavoriteImage(viewHolder, true);
        }else{
            mDatabase = FirebaseDatabase.getInstance().getReference("userwishlist");
            mDatabase.child(HomeScreen.userId).child(modelNo).removeValue();
            // Refreshing the Image
            setFavoriteImage(viewHolder, false);
        }
        LoaderUtils.dismissProgress();
    }

    public static int getItemPos(String category) {
        return HomeScreen.favoriteArray.indexOf(category);
    }

    public void setFavoriteImage(ViewHolder viewHolder, boolean status){
        if(status){
            viewHolder.ivFavorite.setBackgroundResource(R.drawable.ic_favorite_red_400_24dp);
        }else{
            viewHolder.ivFavorite.setBackgroundResource(R.drawable.ic_favorite_border_red_400_24dp);
        }

    }

    // Storing UserDetails
    public void storingUserDetails(ArrayList<String> favorites){
        SharedPreferences pref = context.getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("userFavorites", HomeScreen.favoriteArray.toString());
        editor.commit();
    }

    @Override
    public int getItemCount() {
        return mFilteredList.size();
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

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tv_name,tv_model,tv_price;
        ImageView ivGuitarImage;
        final ImageView ivFavorite;
        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            ivGuitarImage = (ImageView) view.findViewById(R.id.ivGuitarIcon);
            tv_name = (TextView)view.findViewById(R.id.tvGuitarName);
            tv_model = (TextView)view.findViewById(R.id.tvGuitarModel);
            tv_price = (TextView)view.findViewById(R.id.tvGuitarPrice);
            ivFavorite = (ImageView) view.findViewById(R.id.ivFavorite);
            ivFavorite.setVisibility(View.GONE);
            view.setTag(view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onClick(View view, int position);
    }


}
