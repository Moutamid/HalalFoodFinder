package com.moutamid.halalfoodfinder.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.moutamid.halalfoodfinder.Model.ProductModel;
import com.moutamid.halalfoodfinder.R;

import java.util.List;

public class AllProductAdapter extends RecyclerView.Adapter<AllProductAdapter.GalleryPhotosViewHolder> {


    Context ctx;
    List<ProductModel> productModels;

    public AllProductAdapter(Context ctx, List<ProductModel> productModels) {
        this.ctx = ctx;
        this.productModels = productModels;
    }

    @NonNull
    @Override
    public GalleryPhotosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.products, parent, false);
        return new GalleryPhotosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryPhotosViewHolder holder, final int position) {
        ProductModel productModel = productModels.get(position);
        holder.item_type.setText(productModel.getItem_type());
        holder.item_name.setText(productModel.getName());
        if (productModels.get(position).getItem_type().equals("Limited Risk")) {
            holder.type.setBackgroundResource(R.drawable.red_bg);
        } else if (productModels.get(position).getItem_type().equals("Risky")) {
            holder.type.setBackgroundResource(R.drawable.orange_bg);

        }


    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class GalleryPhotosViewHolder extends RecyclerView.ViewHolder {

        TextView item_name, item_type;
View type;
        public GalleryPhotosViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name = itemView.findViewById(R.id.item_name);
            item_type = itemView.findViewById(R.id.item_type);
            type = itemView.findViewById(R.id.type);

        }
    }
}
