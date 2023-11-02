package com.vicente.sqliteoperaciones;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbAdapter {
    dBHelper helper;

    public DbAdapter(Context context) {
    helper = new dBHelper(context);
    }
    public long insertarDatos(String usuario, String password) {
    SQLiteDatabase db = helper.getWritableDatabase();
    ContentValues contenidoValores = new ContentValues();
    contenidoValores.put(dBHelper.NAME, usuario);
    contenidoValores.put(dBHelper.MyPASSWORD, password);
    long id = db.insert(dBHelper.TABLE_NAME, null, contenidoValores);
    return id;
    }
    public String getData(){
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] columns = {dBHelper.UID, dBHelper.NAME, dBHelper.MyPASSWORD};
        Cursor cursor = db.query(dBHelper.TABLE_NAME, columns, null,null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while (cursor.moveToNext()){
            int cid = cursor.getInt(cursor.getColumnIndex(dBHelper.UID));
            String name = cursor.getString(cursor.getColumnIndex(dBHelper.NAME));
            String password = cursor.getString(cursor.getColumnIndex(dBHelper.MyPASSWORD));
            buffer.append(cid +" "+ name +" "+ password + " \n");
        }
        return buffer.toString();
    }
    public int updateName(String oldName, String newName){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contenedorValores = new ContentValues();
        contenedorValores.put(dBHelper.NAME, newName);
        String [] whereArgs = {oldName};
        int count = db.update(dBHelper.TABLE_NAME, contenedorValores, dBHelper.NAME + " = ?",whereArgs);
        return count;
    }
    public int delete(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        String [] whereArgs ={name};
        int count = db.delete(dBHelper.TABLE_NAME, dBHelper.NAME+ " = ?",whereArgs );
        return count;
    }
    static class dBHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "registrosdb"; //nombre de base de datos
        private static final String TABLE_NAME = "usuarios";//nombre de tabla
        private static final int DATABASE_version = 1;//version de Base de datos

        private static final String UID = "_id";//columna 1(Primary Key -clave primaria)
        private static final String NAME="Nombre";//COLUMNA 2
        private static final String MyPASSWORD="Password";//COLUMNA2

        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+ " VARCHAR(255), "+MyPASSWORD+" VARCHAR(255));";
        private static final String DROP_TABLE = "DROP TABLE IF EXIST" + TABLE_NAME;
        private Context context;
        public dBHelper (Context contextRecibido){
            super(contextRecibido, DATABASE_NAME, null, DATABASE_version);
            this.context = contextRecibido;
        }
        //se crea la base de datos
        public void onCreate(SQLiteDatabase db){
          try {
              db.execSQL(CREATE_TABLE);
          }catch(Exception e) {
          Mensaje.aviso(context, "ERROR" + e);

          }
          }
          //se crea la base de datos
          @Override
          public void onUpgrade(SQLiteDatabase db, int viejaVersion, int nuevaVersion){
            try {
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch(Exception e) {
            Mensaje.aviso(context, "ERROR" + e);
            }

            }
        }
    }

