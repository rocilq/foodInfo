package org.insbaixcamp.reus.foodinfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.insbaixcamp.reus.foodinfo.io.ApiAdapter;
import org.insbaixcamp.reus.foodinfo.model.Code;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<Code> {

    private String ultimoCodigo = "";

    private final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            String codigo = result.getContents();
            Toast.makeText(this, "Scanned: " + codigo, Toast.LENGTH_LONG).show();
            if (codigo != null) {
                ultimoCodigo = codigo;
                call(ultimoCodigo);
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

    public void call(String ultimoCodigo) {
        Call<Code> call = ApiAdapter.getApiService().getCode(ultimoCodigo);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<Code> call, Response<Code> response) {
        if (response.isSuccessful()) {
            Code code = response.body();
            Log.d("On response", "Code: " + code.getCode());
        }
    }

    @Override
    public void onFailure(Call<Code> call, Throwable t) {

    }
}