package com.example.abraham.buscar4k;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    DatabaseHelper db;
    private final int REQUEST_CODE = 1;

    ListView userlist;
    ArrayList<String> listItem;
    ArrayAdapter adapter;
    Button btRestablecer;
    ImageButton add_data;
    ImageButton search_data;
    EditText edSearch;


    String caract = "";
    String descr = "";
    String precio = "";
    String modelo = "";
    String marca = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo); db = new DatabaseHelper(this);
        //Asignamos los valores
        listItem = new ArrayList<>();
        userlist = findViewById(R.id.users_list);
        btRestablecer = findViewById(R.id.btRestablecer);
        add_data = findViewById(R.id.btAdd);
        search_data = findViewById(R.id.btSearch);
        edSearch = findViewById(R.id.edSearch);


        userlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//en caso de marcar en alguno de los elementos de la lista
                String text = userlist.getItemAtPosition(position).toString();
                Intent intent = new Intent( Catalogo.this, Modelos.class );//nos iremos a los modelos del elemento seleccionado
                intent.putExtra("marca", text);
                startActivity(intent);
            }
        });

        add_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//en caso de querer añadir alguna marca nueva o un nuevo modelo en la marca
                Intent myIntent = new Intent( Catalogo.this, add.class );//nos iremos a la clase "add"
                startActivityForResult( myIntent, REQUEST_CODE );
            }
        });

        search_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscar();
            }
        });

        btRestablecer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restablecerBaseDatos();
            }
        });

        this.registerForContextMenu(userlist);

        if(db.getCount() == 0){//En caso de que se eliminen todas las marcas o que se inicialice por primera vez la app se crearán los siguientes inserts de inicialización
            restablecerBaseDatos();
        }
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    protected void onPause() {
        super.onPause();
        db.close();
    }

    //Esta función nos permite buscar entre las diferentes marcas, si existe una coincidencia nos enviara a la actividad con los modelos correspondientes a dicha marca
    private void buscar(){
        final String text = edSearch.getText().toString();
        String toret = text.toUpperCase();

        if ( !text.isEmpty() ) {
            Cursor cursor = db.searchFor( text );

            if ( cursor != null ) {
                if ( cursor.moveToFirst() ) {
                    Intent intent = new Intent( Catalogo.this, Modelos.class );
                    intent.putExtra("marca", toret);
                    startActivity(intent);
                } else {
                    Toast.makeText( this, "No hay ninguna marca con ese nombre, prueba de nuevo", Toast.LENGTH_LONG ).show();
                }
            } else {
                Toast.makeText( this, "Internal BD Error", Toast.LENGTH_LONG ).show();
            }
        } else {
            Toast.makeText( this, "Escriba algo en el cuadro de búsqueda", Toast.LENGTH_LONG ).show();
        }

        return;
    }

    //Esta función nos permitirá restablecer los valores de fábrica de la base de datos
    private void restablecerBaseDatos(){//Hacer los inserts AQUI!
        db.removeAll();
        insert("HYUNDAI", "I40", "Desde 20.595", "https://www.hyundai.es/i40",  "Diseñado y desarrollado para satisfacer, Bajo coste de mantenimiento, Respetuoso con el medio ambiente, Sistema de 9 airbags, Anclajes ISOFIX, Asistente al arranque en pendiente (HAC)");
        insert("HYUNDAI", "i30 N", "Desde 28.800", "https://www.hyundai.es/i30n", "Con el i30 N llamarás la atención, y no sólo por el sonido del motor. Sus acabados deportivos y su chasis rebajado son un fiel reflejo del asombroso rendimiento que esconde en su interior. El rey de las curvas.");
        insert("HYUNDAI", "Santa FÉ", "Desde 29.700", "https://www.hyundai.es/nuevo-santafe",  "Espacio interior líder en su clase con 7 plazas de serie, Seguridad avanzada, Diseño SUV con tracción tota, Head-up display, Portón trasero automático inteligente, Sistema de tracción total HTRAC™");

        insert("BMW", "Serie 4 COUPÉ Deportivo", "Desde 37.000", "https://www.bmw.es/es/coches-bmw/serie-4/coupe/2017/presentacion.html", "Dinamismo en la conducción, Diseño de luces, Las cifras de consumo de combustible, emisiones de CO2 bajas");
        insert("BMW", "Serie 1", "Desde 26.375", "https://www.bmw.es/es/coches-bmw/serie-1/5-puertas/2017/presentacion.html", "5 Puertas, Deja huella, es divertido y se reconoce al instante. Con su diseño deportivo, excelente dinamismo y máxima conectividad, este nuevo héroe urbano conquista al instante. Después de todo, no solo se pone en marcha de inmediato, sino que es así como responde también su conectividad, lo que permite superar cualquier reto. Una pequeña diferencia que significa mucho.");
        insert("BMW", "Serie 5", "Desde 47.937", "https://www.bmw.es/es/coches-bmw/serie-5/berlina/2016/presentacion.html", "Su declaración de intenciones: marcar la pauta. El BMW Serie 5 es la berlina ejecutiva moderna por excelencia. Gracias a su dinamismo y elegancia, responde a las expectativas relativas a un vehículo de su categoría hoy en día: dinamismo y máximo placer de conducir con tecnología avanzada.");

        insert("FORD", "Mondeo", "Desde 33.600", "https://www.ford.es/turismos/mondeo?vehicleNavCategory=all%20cars", "Diseñado más allá de la pura estética, Limpia el parabrisas en un abrir y cerrar de ojos con Quickclear, EcoBoost: saca el máximo partido a tu motor, Protección adicional para los pasajeros del asiento trasero, ");
        insert("FORD", "Focus RS", "Desde 42.100", "https://www.ford.es/turismos/focus-rs?vehicleNavCategory=all%20cars", "Una visión imponente. Una fuerza potente, Máximo control en curvas, Sin manos con SYNC 3");
        insert("FORD", "Mustang", "Desde 40.200", "https://www.ford.es/turismos/mustang?vehicleNavCategory=all%20cars", "8,7 – 12,1 l/100km, 4 plazas, ás potencia. Más refinamiento. Más Mustang");

        insert("MERCEDES BENZ", "CLA Coupé", "Desde 35.886,29", "https://www.mercedes-benz.es/passengercars/mercedes-benz-cars/models/cla/cla-coupe/explore.html", "Su CLA Coupé le facilita la conducción, especialmente en situaciones estresantes como las horas punta, al conducir muchas horas de noche o por carreteras desconocidas.");
        insert("MERCEDES BENZ", "GLC 220 4MATIC", "Desde 29.184", "https://www.mercedes-benz.es/passengercars/mercedes-benz-cars/4matic-range/4MATIC.module.html", "4MATIC te proporciona estabilidad, tracción, control y agilidad en todas las condiciones meteorológicas. Nuestro embajador de marca Roger Federer, experto en conducción en nieve, confía plenamente en las virtudes de la tracción total 4MATIC.");
        insert("MERCEDES BENZ", "Clase E Berlina", "Desde 65273,49", "https://www.mercedes-benz.es/passengercars/mercedes-benz-cars/models/e-class/e-class-saloon/explore/amg.module.html", "Su Clase E Berlina le facilita la conducción, especialmente en situaciones de estrés como las horas punta, durante largos recorridos nocturnos o al circular por carreteras desconocidas. Esto se debe a un concepto que hace cualquier desplazamiento en un Mercedes-Benz más seguro.");

        insert("VOLKSWAGEN", "Passat Variant", "Desde 36.560", "https://www.volkswagen.es/es/modelos/passat-variant.html", "Diesel, 150 CV, Combina un extra de espacio con la elegancia de la gama Passat, y la tecnología más innovadora con los asistentes que harán cada viaje más seguro y cómodo.");
        insert("VOLKSWAGEN", "Golf", "Desde 32.055", "https://www.volkswagen.es/es/modelos/golf.html", "Tan bueno que mejora con los años. Así es el Golf, que además de ser fiel a un estilo, incorpora lo último en tecnología mientras sigue siendo un referente de la carretera.");
        insert("VOLKSWAGEN", "Polo", "Desde 20.800", "https://www.volkswagen.es/es/modelos/polo.html", "Conduce todo un Polo por 242€/mes sin entrada, seguro a todo riesgo sin límite de edad, mantenimiento, garantía, asistencia en viaje y entrega inmediata con My Renting y preocúpate solo por estrenar coche cada 4 años.");

        insert("OPEL", "Corsa", "Desde 13.500", "https://www.opel.es/coches/corsa/5-puertas/resumen.html", "Más fácil, más seguro e inteligente: el extraordinario coche urbano de 5 puertas con tecnología de clase superior y las innovaciones de la ingeniería alemana.");
        insert("OPEL", "Astra", "Desde 19.850", "https://www.opel.es/coches/astra/5-puertas/resumen.html", "5 Puertas, La referencia en la clase compacta un equipamiento de clase superior, manejabilidad deportiva y una conectividad sin precedentes.  ");
        insert("OPEL", "Insignia", "Desde 27.774", "https://www.opel.es/coches/nuevo-insignia/new-sports-tourer/resumen.html", "Diseño dinámico, conectividad superior y sistemas inteligentes de asistencia al conductor. Insignia, una irresistible invitación a viajar.");

        insert("DACIA", "Dokker", "Desde 8.960", "https://www.dacia.es/modelos/dokker.html", "La modularidad de sus asientos permite pasar de una configuración de 5 plazas a una configuración de 4, 3, o 2 plazas. Sus puertas laterales deslizantes facilitan el acceso a la carga haciendo del Dokker un vehículo polivalente.");
        insert("DACIA", "Sandero", "Desde 7.340", "https://www.dacia.es/modelos/sandero.html", "Diseño sólido, parrilla cromada, faros con la firma luminosa Dacia... Sandero te seducirá a primera vista. ");
        insert("DACIA", "Logan", "Desde 8.496", "https://www.dacia.es/modelos/logan.html", "Un diseño sólido, una parrilla cromada, faros con la firma luminosa Dacia... Logan lo tiene todo para seducirte al mejor precio");

        update();
    }

    //Con esta funcion podremos comunicarnos con la clase add y recoger los resultados impuestos en el formulario de dicha clase trayendolas a esta clase
    public void onActivityResult(int requestCode, int resultCode, Intent datosDevolver)
    {
        if ( requestCode == REQUEST_CODE
                && resultCode == RESULT_OK )
        {
            marca = datosDevolver.getStringExtra("marca");
            modelo = datosDevolver.getStringExtra("modelo");
            precio = datosDevolver.getStringExtra("precio");
            descr = datosDevolver.getStringExtra("descr");
            caract = datosDevolver.getStringExtra("caract");
            añadir(marca, modelo, precio, descr,  caract);
        }
    }

    //Permite añadir un elemento a la BD
    private void añadir(String marca, String modelo, String precio, String descr,String caract){
        if(marca.length() > 0){
            if(db.insertData(marca, modelo, precio, descr, caract)){
                Toast.makeText(Catalogo.this, "Marca insertada", Toast.LENGTH_SHORT).show();
                update();
            }else{
                Toast.makeText(Catalogo.this, "Data not added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //metodo creado para los inserts iniciales, quitando los mensajes TOAST
    private void insert(String marca, String modelo, String precio, String descr,String caract){
        db.insertData(marca, modelo, precio, descr, caract);

    }

//Visualiza los datos del catálogo
    private void viewData() {
        Cursor cursor = db.viewMarcas();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "No data to show", Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                listItem.add(cursor.getString(0));
            }
            adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItem);
            userlist.setAdapter(adapter);
        }
    }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        if ( v.getId() == R.id.users_list ) {
            this.getMenuInflater().inflate( R.menu.menu, menu );
        }
        return;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean toret = false;

        int pos = ( (AdapterView.AdapterContextMenuInfo) item.getMenuInfo() ).position;
        String eliminar = userlist.getItemAtPosition(pos).toString();
        switch ( item.getItemId() ) {
            case R.id.menu_delete:
                this.deleteMarca( eliminar );
                toret = true;
                break;
        }
        return toret;
    }

    private void deleteMarca(String eliminar) {
        db.deleteMarcas(eliminar);
        update();
    }

    private void update(){
        listItem.clear();
        viewData();
    }
    
}
