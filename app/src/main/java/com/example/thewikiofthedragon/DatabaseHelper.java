package com.example.thewikiofthedragon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nombre de la base de datos y versión
    private static final String DATABASE_NAME = "dragonDatabase.db";
    private static final int DATABASE_VERSION = 2;

    // Tabla de personajes
    private static final String TABLE_CHARACTERS = "characters";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE = "imageResId";
    private static final String COLUMN_BIOGRAPHY = "biography";

    // Tabla de usuarios
    private static final String TABLE_USERS = "users";
    private static final String USER_ID = "id";
    private static final String USER_NAME = "nombre";
    private static final String USER_EMAIL = "email";
    private static final String USER_PASSWORD = "contrasena";
    private static final String USER_BANDO = "bando_favorito";
    private static final String USER_CASA = "casa_favorita";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de personajes
        String CREATE_TABLE_CHARACTERS = "CREATE TABLE " + TABLE_CHARACTERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_IMAGE + " INTEGER NOT NULL," +
                COLUMN_BIOGRAPHY + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_CHARACTERS);

        // Crear tabla de usuarios, permitiendo que el campo email sea opcional
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_NAME + " TEXT NOT NULL," +
                USER_EMAIL + " TEXT," +  // Permitir que el email sea opcional
                USER_PASSWORD + " TEXT NOT NULL," +
                USER_BANDO + " TEXT NOT NULL," +
                USER_CASA + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_CHARACTERS + " ADD COLUMN " + COLUMN_BIOGRAPHY + " TEXT NOT NULL DEFAULT ''");
        }
    }

    // Método para validar el login del usuario
    public boolean validarLogin(String nickname, String password, String bando) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Convertir a minúsculas y quitar espacios en blanco para evitar problemas de comparación
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE LOWER(" + USER_NAME + ") = ? AND " +
                "LOWER(" + USER_PASSWORD + ") = ? AND LOWER(" + USER_BANDO + ") = ?";

        Cursor cursor = db.rawQuery(query,
                new String[]{nickname.toLowerCase().trim(), password.toLowerCase().trim(), bando.toLowerCase().trim()});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }


    // Método para agregar un usuario con bando y casa favorita
    public long agregarUsuario(String nombre, String email, String contrasena, String bando, String casa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(USER_NAME, nombre.trim().toLowerCase());
        values.put(USER_EMAIL, email.trim().toLowerCase());
        values.put(USER_PASSWORD, contrasena.trim().toLowerCase());
        values.put(USER_BANDO, bando.trim().toLowerCase());
        values.put(USER_CASA, casa.trim().toLowerCase());

        long id = -1;
        try {
            id = db.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            e.printStackTrace();  // Imprimir error en los logs
        } finally {
            db.close();
        }
        return id;
    }


    // Método para modificar un usuario
    public int modificarUsuario(int id, String nombre, String email, String contrasena, String bando, String casa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_NAME, nombre);
        values.put(USER_EMAIL, email);
        values.put(USER_PASSWORD, contrasena);
        values.put(USER_BANDO, bando);
        values.put(USER_CASA, casa);

        return db.update(TABLE_USERS, values, USER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    // Método para eliminar un usuario por su nickname
    public boolean eliminarUsuarioPorNickname(String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Eliminar el usuario donde el nickname coincida
        int resultado = db.delete(TABLE_USERS, USER_NAME + " = ?", new String[]{nickname.toLowerCase().trim()});

        db.close();
        // Verificar si se eliminó al menos una fila
        return resultado > 0;
    }


    // Método para obtener todos los usuarios
    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarioList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario(
                        cursor.getInt(cursor.getColumnIndexOrThrow(USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_PASSWORD)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_BANDO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(USER_CASA))
                );
                usuarioList.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usuarioList;
    }

    // Método para agregar un personaje con su biografía
    public long addCharacter(String name, int imageResId, String biography) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_IMAGE, imageResId);
        values.put(COLUMN_BIOGRAPHY, biography);

        long id = db.insert(TABLE_CHARACTERS, null, values);
        return id;
    }

    // Método para obtener todos los personajes
    public List<Character> getAllCharacters() {
        List<Character> characterList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS, null);

        if (cursor.moveToFirst()) {
            do {
                Character character = new Character(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BIOGRAPHY))
                );
                characterList.add(character);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return characterList;
    }
}
