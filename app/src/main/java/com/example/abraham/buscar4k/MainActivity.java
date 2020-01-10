package com.example.abraham.buscar4k;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button btCatalogo = this.findViewById(R.id.btCatalogo);
        final Button btOpciones = this.findViewById(R.id.btOpciones);
        final Button btIntegrantes = this.findViewById(R.id.btIntegrantes);
        final Button btSalir = this.findViewById(R.id.btSalir);

        btCatalogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent( MainActivity.this, Catalogo.class );
                startActivity( myIntent );
            }
        });

        btOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muestraDialogoOpciones();
            }
        });

        btIntegrantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                muestraIntegrantes();
            }
        });

        btSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }


    public void muestraDialogoOpciones(){
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);

        dlg.setTitle( "Aplicación aun en Desarrollo " );
        dlg.setMessage("Con el Botón 'RESTABLECER' dentro del Catálogo se puede conseguir restaurar la base de datos a su estado inicial \n\nEn caso de querer borrar una marca o modelo desde la lista" +
                " con mantenerlo presionado nos permite la opción de Borrarlo");
        dlg.setNeutralButton("Ok", null);

        dlg.create().show();
    }

    public void muestraIntegrantes(){
        final AlertDialog.Builder dlg = new AlertDialog.Builder(this);

        dlg.setTitle( "Integrantes: " );
        dlg.setMessage("Jorge Carlos Maza Espinosa \nAbraham E. Arce Sabin");
        dlg.setNeutralButton("Ok", null);

        dlg.create().show();
    }
}
