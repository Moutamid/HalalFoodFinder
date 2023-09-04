package com.moutamid.halalfoodfinder.Dialogue;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moutamid.halalfoodfinder.Model.ProductModel;
import com.moutamid.halalfoodfinder.R;
import com.moutamid.halalfoodfinder.helper.Config;

public class FeedBackDialogClass extends Dialog {

    public Activity c;
    public Dialog d;
    private RadioGroup item_type;
    String item_types_str = "Like";
    TextView upload;
    String itemCategory;
    String item_type_;
    String item_name;
    String item_barcode;
    String key;
    public FeedBackDialogClass(Activity a, String key, String item_name, String item_type_, String itemCategory, String item_barcode) {
        super(a);
        this.c = a;
        this.item_name = item_name;
        this.item_type_ = item_type_;
        this.item_barcode = item_barcode;
        this.itemCategory = itemCategory;
        this.key = key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.like_dislike);
        item_type = findViewById(R.id.item_type);
        upload = findViewById(R.id.upload);
        item_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                item_types_str = radioButton.getText().toString();

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                addDataToFirebase(key, item_barcode, item_name, itemCategory, item_type_, item_types_str);

            }
        });

    }

    public void addDataToFirebase(String key, String item_barcode_str, String item_name_str, String item_category_str, String item_types_str, String feedback) {
        Config.showProgressDialog(c);
        String newkey = Config.databaseReference().child("Product").push().getKey();

        ProductModel productModel = new ProductModel();
        productModel.setItem_barcode(item_barcode_str);
        productModel.setItem_name(item_name_str);
        productModel.setItem_category(item_category_str);
        productModel.setItem_type(item_types_str);
        productModel.setFeedback(feedback);
        productModel.setKey(key);
        Config.databaseReference().child("Feedback").child(feedback).child(newkey).setValue(productModel)
                .addOnSuccessListener(aVoid -> {
                    dismiss();
                    Config.dismissProgressDialog();
                    c.finish();
                    Toast.makeText(c, "Successfully Submitted", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    dismiss();
                    Config.dismissProgressDialog();
                    c.finish();
                    Toast.makeText(c, "Please try again", Toast.LENGTH_SHORT).show();
                });

    }


}