package org.insbaixcamp.reus.foodinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Search extends AppCompatActivity {

        private ProductAdapter mAdapter;
        private ListView mListViewProducts;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);


            // Obtén la lista de productos
            ProductList productList = ProductList.getInstance();

            // Elimina los productos duplicados
            Set<Product> uniqueProducts = new HashSet<>(productList.getProducts());
            productList.setProducts(new ArrayList<>(uniqueProducts));

            // Crea un adaptador personalizado
            mAdapter = new ProductAdapter(this, productList.getProducts());

            // Configura la ListView
            mListViewProducts = findViewById(R.id.list_view_products);
            mListViewProducts.setAdapter(mAdapter);
        }


}