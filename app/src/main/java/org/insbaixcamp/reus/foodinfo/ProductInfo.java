package org.insbaixcamp.reus.foodinfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;

public class ProductInfo extends AppCompatActivity {

    TextView tvNoAllergens;



    ImageView ivProducto;
    String codigo;
    private TextView productNameTextView;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        Objects.requireNonNull(getSupportActionBar()).hide();

        productNameTextView = findViewById(R.id.productNameTextView);
        ivProducto = findViewById(R.id.ivProducto);
        //allergensTextView = findViewById(R.id.allergensTextView);
        listView = findViewById(R.id.lista);

        // Obtener el valor del extra "codigo" del Intent
        codigo = getIntent().getStringExtra("codigo");

        tvNoAllergens = findViewById(R.id.tvNoAllergens);
        tvNoAllergens.setVisibility(View.GONE);

        jsonRequest(codigo);

    }

    public void jsonRequest(String codigo) {
        String url = "https://world.openfoodfacts.org/api/v0/product/" + codigo + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        // Procesa la respuesta de la API
                        JSONObject product = response.getJSONObject("product");
                        String productName = product.getString("product_name");
                        String allergens = product.getString("allergens");

                        if (allergens.equals("")) {
                            tvNoAllergens.setVisibility(View.VISIBLE);
                            tvNoAllergens.setText(R.string.no_allergens);
                        } else {

                            Map<String, String> allergenTranslations = new HashMap<>();
                            allergenTranslations.put("milk", "leche");
                            allergenTranslations.put("nuts", "nueces");
                            allergenTranslations.put("soybeans", "soja");
                            allergenTranslations.put("mustard", "mostaza");
                            allergenTranslations.put("mollusc", "moluscos");
                            allergenTranslations.put("shellfish", "moluscos");
                            allergenTranslations.put("peanuts", "cacahuetes");
                            allergenTranslations.put("crustacean", "crustaceos");
                            allergenTranslations.put("tree nuts", "trazas");
                            allergenTranslations.put("fish", "pescado");
                            allergenTranslations.put("egg", "huevos");
                            allergenTranslations.put("eggs", "huevos");
                            allergenTranslations.put("sulphur-dioxide-and-sulphites", "dióxido de azufre y sulfitos");
                            allergenTranslations.put("sesame", "sesamo");
                            allergenTranslations.put("soy", "soja");
                            allergenTranslations.put("celery", "apio");
                            allergenTranslations.put("lupines", "altramuces");


                            for (Map.Entry<String, String> entry : allergenTranslations.entrySet()) {
                                allergens = allergens.replace(entry.getKey(), entry.getValue());
                            }


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
                            listView.setAdapter(adapter);

                        }


                        // Actualiza la interfaz de usuario con la información obtenida
                        productNameTextView.setText(productName);

                        // Carga la imagen del producto en el ImageView usando Volley
                        String imageUrl = product.getString("image_front_small_url");
                        loadImage(imageUrl, ivProducto, productName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (response.isNull("product")) {
                        productNameTextView.setText(R.string.error);
                        productNameTextView.setGravity(Gravity.CENTER);
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

        if (imageUrl == null || imageUrl.isEmpty()) {
            // Establece la imagen por defecto
            ivProducto.setImageResource(R.drawable.nodisponible);
            return;
        }

        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                // Actualiza la interfaz de usuario con la imagen cargada
                ivProducto.setImageBitmap(response);
                ivProducto.setVisibility(View.VISIBLE);


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

}