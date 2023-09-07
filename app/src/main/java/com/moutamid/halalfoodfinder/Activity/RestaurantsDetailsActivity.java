package com.moutamid.halalfoodfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.halalfoodfinder.Model.ResturantModel;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

import java.util.ArrayList;

public class RestaurantsDetailsActivity extends AppCompatActivity {

    //TODO name change of variable
    ImageView resImage_img, favourite_img, unfavourite_img;
    TextView name, phone_res, address_res, description, website, title;
    TextView mon_opnening, tue_opnening, wed_opnening, thursday_opnening, fri_opnening, sat_opnening, sun_opnening;
    TextView mon_closing, tue_closing, wed_closing, thursday_closing, fri_closing, sat_closing, sun_closing;
    double lat, lng;
    Button map;

    ResturantModel current_resturantModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_details);
        resImage_img = findViewById(R.id.image);
        unfavourite_img = findViewById(R.id.unfavourite);
        favourite_img = findViewById(R.id.favourite);
        map = findViewById(R.id.map);
        phone_res = findViewById(R.id.phone_res);
        address_res = findViewById(R.id.address);
        website = findViewById(R.id.website);
        description = findViewById(R.id.description);
        name = findViewById(R.id.name);
        title = findViewById(R.id.title);
        mon_opnening = findViewById(R.id.mon_opnening);
        tue_opnening = findViewById(R.id.tue_opnening);
        wed_opnening = findViewById(R.id.wed_opnening);
        thursday_opnening = findViewById(R.id.thursday_opnening);
        fri_opnening = findViewById(R.id.fri_opnening);
        sat_opnening = findViewById(R.id.sat_opnening);
        sun_opnening = findViewById(R.id.sun_opnening);
        mon_closing = findViewById(R.id.mon_closing);
        tue_closing = findViewById(R.id.tue_closing);
        wed_closing = findViewById(R.id.wed_closing);
        thursday_closing = findViewById(R.id.thur_closing);
        fri_closing = findViewById(R.id.fru_closing);
        sat_closing = findViewById(R.id.sat_closing);
        sun_closing = findViewById(R.id.sun_closing);
        current_resturantModel = (ResturantModel) Stash.getObject(Config.currentModel, ResturantModel.class);
        name.setText(current_resturantModel.getName());
        title.setText(current_resturantModel.getName());
        address_res.setText(current_resturantModel.getAddress());
        tue_opnening.setText(current_resturantModel.getTue_opnening());
        tue_closing.setText(current_resturantModel.getTue_closing());
        wed_opnening.setText(current_resturantModel.getWed_opnening());
        wed_closing.setText(current_resturantModel.getWed_closing());
        fri_opnening.setText(current_resturantModel.getFri_opnening());
        fri_closing.setText(current_resturantModel.getFri_closing());
        sat_opnening.setText(current_resturantModel.getSat_opnening());
        sat_closing.setText(current_resturantModel.getSat_closing());
        sun_opnening.setText(current_resturantModel.getSun_opnening());
        sun_closing.setText(current_resturantModel.getSat_closing());
        Glide.with(RestaurantsDetailsActivity.this).load(current_resturantModel.getImage_url()).into(resImage_img);
        phone_res.setText(current_resturantModel.getPhone());
        description.setText(current_resturantModel.getShort_description());
        mon_closing.setText(current_resturantModel.getMon_closing());
        mon_opnening.setText(current_resturantModel.getMon_opnening());
        thursday_closing.setText(current_resturantModel.getThursday_closing());
        thursday_opnening.setText(current_resturantModel.getThursday_opnening());
        website.setText(current_resturantModel.getWebsite());
        ArrayList<ResturantModel> resturantModels = Stash.getArrayList(Config.favourite, ResturantModel.class);
        if (resturantModels != null) {
            for (int i = 0; i < resturantModels.size(); i++) {

                if (current_resturantModel.getKey().equals(resturantModels.get(i).getKey())) {
                    unfavourite_img.setVisibility(View.VISIBLE);

                } else {
                    favourite_img.setVisibility(View.VISIBLE);

                }

            }
}
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("latlng", current_resturantModel.getLat() + "   " + current_resturantModel.getLng());

                if (current_resturantModel.getLat() > -90 && current_resturantModel.getLat() < 90 && current_resturantModel.getLng() > -180 && current_resturantModel.getLng() < 180) {
                    Intent intent = new Intent(RestaurantsDetailsActivity.this, MapActivity.class);
                    intent.putExtra("lat", current_resturantModel.getLat());
                    intent.putExtra("lng", current_resturantModel.getLng());
                    intent.putExtra("name", current_resturantModel.getName());
                    startActivity(intent);

                } else {
                    Toast.makeText(RestaurantsDetailsActivity.this, "Invalid Coordinates to show marker", Toast.LENGTH_SHORT).show();
                }

            }
        });
        favourite_img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ArrayList<ResturantModel> resturantModelArrayList = Stash.getArrayList(Config.favourite, ResturantModel.class);
                resturantModelArrayList.add(current_resturantModel);
                Stash.put(Config.favourite, resturantModelArrayList);
                unfavourite_img.setVisibility(View.VISIBLE);
                favourite_img.setVisibility(View.GONE);
            }
        });
        unfavourite_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ResturantModel> resturantModel = Stash.getArrayList(Config.favourite, ResturantModel.class);
                for (int i = 0; i < resturantModel.size(); i++) {
                    if (resturantModel.get(i).getKey().equals(current_resturantModel.getKey())) {
                        resturantModel.remove(i);
                    }
                }
                Stash.put(Config.favourite, resturantModel);
                unfavourite_img.setVisibility(View.GONE);
                favourite_img.setVisibility(View.VISIBLE);
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }
}