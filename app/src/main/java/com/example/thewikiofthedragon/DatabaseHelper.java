package com.example.thewikiofthedragon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dragonDatabase.db";
    private static final int DATABASE_VERSION = 2;

    // Tabla de personajes
    private static final String TABLE_CHARACTERS = "characters";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE = "imageResId";
    private static final String COLUMN_BIOGRAPHY = "biography";

    // Tabla de dragones
    private static final String TABLE_DRAGONS = "dragons";
    private static final String COLUMN_DRAGON_ID = "id";
    private static final String COLUMN_DRAGON_NAME = "name";
    private static final String COLUMN_DRAGON_IMAGE = "imageResId";
    private static final String COLUMN_DRAGON_BIOGRAPHY = "biography";
    private static final String COLUMN_DRAGON_FACTION = "faction";  // 'Negro' o 'Verde'

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

        // Crear tabla de dragones
        String CREATE_TABLE_DRAGONS = "CREATE TABLE " + TABLE_DRAGONS + " (" +
                COLUMN_DRAGON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_DRAGON_NAME + " TEXT NOT NULL," +
                COLUMN_DRAGON_IMAGE + " INTEGER NOT NULL," +
                COLUMN_DRAGON_BIOGRAPHY + " TEXT NOT NULL," +
                COLUMN_DRAGON_FACTION + " TEXT NOT NULL)";  // Bando de los dragones
        db.execSQL(CREATE_TABLE_DRAGONS);

        // Crear tabla de usuarios
        String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + " (" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_NAME + " TEXT NOT NULL," +
                USER_EMAIL + " TEXT," +
                USER_PASSWORD + " TEXT NOT NULL," +
                USER_BANDO + " TEXT NOT NULL," +
                USER_CASA + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_USERS);

        // Insertar personajes y dragones iniciales
        insertInitialCharacters(db);
        insertInitialDragons(db);

        Log.d("DatabaseHelper", "Tablas y datos iniciales creados.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_CHARACTERS + " ADD COLUMN " + COLUMN_BIOGRAPHY + " TEXT NOT NULL DEFAULT ''");
        }
    }

    // Método para agregar personajes iniciales
    private void insertInitialCharacters(SQLiteDatabase db) {
        addCharacter(db, "Rhaenyra Targaryen", R.drawable.rhae, "Rhaenyra Targaryen fue la primogénita del rey Viserys I y la heredera designada al Trono de Hierro, un hecho que desató la Guerra Civil Targaryen conocida como la *Danza de los Dragones*. A pesar de su derecho legítimo, la corte y las costumbres de Westeros prefirieron a su medio hermano Aegon II, lo que llevó a un sangriento conflicto entre los 'Negros' (partidarios de Rhaenyra) y los 'Verdes' (seguidores de Aegon). Jinete del dragón Syrax, su muerte fue brutal, devorada por el dragón Sunfyre de Aegon II.");
        addCharacter(db, "Daemon Targaryen", R.drawable.daemon_targaryen, "Daemon Targaryen fue un hombre ambicioso y peligroso. Fue conocido por su papel en la *Danza de los Dragones*, apoyando la causa de su esposa y sobrina, Rhaenyra. Antes del conflicto, Daemon fue comandante de la Guardia de la Ciudad de Desembarco del Rey. Era jinete del dragón Caraxes, conocido como el Dragón Rojo Sangre, y su figura sigue envuelta en un halo de misterio y leyenda.");
        addCharacter(db, "Jacaerys Velaryon", R.drawable.jaca, "Jacaerys Velaryon, hijo mayor de Rhaenyra, montaba a su dragón, Vermax, y fue uno de los principales defensores de su madre durante la *Danza de los Dragones*. Su muerte en la guerra fue una de las muchas tragedias que marcaron el destino de la Casa Targaryen.");
        addCharacter(db, "Lucerys Velaryon", R.drawable.luce, "Lucerys Velaryon, segundo hijo de Rhaenyra, fue asesinado por Aemond Targaryen en una batalla aérea sobre los Peldaños de Piedra durante la *Danza de los Dragones*. Su muerte fue uno de los eventos más dolorosos de la guerra civil.");
        addCharacter(db, "Joffrey Velaryon", R.drawable.joff, "Joffrey Velaryon fue el hijo menor de Rhaenyra Targaryen. A pesar de su juventud, se vio atrapado en la tragedia de la guerra civil conocida como la *Danza de los Dragones*. Montaba al dragón Tyraxes.");
        addCharacter(db, "Baela Targaryen", R.drawable.baela, "Baela Targaryen, hija de Daemon Targaryen, montaba al dragón Moondancer y fue una partidaria feroz de Rhaenyra durante la *Danza de los Dragones*. Participó activamente en la guerra, destacándose por su valentía en combate.");
        addCharacter(db, "Rhaena Targaryen", R.drawable.rhaena, "Rhaena Targaryen, hermana gemela de Baela, adquirió su dragón Morning durante la *Danza de los Dragones*. A pesar de no montar un dragón al principio de la guerra, su lealtad y apoyo a su familia fueron fundamentales.");
        addCharacter(db, "Rhaenys Targaryen", R.drawable.rhaenys, "Rhaenys Targaryen, conocida como la Reina que Nunca Fue, era la esposa de Lord Corlys Velaryon. Aunque fue pasada por alto en favor de Viserys I, Rhaenys fue una pieza clave en la *Danza de los Dragones*, montando a su dragón Meleys, la Reina Roja.");
    }

    // Método para agregar dragones iniciales
    private void insertInitialDragons(SQLiteDatabase db) {
        addDragon(db, "Syrax", R.drawable.syrax, "Dragón montado por Rhaenyra Targaryen", "Negro");
        addDragon(db, "Caraxes", R.drawable.caraxes, "Dragón montado por Daemon Targaryen", "Negro");
        addDragon(db, "Vermax", R.drawable.vermax, "Dragón montado por Jacaerys Velaryon", "Negro");
        addDragon(db, "Moondancer", R.drawable.moondancer, "Dragón montado por Baela Targaryen", "Negro");
        addDragon(db, "Meleys", R.drawable.meleys, "Dragón montado por Rhaenys Targaryen", "Negro");
    }

    // Método para agregar un personaje con su biografía
    private void addCharacter(SQLiteDatabase db, String name, int imageResId, String biography) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_IMAGE, imageResId);
        values.put(COLUMN_BIOGRAPHY, biography);
        long id = db.insert(TABLE_CHARACTERS, null, values);
        if (id == -1) {
            Log.e("DatabaseInsert", "Error al insertar el personaje: " + name);
        } else {
            Log.d("DatabaseInsert", "Personaje insertado: " + name);
        }
    }

    // Método para agregar un dragón con su biografía
    private void addDragon(SQLiteDatabase db, String name, int imageResId, String biography, String faction) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_DRAGON_NAME, name);
        values.put(COLUMN_DRAGON_IMAGE, imageResId);
        values.put(COLUMN_DRAGON_BIOGRAPHY, biography);
        values.put(COLUMN_DRAGON_FACTION, faction);
        long id = db.insert(TABLE_DRAGONS, null, values);
        if (id == -1) {
            Log.e("DatabaseInsert", "Error al insertar el dragón: " + name);
        } else {
            Log.d("DatabaseInsert", "Dragón insertado: " + name);
        }
    }

    // Método para obtener todos los dragones del Bando Negro
    public List<Dragon> getAllBlackDragons() {
        List<Dragon> dragonList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DRAGONS + " WHERE " + COLUMN_DRAGON_FACTION + " = ?", new String[]{"Negro"});

        if (cursor.moveToFirst()) {
            do {
                Dragon dragon = new Dragon(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRAGON_NAME)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_DRAGON_IMAGE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRAGON_BIOGRAPHY))
                );
                dragonList.add(dragon);
            } while (cursor.moveToNext());
        } else {
            Log.d("DatabaseDebug", "No se encontraron dragones del Bando Negro en la base de datos.");
        }
        cursor.close();
        return dragonList;
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
        } else {
            Log.d("DatabaseDebug", "No se encontraron personajes en la base de datos.");
        }
        cursor.close();
        return characterList;
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

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Método para validar el login del usuario
    public boolean validarLogin(String nickname, String password, String bando) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USERS + " WHERE LOWER(" + USER_NAME + ") = ? AND LOWER(" + USER_PASSWORD + ") = ? AND LOWER(" + USER_BANDO + ") = ?";
        Cursor cursor = db.rawQuery(query, new String[]{nickname.toLowerCase().trim(), password.toLowerCase().trim(), bando.toLowerCase().trim()});
        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    // Método para eliminar un usuario por su nickname
    public boolean eliminarUsuarioPorNickname(String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete(TABLE_USERS, USER_NAME + " = ?", new String[]{nickname.toLowerCase().trim()});
        db.close();
        return resultado > 0;
    }
}
