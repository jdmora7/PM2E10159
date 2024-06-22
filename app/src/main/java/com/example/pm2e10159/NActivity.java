package com.example.pm2e10159;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pm2e10159.ui.theme.sampledata.entidades.db.DbContactos;

public class NActivity extends AppCompatActivity {


    EditText txtNombre, txtTelefono, txtNota;
    Button btnGuardar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nactivity);

        txtNombre = findViewById(R.id.txtNombre);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtNota = findViewById(R.id.txtNota);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbContactos dbContactos = new DbContactos(NActivity.this);
                long id = dbContactos.insertarContacto(txtNombre.getText().toString(), Integer.parseInt(txtTelefono.getText().toString()),txtNota.getText().toString());

                if (id > 0){
                    Toast.makeText(NActivity.this,"Contacto guardado", Toast.LENGTH_LONG).show();
                    limpiar();
                } else
                {
                    Toast.makeText(NActivity.this,"Error al guardar el contacto", Toast.LENGTH_LONG).show();

                }
            }
        });




        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void limpiar(){
        txtNombre.setText("");
        txtTelefono.setText("");
        txtNota.setText("");
    }
}