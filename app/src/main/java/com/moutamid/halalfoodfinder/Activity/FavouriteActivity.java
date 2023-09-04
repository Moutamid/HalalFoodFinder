package com.moutamid.halalfoodfinder.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.halalfoodfinder.Adapter.AllResturantsAdapter;
import com.moutamid.halalfoodfinder.Model.ResturantModel;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    RecyclerView content_rcv;
    public List<ResturantModel> productModelList = new ArrayList<>();
    AllResturantsAdapter herbsAdapter;
    ImageView backpress;
    TextView no_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        content_rcv = findViewById(R.id.content_rcv);
        backpress = findViewById(R.id.backpress);
        no_text = findViewById(R.id.no_text);
        content_rcv.setLayoutManager(new GridLayoutManager(FavouriteActivity.this, 1));
        herbsAdapter = new AllResturantsAdapter(FavouriteActivity.this, productModelList);
        content_rcv.setAdapter(herbsAdapter);
        if (Config.isNetworkAvailable(FavouriteActivity.this)) {
            getProduct();
        } else {
            Toast.makeText(FavouriteActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FavouriteActivity.this, MainActivity.class));
                finishAffinity();
            }
        });

    }

    public void getProduct() {
        Config.showProgressDialog(FavouriteActivity.this);
        ArrayList<ResturantModel> resturantModels = Stash.getArrayList("Favourite", ResturantModel.class);
        Log.d("data", resturantModels.size()+"");
        for (int i = 0; i < resturantModels.size(); i++) {
                Log.d("data", resturantModels.get(i).getKey());
        }
        Config.databaseReference().child("Restaurants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ResturantModel herbsModel = ds.getValue(ResturantModel.class);
                    for (int i = 0; i < resturantModels.size(); i++) {
                        if (herbsModel.getKey().equals(resturantModels.get(i).getKey())) {
                            productModelList.add(herbsModel);
                        }
                    }
                }
                Config.dismissProgressDialog();
                herbsAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

    }
}