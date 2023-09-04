package com.moutamid.halalfoodfinder.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.moutamid.halalfoodfinder.Activity.RestaurantsDetailsActivity;
import com.moutamid.halalfoodfinder.Model.ResturantModel;
import com.moutamid.halalfoodfinder.R;

import java.util.ArrayList;
import java.util.List;

public class AllResturantsAdapter extends RecyclerView.Adapter<AllResturantsAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<ResturantModel> productModels;

    public AllResturantsAdapter(Context ctx, List<ResturantModel> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.allresturants, parent, false);
        return new GalleryPhotosViewHolder(view);
    }
    public void filterList(ArrayList<ResturantModel> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        productModels = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        final String name = productModels.get(position).getName();
        final String address = productModels.get(position).getAddress();
        String imageUrl = productModels.get(position).getImage_url();
        String phone = productModels.get(position).getPhone();
        String shortDescription = productModels.get(position).getShort_description();
        String monClosing = productModels.get(position).getMon_closing();
        String monOpnening = productModels.get(position).getMon_opnening();
        String thursdayClosing = productModels.get(position).getThursday_closing();
        String thursdayOpnening = productModels.get(position).getThursday_opnening();
        String tueOpnening = productModels.get(position).getTue_opnening();
        String tueClosing = productModels.get(position).getTue_closing();
        String wedOpnening = productModels.get(position).getWed_opnening();
        String wedClosing = productModels.get(position).getWed_closing();
        String friOpnening = productModels.get(position).getFri_opnening();

        String fri_closing = productModels.get(position).getFri_closing();

        String satOpnening = productModels.get(position).getSat_opnening();
        String satClosing = productModels.get(position).getSat_closing();
        String sunOpnening = productModels.get(position).getSun_opnening();
        String sunClosing = productModels.get(position).getSun_closing();
        String website = productModels.get(position).getWebsite();
        double lat = productModels.get(position).getLat();
        double lng = productModels.get(position).getLng();
        String key = productModels.get(position).getKey();

        holder.resturant_name.setText(name);
        holder.resturant_discription.setText(address);
        Glide.with(ctx).load(imageUrl).into(holder.image);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(ctx, RestaurantsDetailsActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("address", address);
            intent.putExtra("tueOpnening", tueOpnening);
            intent.putExtra("tueClosing", tueClosing);
            intent.putExtra("wedOpnening", wedOpnening);
            intent.putExtra("wedClosing", wedClosing);
            intent.putExtra("friOpnening", friOpnening);
            intent.putExtra("fri_closing", fri_closing);
            intent.putExtra("satOpnening", satOpnening);
            intent.putExtra("satClosing", satClosing);
            intent.putExtra("sunOpnening", sunOpnening);
            intent.putExtra("sunClosing", sunClosing);
            intent.putExtra("imageUrl", imageUrl);
            intent.putExtra("phone", phone);
            intent.putExtra("shortDescription", shortDescription);
            intent.putExtra("monClosing", monClosing);
            intent.putExtra("monOpnening", monOpnening);
            intent.putExtra("thursdayClosing", thursdayClosing);
            intent.putExtra("thursdayOpnening", thursdayOpnening);
            intent.putExtra("website", website);
            intent.putExtra("lat", lat);
            intent.putExtra("lng", lng);
            intent.putExtra("key", key);
            ctx.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView resturant_discription, resturant_name;
        ImageView image;

        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            resturant_discription = itemView.findViewById(R.id.resturant_discription);
            resturant_name = itemView.findViewById(R.id.resturant_name);
            image = itemView.findViewById(R.id.image);

        }
    }
}
