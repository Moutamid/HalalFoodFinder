package com.moutamid.halalfoodfinder.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moutamid.halalfoodfinder.Activity.AddProductsActivity;
import com.moutamid.halalfoodfinder.Activity.MainActivity;
import com.moutamid.halalfoodfinder.Activity.ProductsActivity;
import com.moutamid.halalfoodfinder.R;


public class TypesFragment extends Fragment {
    CardView halal, haram, doubtful;
    CardView add_product;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_types, container, false);

        halal = view.findViewById(R.id.halal);
        haram = view.findViewById(R.id.haram);
        doubtful = view.findViewById(R.id.doubtful);
        add_product = view.findViewById(R.id.add_product);
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AddProductsActivity.class));
            }
        });
        halal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("type", "Halal");
                startActivity(intent);
            }
        });
        doubtful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("type", "Doubtful");
                startActivity(intent);
            }
        });
        haram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProductsActivity.class);
                intent.putExtra("type", "Haram");
                startActivity(intent);
            }
        });

        return view;
    }
}