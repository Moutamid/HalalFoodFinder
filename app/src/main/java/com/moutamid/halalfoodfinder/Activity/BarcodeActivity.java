package com.moutamid.halalfoodfinder.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.moutamid.halalfoodfinder.Dialogue.FeedBackDialogClass;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

import java.io.IOException;

public class BarcodeActivity extends AppCompatActivity {


    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    FloatingActionButton add_product;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    String itemCategory = "Doubtful";
    String item_type;
    String item_name;
    String item_barcode, key;
    Toolbar toolbar;
    ImageView image_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        toolbar = findViewById(R.id.toolbar_);
        image_type = findViewById(R.id.image_type);
        add_product = findViewById(R.id.add_product);
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BarcodeActivity.this, AddProductsActivity.class));
            }
        });
//        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(intentData)));
    }

    //TODO
    private void initialiseDetectorsAndSources() {

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature  
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(BarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(BarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

                    }

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {

                            if (barcodes.valueAt(0).email != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).email.address;
//                                txtBarcodeValue.setText(intentData);

                                getProducts(intentData);
                            } else {
                                intentData = barcodes.valueAt(0).displayValue;
//                                txtBarcodeValue.setText(intentData);
                                getProducts(intentData);

                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void getProducts(String type) {

        surfaceView.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);
        Config.databaseReference().child("Product").orderByChild("item_barcode").equalTo(type).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.hasChild("item_category")) {
                        itemCategory = ds.child("item_category").getValue().toString();
                        item_type = ds.child("item_type").getValue().toString();
                        item_name = ds.child("item_name").getValue().toString();
                        item_barcode = ds.child("item_barcode").getValue().toString();
                        key = ds.child("key").getValue().toString();
                        Log.d("type", itemCategory);
                        if (itemCategory.equals("Halal")) {
                            txtBarcodeValue.setText("Halal");
                        } else if (itemCategory.equals("Haram")) {
                            txtBarcodeValue.setText("Haram");
                        } else {
                            txtBarcodeValue.setText("Doubtful");


                        }
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override

                        public void run() {
                            FeedBackDialogClass cdd = new FeedBackDialogClass(BarcodeActivity.this, key, item_name, item_type, itemCategory, item_barcode);
                            cdd.show();

                        }

                    }, 1 * 1000); // wait for 5 seconds

                }
                if (itemCategory.equals("Doubtful")) {
                    txtBarcodeValue.setText("Doubtful");
                    add_product.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("date", databaseError.toString() + "  f");

            }
        });
    }

    public void backpress(View view) {
        onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, "abc "+requestCode+""+resultCode+""+data, Toast.LENGTH_SHORT).show();
//        if (requestCode==201) {
            initialiseDetectorsAndSources();

//        }
    }
}