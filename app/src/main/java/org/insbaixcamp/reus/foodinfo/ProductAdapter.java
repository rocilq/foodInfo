package org.insbaixcamp.reus.foodinfo;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> implements View.OnClickListener, Filterable {

    private Context context;
    private List<Product> productList;
    private View.OnClickListener listener;
    private List<Product> mFilteredProductList;

    private OnProductClickListener mListener;

    public void setOnProductClickListener(OnProductClickListener listener) {
        mListener = listener;
    }

    public interface OnProductClickListener {
        void onProductClick(int position);
    }

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_product, parent, false);

        view.setOnClickListener(this);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = productList.get(position);

        if (holder.getAdapterPosition() != RecyclerView.NO_POSITION){

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onProductClick(holder.getAdapterPosition());
                    }
                }
            });

       }


        holder.nameTextView.setText(product.getName());


//        Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.nodisponible).into(holder.imageView);

        // Verificar si la imagen del producto es nula o no válida
        if (product.getImageUrl() == null || product.getImageUrl().isEmpty() || !Patterns.WEB_URL.matcher(product.getImageUrl()).matches()) {
            // Si la imagen es nula o no válida, establecer la imagen por defecto
            holder.imageView.setImageResource(R.drawable.nodisponible);
        } else {
            // Si la imagen es válida, cargarla en el ImageView con Picasso
            Picasso.get().load(product.getImageUrl()).placeholder(R.drawable.nodisponible).into(holder.imageView);
        }


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        if (listener != null ){
            listener.onClick(view);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView nameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            nameTextView = itemView.findViewById(R.id.product_name);
        }
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = productList.size();
                    filterResults.values = productList;
                } else {
                    List<Product> filteredProducts = new ArrayList<>();
                    for (Product product : productList) {
                        if (product.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredProducts.add(product);
                        }
                    }
                    filterResults.count = filteredProducts.size();
                    filterResults.values = filteredProducts;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredProductList = (List<Product>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

}


