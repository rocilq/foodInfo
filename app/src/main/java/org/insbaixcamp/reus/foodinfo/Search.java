package org.insbaixcamp.reus.foodinfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.Response;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Search extends AppCompatActivity {

    private List<String> barcodesList = new ArrayList<>();
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Objects.requireNonNull(getSupportActionBar()).hide();

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


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Manejar errores en la lectura de la base de datos
                }
            });
        }

    }

    public void jsonRequest(String codigo) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + codigo + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Procesa la respuesta de la API
                            JSONObject product = response.getJSONObject("product");
                            String productName = product.getString("product_name");
                            String allergens = product.getString("allergens");

                            if (allergens.equals("")) {
                                //tvNoAllergens.setVisibility(View.VISIBLE);
                                //tvNoAllergens.setText(R.string.no_allergens);
                            } else {

                                // Separar la cadena en una matriz de cadenas
                                String[] allergensArray = allergens.split(",");

                                // Crear una lista de nombres de alérgenos
                                List<String> allergensList = new ArrayList<>();
                                for (String allergen : allergensArray) {
                                    // Eliminar la cadena "en:" de cada alérgeno
                                    String name = allergen.substring(3);
                                    allergensList.add(name);

                                }

                                // Usar la lista de nombres de alérgenos para crear la lista de elementos en la ListView
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                                        R.layout.item, R.id.tv_allergen_name, allergensList);
                                //listView.setAdapter(adapter);

                            }


                            // Actualiza la interfaz de usuario con la información obtenida
                            //productNameTextView.setText(productName);

                            // Carga la imagen del producto en el ImageView usando Volley
                            String imageUrl = product.getString("image_front_small_url");
                            //loadImage(imageUrl, ivProducto, productName);

//                            mAuth = FirebaseAuth.getInstance();
//                            FirebaseUser currentUser = mAuth.getCurrentUser();
//                            if (currentUser != null) {
//                                Intent intent = new Intent(getApplicationContext(), Search.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("nombre", productName);
//
//                                Bitmap bitmap = ((BitmapDrawable) ivProducto.getDrawable()).getBitmap();
//                                bundle.putParcelable("miImagen", bitmap);
//
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error de la solicitud
                        error.printStackTrace();
                    }
                });

        // Agrega la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void loadImage(String imageUrl, ImageView ivProducto, String productName) {
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // Actualiza la interfaz de usuario con la imagen cargada
                ivProducto.setImageBitmap(response);
                ivProducto.setVisibility(View.VISIBLE);

                //mAuth = FirebaseAuth.getInstance();
                //FirebaseUser currentUser = mAuth.getCurrentUser();
                /*if (currentUser != null) {
                    Bitmap image = ((BitmapDrawable) ivProducto.getDrawable()).getBitmap();

                    Code code = new Code(lCode);

                    code.addCode(codigo);

//                    // Agregar el objeto a la lista ProductList si no está duplicado
//                    ProductList productList = ProductList.getInstance();
//                    productList.addProduct(product);

                }*/

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Maneja el error de la solicitud
                        error.printStackTrace();
                    }
                });

        // Agrega la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(imageRequest);
    }


       /* private ProductAdapter mAdapter;
        private ListView mListViewProducts;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);
            Objects.requireNonNull(getSupportActionBar()).hide();

            // Inicializar el SearchView
            SearchView searchView = findViewById(R.id.search_view);

            // Obtén la lista de productos
            Code code = Code.getInstance();



            // Crea un adaptador personalizado
            mAdapter = new ProductAdapter(this, productList.getProducts());

            // Configura la ListView
            mListViewProducts = findViewById(R.id.list_view_products);
            mListViewProducts.setAdapter(mAdapter);

            // Agregar un listener para el SearchView
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Filtrar los elementos de la lista
                    mAdapter.getFilter().filter(newText);
                    return false;
                }
            });


            //
        }*/


}