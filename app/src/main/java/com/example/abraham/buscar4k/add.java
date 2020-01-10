package com.example.abraham.buscar4k;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add extends AppCompatActivity {

    DatabaseHelper db;

    String marca = "";
    String modelo = "";
    String precio = "";
    String descr = "";
    String caract = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = new DatabaseHelper(this);


        final EditText btMarca = findViewById(R.id.EDMarca);
        final EditText btModelo = findViewById(R.id.EDModelo);
        final EditText btPrecio = findViewById(R.id.EDPrecio);
        final EditText btDescripcion = findViewById(R.id.EDDescripcion);
        final EditText btCaracteristicas = findViewById(R.id.EDCaracterísticas);


        Button btVuelve = (Button) this.findViewById(R.id.btVuelve);
        Button btAgregar = (Button) this.findViewById(R.id.btAceptar);
        marca = btMarca.getText().toString().toUpperCase();
        modelo = btModelo.getText().toString().toUpperCase();
        precio = btPrecio.getText().toString().toUpperCase();
        descr = btDescripcion.getText().toString().toUpperCase();
        caract = btCaracteristicas.getText().toString().toUpperCase();

        btAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marca = btMarca.getText().toString().toUpperCase();
                modelo = btModelo.getText().toString().toUpperCase();
                precio = btPrecio.getText().toString().toUpperCase();
                descr = btDescripcion.getText().toString().toUpperCase();
                caract = btCaracteristicas.getText().toString().toUpperCase();
                auxiliar();
            }
        });

        btVuelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent datosDevolver = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
    private void auxiliar() {
        if (marca.length() == 0 || modelo.length() == 0 || precio.length() == 0 || descr.length() == 0 || caract.length() == 0) {//comprobamos que todos los campos esten ocupados
            Toast.makeText(add.this, "No puede existir ningún campo vacío, rellenalos todos", Toast.LENGTH_LONG).show();
        }else if(!db.existeModelo(marca, modelo)){//comprobamos que no existe una tubla en la BBDD con los mismos valores
            Toast.makeText(add.this, "El modelo-marca insertado ya existe en la base de datos, cambie los nombres o bien elimine dicho modelo existente", Toast.LENGTH_LONG).show();
        }else{
            Intent datosDevolver = new Intent();
            datosDevolver.putExtra("marca", marca);
            datosDevolver.putExtra("modelo", modelo);
            datosDevolver.putExtra("precio", precio);
            datosDevolver.putExtra("descr", descr);
            datosDevolver.putExtra("caract", caract);
            setResult(RESULT_OK, datosDevolver);
            finish();
        }
    }
}
