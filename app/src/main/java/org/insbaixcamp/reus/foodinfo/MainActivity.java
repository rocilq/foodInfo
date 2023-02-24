package org.insbaixcamp.reus.foodinfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private String ultimoCodigo = "";

    private final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            String codigo = result.getContents();
            Toast.makeText(this, "Scanned: " + codigo, Toast.LENGTH_LONG).show();
            ultimoCodigo = codigo;

            // Creao un Intent para lanzar la nueva actividad ProductInfo
            Intent intent = new Intent(this, ProductInfo.class);
            intent.putExtra("codigo", codigo);
            startActivity(intent);

//            jsonRequest(ultimoCodigo);
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

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

}