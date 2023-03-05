package org.insbaixcamp.reus.foodinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Search extends AppCompatActivity {

    private List<String> barcodesList = new ArrayList<>();
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Inicializar el SearchView
        SearchView searchView = findViewById(R.id.search_view);

        // Obtener la instancia de la base de datos
        FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
        myRef = fbDatabase.getReference("usuarios");

        // Obtener los códigos de barras del usuario actual
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            myRef.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    barcodesList.clear(); // Limpiar la lista antes de agregar nuevos códigos
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String barcode = ds.getValue(String.class);
                        barcodesList.add(barcode);
                    }

                    // Aquí puedes usar la lista de códigos de barras para lo que necesites
                    List<Product> productList = new ArrayList<>();
                    AtomicInteger requestsCompleted = new AtomicInteger();

                    for (String barcode : barcodesList) {
                        String url = "https://world.openfoodfacts.org/api/v0/product/" + barcode + ".json";
                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                                response -> {
                                    try {
                                        JSONObject productJson = response.getJSONObject("product");
                                        String name = productJson.getString("product_name");
                                        String imageUrl = productJson.getString("image_front_small_url");
                                        Product product = new Product(name, imageUrl,barcode); // Agregar el código de barras al producto);
                                        productList.add(product);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    requestsCompleted.getAndIncrement();
                                    if (requestsCompleted.get() == barcodesList.size()) {
                                        // All requests have completed, so create adapter and set it on RecyclerView
                                        ProductAdapter adapter = new ProductAdapter(Search.this, productList);
                                        RecyclerView recyclerView = findViewById(R.id.rv_products);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(Search.this));
                                        adapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
                                            @Override
                                            public void onProductClick(int position) {
                                                Product product = productList.get(position);
                                                String barcode = product.getBarcode();
                                                Intent intent = new Intent(getApplicationContext(), ProductInfo.class);
                                                intent.putExtra("codigo", barcode);
                                                startActivity(intent);
                                            }
                                        });
                                        recyclerView.setAdapter(adapter);

                                        // Agregar un listener para el SearchView
                                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                            @Override
                                            public boolean onQueryTextSubmit(String query) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onQueryTextChange(String newText) {
                                                // Filtrar los elementos de la lista
                                                adapter.getFilter().filter(newText);
                                                return false;
                                            }
                                        });

                                    }
                                },
                                error -> {
                                    // Handle errors in API request
                                });
                        Volley.newRequestQueue(Search.this).add(request);
                        request.setRetryPolicy(new RetryPolicy() {
                            @Override
                            public int getCurrentTimeout() {
                                return 50000;
                            }

                            @Override
                            public int getCurrentRetryCount() {
                                return 50000;
                            }

                            @Override
                            public void retry(VolleyError error) throws VolleyError {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar errores en la lectura de la base de datos
                }
            });
        }

    }



}