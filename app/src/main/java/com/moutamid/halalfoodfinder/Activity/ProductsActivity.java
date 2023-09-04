package com.moutamid.halalfoodfinder.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.icu.lang.UCharacter;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.halalfoodfinder.Adapter.AllProductAdapter;
import com.moutamid.halalfoodfinder.Model.ProductModel;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {

    RecyclerView content_rcv;
    CardView main_lyt;
    public List<ProductModel> productModelList = new ArrayList<>();
    AllProductAdapter productAdapter;
    TextView nothing, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        content_rcv = findViewById(R.id.content_rcv);
        nothing = findViewById(R.id.nothing);
        title = findViewById(R.id.title);
        main_lyt = findViewById(R.id.main_lyt);
        content_rcv.setLayoutManager(new GridLayoutManager(this, 1));
        productAdapter = new AllProductAdapter(this, productModelList);
        content_rcv.setAdapter(productAdapter);
        String type = getIntent().getStringExtra("type");
        title.setText(type + " Products");
        if (Config.isNetworkAvailable(ProductsActivity.this)) {
            getProducts(type);
        } else {
            Toast.makeText(ProductsActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }

    }


    private void getProducts(String type) {
        productModelList.clear();

        Dialog progressDialog = new Dialog(ProductsActivity.this);
        progressDialog.setContentView(R.layout.loading);
        Objects.requireNonNull(progressDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(UCharacter.JoiningType.TRANSPARENT));
        progressDialog.setCancelable(false);
        progressDialog.show();
        Config.databaseReference().child("Product").orderByChild("item_category").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productModelList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ProductModel herbsModel = ds.getValue(ProductModel.class);
                    productModelList.add(herbsModel);
                }
                if (productModelList != null) {

                    nothing.setVisibility(View.GONE);
                    main_lyt.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();

                } else {
                    progressDialog.dismiss();


                    main_lyt.setVisibility(View.GONE);
                    nothing.setVisibility(View.VISIBLE);

                }
                productAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Config.dismissProgressDialog();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Config.isNetworkAvailable(ProductsActivity.this)) {
            String type = getIntent().getStringExtra("type");
            getProducts(type);
        } else {
            Toast.makeText(ProductsActivity.this, "No network connection available.", Toast.LENGTH_SHORT).show();
        }
    }

    public void backpress(View view) {
        onBackPressed();
    }
}