package com.moutamid.halalfoodfinder.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

public class FavouriteFragment extends Fragment {

    RecyclerView content_rcv;
    public List<ResturantModel> productModelList = new ArrayList<>();
    AllResturantsAdapter herbsAdapter;
    TextView no_text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_favourite, container, false);
        content_rcv = view.findViewById(R.id.content_rcv);
        no_text = view.findViewById(R.id.no_text);
        content_rcv.setLayoutManager(new GridLayoutManager(getContext(), 1));
        herbsAdapter = new AllResturantsAdapter(getContext(), productModelList);
        content_rcv.setAdapter(herbsAdapter);
        if (Config.isNetworkAvailable(getContext())) {
            getProduct();
        } else {
            Toast.makeText(getContext(), "No network connection available.", Toast.LENGTH_SHORT).show();
        }


        return view;
    }

    public void getProduct() {
        Config.showProgressDialog(getContext());
        ArrayList<ResturantModel> resturantModels = Stash.getArrayList("Favourite", ResturantModel.class);
        Log.d("data", resturantModels.size() + "");
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