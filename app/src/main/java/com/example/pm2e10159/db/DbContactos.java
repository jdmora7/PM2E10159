package com.example.pm2e10159.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.pm2e10159.entidades.Contactos;

import java.util.ArrayList;

public class DbContactos extends dbHelper {

    Context context;

    public DbContactos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarContacto(String nombre, int telefono, String nota) {
        long id = 0;
        try {
            dbHelper dbHelper = new dbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("nombre", nombre);
            values.put("telefono", telefono);
            values.put("nota", nota);
            id = db.insert(TABLE_CONTACTOS, null, values);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public ArrayList<Contactos> visualizarContactos() {
        dbHelper dbHelper = new dbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ArrayList<Contactos> listaContactos = new ArrayList<>();
        Contactos contacto;
        Cursor cursorContactos = db.rawQuery("SELECT * FROM " + TABLE_CONTACTOS, null);

        if (cursorContactos.moveToFirst()) {
            do {
                contacto = new Contactos();
                contacto.setId(cursorContactos.getInt(0));
                contacto.setNombre(cursorContactos.getString(1));
                contacto.setTelefono(String.valueOf(cursorContactos.getInt(2)));
                contacto.setNota(cursorContactos.getString(3));
                listaContactos.add(contacto);
            } while (cursorContactos.moveToNext());
        }
        cursorContactos.close();
        return listaContactos;
    }

    public void eliminarContacto(int idContacto) {
        try {
            dbHelper dbHelper = new dbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete(TABLE_CONTACTOS, "id=?", new String[]{String.valueOf(idContacto)});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


