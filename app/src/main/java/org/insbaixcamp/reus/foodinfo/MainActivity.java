package org.insbaixcamp.reus.foodinfo;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private String ultimoCodigo = "";

    private FirebaseAuth mAuth;

    private FirebaseDatabase fbDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = fbDatabase.getReference("usuarios");

    private final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() == null) {
            Log.d("state", "cancelled");
        } else {
            String codigo = result.getContents();
            ultimoCodigo = codigo;

            // Creao un Intent para lanzar la nueva actividad ProductInfo
            Intent intent = new Intent(this, ProductInfo.class);
            intent.putExtra("codigo", codigo);
            startActivity(intent);

//            jsonRequest(ultimoCodigo);
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {

                addCode(ultimoCodigo);


            } else {

            }

        }
    });



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();


        ImageButton camara = findViewById(R.id.ibScan);
        ImageButton user = findViewById(R.id.ibAccount);
        ImageButton search = findViewById(R.id.ibSearch);

        camara.setOnClickListener(v -> scanCode());
        user.setOnClickListener(v -> account());
        search.setOnClickListener(v -> search());

    }

    private void search() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, Search.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Tienes que iniciar sesión para ver los productos escaneados" , Toast.LENGTH_LONG).show();
        }
    }

    private void account() {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(MainActivity.this, Account.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
        }

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


    private void addCode(String code) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        assert user != null;
        String userId = user.getUid();

        // Consultar la base de datos para verificar si el código ya existe
        myRef.child(userId).orderByValue().equalTo(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Si el código no existe en la base de datos, guardarlo
                if (!dataSnapshot.exists()) {
                    myRef.child(userId).push().setValue(code);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Manejar el error si la consulta falla
            }
        });


    }

}