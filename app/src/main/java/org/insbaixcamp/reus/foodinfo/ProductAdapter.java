package org.insbaixcamp.reus.foodinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> implements Filterable {

    private Context mContext;
    private List<Product> mProductList;
    private List<Product> mFilteredProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        mContext = context;
        mProductList = productList;
        mFilteredProductList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_product, parent, false);
        }

        Product currentProduct = mFilteredProductList.get(position);

        ImageView imageViewProduct = listItem.findViewById(R.id.product_image);
        imageViewProduct.setImageBitmap(currentProduct.getImage());

        TextView textViewProductName = listItem.findViewById(R.id.product_name);
        textViewProductName.setText(currentProduct.getName());

        return listItem;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return mFilteredProductList.size();
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = mProductList.size();
                    filterResults.values = mProductList;
                } else {
                    List<Product> filteredProducts = new ArrayList<>();
                    for (Product product : mProductList) {
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


