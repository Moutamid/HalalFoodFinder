package com.moutamid.halalfoodfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.halalfoodfinder.Model.ResturantModel;
import com.moutamid.halalfoodfinder.R;

import java.util.ArrayList;

public class RestaurantsDetailsActivity extends AppCompatActivity {
    ImageView image, favourite;
    TextView name, phone_res, address_res, description, website, title;
    TextView mon_opnening, tue_opnening, wed_opnening, thursday_opnening, fri_opnening, sat_opnening, sun_opnening;
    TextView mon_closing, tue_closing, wed_closing, thursday_closing, fri_closing, sat_closing, sun_closing;
    double lat, lng;
    Button map;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_details);
        image = findViewById(R.id.image);
        favourite = findViewById(R.id.favourite);
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
        name.setText(getIntent().getStringExtra("name"));
        title.setText(getIntent().getStringExtra("name"));
        address_res.setText(getIntent().getStringExtra("address"));
        tue_opnening.setText(getIntent().getStringExtra("tueOpnening"));
        tue_closing.setText(getIntent().getStringExtra("tueClosing"));
        wed_opnening.setText(getIntent().getStringExtra("wedOpnening"));
        wed_closing.setText(getIntent().getStringExtra("wedClosing"));
        fri_opnening.setText(getIntent().getStringExtra("friOpnening"));
        fri_closing.setText(getIntent().getStringExtra("fri_closing"));
        sat_opnening.setText(getIntent().getStringExtra("satOpnening"));
        sat_closing.setText(getIntent().getStringExtra("satClosing"));
        sun_opnening.setText(getIntent().getStringExtra("sunOpnening"));
        sun_closing.setText(getIntent().getStringExtra("sunClosing"));
        String imageUrl = getIntent().getStringExtra("imageUrl");
        Glide.with(RestaurantsDetailsActivity.this).load(imageUrl).into(image);
        phone_res.setText(getIntent().getStringExtra("phone"));

        description.setText(getIntent().getStringExtra("shortDescription"));
        mon_closing.setText(getIntent().getStringExtra("monClosing"));
        mon_opnening.setText(getIntent().getStringExtra("monOpnening"));
        thursday_closing.setText(getIntent().getStringExtra("thursdayClosing"));
        thursday_opnening.setText(getIntent().getStringExtra("thursdayOpnening"));
        website.setText(getIntent().getStringExtra("website"));

        lat = getIntent().getDoubleExtra("lat", 0.0);
        lng = getIntent().getDoubleExtra("lng", 0.0);
        key = getIntent().getStringExtra("key");
        ArrayList<ResturantModel> resturantModels = Stash.getArrayList("Favourite", ResturantModel.class);
if(resturantModels!=null)
{
    for (int i = 0; i < resturantModels.size(); i++) {
        if (key.equals(resturantModels.get(i).getKey())) {
            favourite.setImageResource(R.drawable.baseline_favorite_24);

        }
    }
}
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RestaurantsDetailsActivity.this, MapActivity.class);
                intent.putExtra("lat", lat);
                intent.putExtra("lng", lng);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                startActivity(intent);
            }
        });
        favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ResturantModel> resturantModel = Stash.getArrayList("Favourite", ResturantModel.class);

                resturantModel.add(new ResturantModel(key));
                Stash.put("Favourite", resturantModel);
                favourite.setImageResource(R.drawable.baseline_favorite_24);
            }
        });
    }

    public void onBack(View view) {
        onBackPressed();
    }
}