package com.example.sqlitedb.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sqlitedb.Model.Pokemon;
import com.example.sqlitedb.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;

    public DatabaseHandler(Context context) {
        super(context, Utils.DATABASE_NAME, null, Utils.DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String sqlCommand = "CREATE TABLE " + Utils.DATABASE_TABLE + "(" +
                Utils.ID + " TEXT PRIMARY KEY," + Utils.NAME + " TEXT," +
                Utils.ELEMENT + " TEXT" +
                ");";

        db.execSQL(sqlCommand);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Utils.DATABASE_TABLE);

        onCreate(db);


    }

    //CRUD

    public void addPokemon(Pokemon pokemon) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues vals = new ContentValues();
        vals.put(Utils.DATABASE_NAME, pokemon.getName());
        vals.put(Utils.ELEMENT, pokemon.getElement());

        db.insert(Utils.DATABASE_TABLE, null, vals);


    }

    public Pokemon getPokemon(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Utils.DATABASE_TABLE, new String[]{Utils.ID, Utils.NAME, Utils.ELEMENT},
                Utils.ID + "=?", new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Pokemon pokemon = new Pokemon();
        pokemon.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(String.valueOf(id)))));
        pokemon.setName(cursor.getString(cursor.getColumnIndex(Utils.NAME)));
        pokemon.setElement(cursor.getString(cursor.getColumnIndex(Utils.ELEMENT)));


        return pokemon;

    }

    public List<Pokemon> getAllPokemon() {

        SQLiteDatabase db = this.getReadableDatabase();

        List<Pokemon> pokemons = new ArrayList<>();


        Cursor cursor = db.rawQuery("SELECT * FROM " + Utils.DATABASE_TABLE, null);

        if (cursor.moveToFirst()) {

            do {

                Pokemon pokemon = new Pokemon();
                pokemon.setId(Integer.parseInt(cursor.getString(0)));
                pokemon.setName(cursor.getString(1));
                pokemon.setElement(cursor.getString(2));

                pokemons.add(pokemon);

            } while (cursor.moveToNext());

        }

        return pokemons;

    }

    public int updatePokemoon(Pokemon pokemon) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Utils.NAME, pokemon.getName());
        values.put(Utils.ELEMENT, pokemon.getElement());

        return db.update(Utils.DATABASE_TABLE, values, Utils.ID + "=?", new String[]{String.valueOf(pokemon.getId())});


    }

    public void deletePokemon(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(Utils.DATABASE_TABLE, Utils.ID + "=?", new String[]{String.valueOf(id)});

        db.close();

    }

    public int getPokeCount(){
        String str = "SELECT * FROM " + Utils.DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(str,null);

        return cursor.getCount();

    }
}


