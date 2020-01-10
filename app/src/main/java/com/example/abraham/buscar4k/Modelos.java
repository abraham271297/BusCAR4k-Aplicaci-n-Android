package com.example.abraham.buscar4k;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Modelos extends AppCompatActivity {
    DatabaseHelper db;

    ListView lista;
    ArrayList<String> listItem;
    ArrayAdapter adapter;

    String marca = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modelos);
        db = new DatabaseHelper(this);

        Intent datos = getIntent();

        marca = datos.getStringExtra("marca");
        lista = findViewById(R.id.modelos_list);
        listItem = new ArrayList<>();
        this.registerForContextMenu(lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = lista.getItemAtPosition(position).toString();
                Intent intent = new Intent( Modelos.this, Detalles.class );
                intent.putExtra("marca", marca);
                intent.putExtra("modelo", text);

                startActivity(intent);
            }
        });

        viewData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void viewData() {
        Cursor cursor = db.viewModelos(marca);

        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                listItem.add(cursor.getString(0));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            lista.setAdapter(adapter);
        }
    }

//elimina un modelo
    private void deleteModelo (String eliminar) {
        db.deleteModelo(marca, eliminar);
        update();
    }

    //refresca la lista
    private void update(){
        listItem.clear();
        viewData();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if ( v.getId() == R.id.modelos_list ) {
            this.getMenuInflater().inflate( R.menu.menu, menu );
        }
        return;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean toret = false;

        int pos = ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).position;
        String eliminar = lista.getItemAtPosition(pos).toString();
        switch ( item.getItemId() ) {
            case R.id.menu_delete:
                this.deleteModelo( eliminar );
                toret = true;
                break;
        }
        return toret;
    }
}
