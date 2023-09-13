package com.moutamid.halalfoodfinder.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.moutamid.halalfoodfinder.Fragment.FavouriteFragment;
import com.moutamid.halalfoodfinder.Fragment.ResturantFragment;
import com.moutamid.halalfoodfinder.Fragment.SettingFragment;
import com.moutamid.halalfoodfinder.Fragment.TypesFragment;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView binding;
    FloatingActionButton barcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = findViewById(R.id.bottomNavigationView);
        barcode = findViewById(R.id.barcode);
        Config.checkApp(MainActivity.this);
        replaceFragment(new ResturantFragment());
        binding.setBackground(null);
        binding.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.types) {
                    replaceFragment(new TypesFragment());
                } else if (item.getItemId() == R.id.home) {
                    replaceFragment(new ResturantFragment());
                }else if (item.getItemId() == R.id.favourite) {
                    replaceFragment(new FavouriteFragment());
                }else if (item.getItemId() == R.id.settings) {
                    replaceFragment(new SettingFragment());
                }
                return true;
            }
        });
        barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BarcodeActivity.class));
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}