package com.example.abraham.buscar4k;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Catalogo.db";
    private static final String DB_TABLE = "Catalogo";



    //Columnas
    public static final String ID = "ID";
    public static final String MARCA ="Users_Marca";
    public static final String MODELO ="Users_Modelo";
    public static final String PRECIO_ESTIMADO ="Users_Precio";
    public static final String DESCRIPCION ="Users_Descripcion";
    public static final String CARACTERISTICAS ="Users_Características";

    private static final String CREATE_TABLE = "CREATE TABLE " + DB_TABLE+" ("+//Creamos la BBDD
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            MARCA+ " TEXT NOT NULL,"+
            MODELO+ " TEXT NOT NULL,"+
            PRECIO_ESTIMADO+ " TEXT NOT NULL,"+
            DESCRIPCION+ " TEXT NOT NULL,"+
            CARACTERISTICAS+ " TEXT NOT NULL"+
            ")";



    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);//Ejecutamos la BBDD
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ DB_TABLE);
        onCreate(db);
    }

    //Esta función nos permite insertar un dato a la base de datos, pasando como parámetros los elementos necesarios
    public boolean insertData(String marca, String modelo, String precio, String descripcion, String caract){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MARCA, marca);
        contentValues.put(MODELO, modelo);
        contentValues.put(PRECIO_ESTIMADO, precio);
        contentValues.put(DESCRIPCION, descripcion);
        contentValues.put(CARACTERISTICAS, caract);

        long result = db.insert(DB_TABLE, null, contentValues);

        return result != -1;
    }

    //Esta función nos permite disponer de todas las marcas existentes en la BBDD que coincidan con el parámetro
    public Cursor viewData(String marca){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE+ " where " + MARCA + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{ marca });

        return cursor;
    }

    //Esta función nos permite acceder a toda la BD
    public Cursor viewAllData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //Esta función nos permite acceder a todas las marcas de la BBDD
    public Cursor viewMarcas(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select DISTINCT " + MARCA + " from "+DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    //Esta función nos permite borrar todos los modelos de una marca, incluida la marca obviamente
    public void deleteMarcas(String marca){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = viewData(marca);
        while(c.moveToNext()){
            remove(marca);
        }
    }

    public void removeAll(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = viewAllData();
        while(c.moveToNext()){
            remove(c.getString(1));
        }
    }

    //Esta función nos permite tener todos los modelos que tengan como marca el valor pasado de parámetro
    public Cursor viewModelos(String marca){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + MODELO + " from "+DB_TABLE+ " where " + MARCA + " LIKE ? ";
        Cursor cursor = db.rawQuery(query, new String[]{ marca });

        return cursor;
    }

    //Elimina un elemento de la BD, el que coincida con el id
    public void remove(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( DB_TABLE, MARCA + " = ? ", new String[]{ id } );
    }

    //Esta función nos permite eliminar un modelo de la BD
    public void deleteModelo(String id, String id1){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete( DB_TABLE, MARCA + " = ? AND " + MODELO + " = ?", new String[]{ id  , id1} );
    }

    //Devuelve un entero con la cantidad de valores de la BBDD
    public int getCount(){
        int toret = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = viewAllData();
        toret = cursor.getCount();

        return toret;
    }

    //Devuelve el precio
    public String getPrecio(String modelo){
        String toret = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE+ " where " + MODELO + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{ modelo });
        cursor.moveToFirst();
        toret = cursor.getString(3);

        return toret;
    }

    //Devuelve la descripcion...
    public String getDescripcion(String modelo){
        String toret = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE+ " where " + MODELO + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{ modelo });
        cursor.moveToFirst();
        toret = cursor.getString(4);

        return toret;
    }

    public String getCaracteristicas(String modelo){
        String toret = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE+ " where " + MODELO + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{ modelo });
        cursor.moveToFirst();
        toret = cursor.getString(5);

        return toret;
    }

    //Nos permite buscar dentro de la base de datos las tuplas en las que la marca coincida con el parámetro pasado
    public Cursor searchFor(String text)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE+ " where " + MARCA + " LIKE ?";
        Cursor toret = db.rawQuery(query, new String[]{ text });

        return toret;
    }

    //comprueba si existe un valor en la BBDD con el par marca-modelo
    public boolean existeModelo(String marca, String modelo){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+DB_TABLE+ " where " + MARCA + " LIKE ? AND " + MODELO + " LIKE ?";
        Cursor cursor = db.rawQuery(query, new String[]{marca, modelo});
        if(cursor.getCount() == 0){
            return true;//no existe una marca-modelo con esos datos
        }else{
            return false;
        }
    }
}
