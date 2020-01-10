package com.example.abraham.buscar4k;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Detalles extends AppCompatActivity {
    DatabaseHelper db;
    String marca = "";
    String modelo = "";
    String precio = "";
    String descripcion = "";
    String caracteristicas = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles);
        db = new DatabaseHelper(this);

        Intent datos = getIntent();
        marca = datos.getStringExtra("marca");
        modelo = datos.getStringExtra("modelo");
        precio = db.getPrecio(modelo);
        descripcion = db.getDescripcion(modelo);
        caracteristicas = db.getCaracteristicas(modelo);

        final TextView tvmarca = findViewById(R.id.tvMarca);
        final TextView tvmodelo = findViewById(R.id.tvModelo);
        final TextView tvprecio = findViewById(R.id.tvPrecio);
        //final TextView tvdescr = findViewById(R.id.tvDescripcion);
        final TextView tvcarac = findViewById(R.id.tvCaracteristicas);
        final Button btEliminar = findViewById(R.id.btEliminar);
        final WebView wvView = findViewById(R.id.wvView);

        tvmarca.setText(marca);
        tvmodelo.setText("Modelo: " + modelo);
        tvprecio.setText("Precio: " + precio);
        //tvdescr.setText("Direccion WEB: " + descripcion);
        tvcarac.setText("Características: " + caracteristicas);
        configureWebView( wvView, descripcion, 10 );

        btEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteModelo(modelo);
            }
        });



    }

    private void configureWebView(WebView wvView, String url, int defaultFontSize){
        WebSettings webSettings = wvView.getSettings();

        webSettings.setBuiltInZoomControls( true );
        webSettings.setDefaultFontSize( defaultFontSize );

        webSettings.setJavaScriptEnabled( true );
        wvView.addJavascriptInterface( new WebAppInterface( this ), "Android" );

        // No nos interesa evitar acceder a la pagina web
        //wvView.setWebViewClient( new WebViewClient() );

        //Cargar la url
        wvView.loadUrl( url );

    }

    private void deleteModelo (String modelo) {
        db.deleteModelo(marca, modelo);
        this.finish();
    }

}
