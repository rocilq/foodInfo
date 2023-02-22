package org.insbaixcamp.reus.foodinfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.insbaixcamp.reus.foodinfo.io.ApiAdapter;
import org.insbaixcamp.reus.foodinfo.model.Code;
import org.insbaixcamp.reus.foodinfo.model.Product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<Product> {

    private String ultimoCodigo = "";

    private final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            String codigo = result.getContents();
            Toast.makeText(this, "Scanned: " + codigo, Toast.LENGTH_LONG).show();
            if (codigo != null) {
                ultimoCodigo = codigo;
                callProduct(ultimoCodigo);
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton camara = findViewById(R.id.ibScan);

        camara.setOnClickListener(v -> scanCode());


    }

    private void scanCode() {

        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.EAN_13);
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(ActivityCap.class);

        barLauncher.launch(options);
    }

    public void callProduct(String code) {
        Call<Product> call = ApiAdapter.getApiService().getProduct(code);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Product> call, Response<Product> response) {
        if (response.isSuccessful()) {
            Product product = response.body();

            // Obtener la lista de alérgenos a partir de la cadena del JSON
            String allergensString = product.getAllergens_from_ingredients();
            String[] allergensArray = allergensString.split(", ");

            // Imprimir la lista de alérgenos
            if (allergensArray.length == 0) {
                Log.d("Product Info", "No se encontraron alérgenos para este producto");
            } else {
                Log.d("Product Info", "Alergenos: " + Arrays.toString(allergensArray));
            }

        } else {
            Log.e("Product Info", "Error getting product info: " + response.message());
        }
    }

    @Override
    public void onFailure(Call<Product> call, Throwable t) {
        Log.e("Product Info", "Error getting product info: " + t.getMessage());
    }
}