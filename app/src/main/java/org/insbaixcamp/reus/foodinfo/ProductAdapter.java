package org.insbaixcamp.reus.foodinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product> {

    private Context mContext;
    private List<Product> mProductList;

    public ProductAdapter(Context context, List<Product> productList) {
        super(context, 0, productList);
        mContext = context;
        mProductList = productList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item_product, parent, false);
        }

        Product currentProduct = mProductList.get(position);

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
}

