package org.insbaixcamp.reus.foodinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductInfo extends AppCompatActivity {

//    TextView tvprueba;

    String codigo;
    private TextView productNameTextView;
    private TextView allergensTextView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        productNameTextView = findViewById(R.id.productNameTextView);
        //allergensTextView = findViewById(R.id.allergensTextView);
         listView = findViewById(R.id.lista);

        // Obtener el valor del extra "codigo" del Intent
         codigo = getIntent().getStringExtra("codigo");

//        // Mostrar el código en un TextView, por ejemplo
//        tvprueba = findViewById(R.id.tvPrueba);
//        tvprueba.setText(codigo);

        jsonRequest(codigo);

    }

    private void jsonRequest(String codigo) {
        String url = "https://world.openfoodfacts.org/api/v2/product/" + codigo + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Procesa la respuesta de la API
                            JSONObject product = response.getJSONObject("product");
                            String productName = product.getString("product_name");
                            String allergens = product.getString("allergens");

                            if (allergens.equals("")){
                                //allergensTextView.setText(R.string.no_allergens);
                            }else{

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
                                        R.layout.item,R.id.tv_allergen_name, allergensList);
                                listView.setAdapter(adapter);

                                //allergensTextView.setText(allergens);
                            }



                            // Actualiza la interfaz de usuario con la información obtenida
                            productNameTextView.setText(productName);

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


    public void separarAlergenos(String cadena){
//        String allergens = "en:milk,en:nuts,en:soybeans";


    }

}