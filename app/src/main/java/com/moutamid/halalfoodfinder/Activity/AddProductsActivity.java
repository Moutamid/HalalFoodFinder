package com.moutamid.halalfoodfinder.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.fxn.stash.Stash;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.moutamid.halalfoodfinder.Model.ProductModel;
import com.moutamid.halalfoodfinder.Model.UserModel;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

import java.io.IOException;


public class AddProductsActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    String intentData = "";
    EditText item_name, name, age, cnic;
    RadioGroup item_category, item_types;
    String item_category_str = "Halal", item_types_str = "Limited Risk", item_name_str, item_barcode_str;
    TextView add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        UserModel userInfo = (UserModel) Stash.getObject("User_info", UserModel.class);
//        Log.d("user", userNew.toString());
        initViews();
        if (userInfo != null) {
            name.setText(userInfo.Name);
            age.setText(userInfo.Age);
            cnic.setText(userInfo.CNIC);
        } else {
            Log.d("user", "data");

        }

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (initValues()) {
                    UserModel userModel = new UserModel(name.getText().toString(), age.getText().toString(), cnic.getText().toString());
                    Stash.put("User_info", userModel);
                    Config.showProgressDialog(AddProductsActivity.this);
                    String key = Config.databaseReference().child("UserProduct").push().getKey();
                    ProductModel productModel = new ProductModel();
                    productModel.setName(name.getText().toString());
                    productModel.setAge(age.getText().toString());
                    productModel.setCnic(cnic.getText().toString());
                    productModel.setItem_barcode(item_barcode_str);
                    productModel.setItem_name(item_name_str);
                    productModel.setItem_category(item_category_str);
                    productModel.setItem_type(item_types_str);
                    productModel.setStatus("Unverified");
                    productModel.setKey(key);
                    Config.databaseReference().child("UserProduct").child(key).setValue(productModel)
                            .addOnSuccessListener(aVoid -> {
                                Config.dismissProgressDialog();
                                Toast.makeText(AddProductsActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                                onBackPressed();
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Config.dismissProgressDialog();
                                Toast.makeText(AddProductsActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                            });
                }
            }
        });
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.item_barcode);
        surfaceView = findViewById(R.id.surfaceView);
        item_name = findViewById(R.id.item_name);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        cnic = findViewById(R.id.cnic);
        item_types = findViewById(R.id.item_type);
        item_category = findViewById(R.id.item_category);
        add_btn = findViewById(R.id.add_btn);
        item_types.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                item_types_str = radioButton.getText().toString();

            }
        });
        item_category.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                item_category_str = radioButton.getText().toString();
            }
        });
    }

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
                    if (ActivityCompat.checkSelfPermission(AddProductsActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(AddProductsActivity.this, new
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
//                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
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
                                txtBarcodeValue.setText(intentData);
                                surfaceView.setVisibility(View.GONE);
                            } else {
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                surfaceView.setVisibility(View.GONE);

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

    public boolean initValues() {
        if (item_name.getText().toString().isEmpty()) {
            item_name.setError("Enter here");
        } else {
            if (name.getText().toString().isEmpty()) {
                name.setError("Enter here");
            } else if (age.getText().toString().isEmpty()) {
                age.setError("Enter here");
            } else if (cnic.getText().toString().isEmpty()) {
                cnic.setError("Enter here");
            } else {
                item_barcode_str = txtBarcodeValue.getText().toString();
                item_name_str = item_name.getText().toString();
                return true;
            }

        }
        return false;
    }

    public void backpress(View view) {
        onBackPressed();
    }
}