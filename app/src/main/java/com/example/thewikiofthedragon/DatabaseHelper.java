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
    private static final int DATABASE_VERSION = 3; // Incrementa la versión para activar onUpgrade

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
                COLUMN_DRAGON_FACTION + " TEXT NOT NULL)";
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
        if (oldVersion < newVersion) {
            // Eliminar registros existentes en la tabla de dragones y volver a insertar los datos iniciales
            db.execSQL("DELETE FROM " + TABLE_DRAGONS);
            insertInitialDragons(db);

            Log.d("DatabaseHelper", "Datos de dragones actualizados en la base de datos.");
        }
    }

    // Método para agregar personajes iniciales
    private void insertInitialCharacters(SQLiteDatabase db) {
        addCharacter(db, "Rhaenyra Targaryen", R.drawable.rhae,
                "Rhaenyra Targaryen fue la primogénita del rey Viserys I y su única hija de su primer matrimonio. Desde joven, Rhaenyra fue educada como la heredera al Trono de Hierro, rompiendo con la tradición que favorecía a los varones. Conocida como la 'Alegría del Reino' en su juventud, fue amada por la corte y adorada por su padre, quien luchó para asegurar su derecho como su sucesora. Rhaenyra era ambiciosa, decidida y ferozmente protectora de su derecho al trono. Sin embargo, tras la muerte de su padre, su medio hermano Aegon II usurpó el trono, desencadenando la brutal guerra civil conocida como la Danza de los Dragones. Rhaenyra montaba a su dragón, Syrax, y lideró a sus seguidores, conocidos como los 'Negros', en un intento por recuperar su trono. A lo largo de la guerra, mostró una mezcla de valor y crueldad, perdiendo seres queridos y enfrentándose a traiciones que la marcaron profundamente. Su trágico final, siendo devorada por el dragón Sunfyre de Aegon II, simbolizó la caída de una figura poderosa, ambiciosa y profundamente trágica en la historia de los Targaryen.");

        addCharacter(db, "Daemon Targaryen", R.drawable.daemon_targaryen,
                "Daemon Targaryen, hermano menor de Viserys I y esposo de Rhaenyra, fue una figura compleja y ambiciosa. Conocido por su carácter impetuoso y su habilidad con la espada, Daemon fue un hombre que vivió bajo sus propias reglas. Fue Comandante de la Guardia de la Ciudad y fundó la organización de los Capas Doradas en Desembarco del Rey, ganándose tanto enemigos como seguidores leales. Era temido por su imprevisibilidad y su deseo de poder, y nunca abandonó su ambición de sentarse en el Trono de Hierro. Daemon montaba a Caraxes, un dragón feroz que complementaba su propia naturaleza combativa. Durante la Danza de los Dragones, Daemon fue una figura clave en la causa de los 'Negros', liderando campañas y enfrentándose a sus enemigos con brutalidad. Su enfrentamiento final contra Aemond Targaryen, donde ambos perecieron junto a sus dragones en Harrenhal, es recordado como uno de los momentos más legendarios de la guerra. Su vida y su muerte definieron la naturaleza conflictiva y ambiciosa de los Targaryen.");

        addCharacter(db, "Jacaerys Velaryon", R.drawable.jaca,
                "Jacaerys Velaryon fue el hijo mayor de Rhaenyra Targaryen y Laenor Velaryon, aunque algunos rumores en la corte ponían en duda su paternidad. Fiel a su madre y a su causa, Jacaerys, conocido como Jace, fue un líder joven y prometedor. Montaba a Vermax, su dragón, con el cual participó en importantes misiones diplomáticas para asegurar alianzas con casas poderosas, como los Stark en el Norte. Jacaerys mostró valentía y determinación en la guerra, y era un símbolo de esperanza para los seguidores de Rhaenyra. Sin embargo, su vida fue truncada durante la guerra en una misión peligrosa. Su muerte fue una de las muchas tragedias que marcaron la Danza de los Dragones, recordándolo como un hijo leal y un líder joven que no alcanzó a ver la paz por la que luchaba.");

        addCharacter(db, "Lucerys Velaryon", R.drawable.luce,
                "Lucerys Velaryon, segundo hijo de Rhaenyra Targaryen, fue un joven valiente pero también vulnerable. Montaba a Arrax, su dragón, y fue enviado por su madre en una misión diplomática para asegurar la lealtad de los Baratheon en Bastión de Tormentas. Sin embargo, su misión terminó trágicamente cuando fue interceptado por Aemond Targaryen y su dragón, Vhagar. En un combate desigual, Lucerys y Arrax fueron derribados y asesinados en una tormenta. Su muerte marcó el inicio de una serie de venganzas y represalias que escalaron la violencia de la Danza de los Dragones, dejando a Rhaenyra devastada y llenando de odio a su facción. La muerte de Lucerys fue un catalizador que alimentó el conflicto y el rencor en ambos bandos.");

        addCharacter(db, "Joffrey Velaryon", R.drawable.joff,
                "Joffrey Velaryon fue el hijo menor de Rhaenyra Targaryen y hermano de Jacaerys y Lucerys. Aunque era el menor de los hermanos, Joffrey mostró una determinación feroz para proteger a su madre y a su familia. Montaba al dragón Tyraxes y, aunque no era un combatiente experimentado, estaba decidido a ayudar en la lucha de los 'Negros'. En uno de los episodios más trágicos de la Danza de los Dragones, Joffrey intentó volar hacia Rocadragón para ayudar a su madre, pero su inexperiencia y la fuerza de los vientos resultaron fatales. Cayó desde su dragón en una tormenta, dejando a Rhaenyra una vez más devastada por la pérdida de otro hijo. La muerte de Joffrey es recordada como un acto de valentía y tragedia, resaltando el costo de la guerra en la familia Targaryen.");

        addCharacter(db, "Baela Targaryen", R.drawable.baela,
                "Baela Targaryen, hija de Daemon Targaryen y Lady Laena Velaryon, era conocida por su carácter valiente y decidido. Montaba a Moondancer, un dragón pequeño pero rápido y ágil. Baela fue una firme defensora de Rhaenyra durante la Danza de los Dragones y mostró gran coraje al enfrentarse al dragón Sunfyre, montado por Aegon II, en una feroz batalla en la que Moondancer perdió la vida. Baela resultó herida, pero sobrevivió a la guerra, mostrando la misma fortaleza que su padre. Su valentía y determinación hicieron de ella una figura respetada y una heroína en la causa de los 'Negros'. Baela continuó siendo una presencia fuerte en la historia Targaryen después de la guerra, y su legado es recordado con respeto.");

        addCharacter(db, "Rhaena Targaryen", R.drawable.rhaena,
                "Rhaena Targaryen, hermana gemela de Baela, era una persona tranquila y leal, aunque no tenía un dragón al comienzo de la Danza de los Dragones. A lo largo de la guerra, Rhaena mostró su apoyo inquebrantable a su familia y eventualmente adquirió a Morning, un dragón joven que simbolizaba su crecimiento y fortaleza en tiempos difíciles. Aunque Rhaena no fue una combatiente activa en la guerra, su presencia fue una fuente de consuelo y apoyo para su familia. Al igual que su hermana, sobrevivió a la guerra y continuó desempeñando un papel importante en la dinastía Targaryen. Rhaena es recordada por su lealtad, su temple y su amor por su familia en medio de tiempos de gran conflicto.");

        addCharacter(db, "Rhaenys Targaryen", R.drawable.rhaenys,
                "Rhaenys Targaryen, conocida como la 'Reina que Nunca Fue', era la esposa de Lord Corlys Velaryon y una de las primeras mujeres en reclamar su derecho al Trono de Hierro. A pesar de su reclamo legítimo, fue pasada por alto en favor de Viserys I debido a su género. Montaba a Meleys, la 'Reina Roja', un dragón tan imponente como ella misma. Durante la Danza de los Dragones, Rhaenys luchó junto a Rhaenyra y sus hijos, defendiendo la causa de los 'Negros'. En una de las batallas más épicas de la guerra, Rhaenys y Meleys se enfrentaron a Vhagar y Sunfyre en una batalla que selló su destino. Rhaenys murió en combate, pero su legado como una mujer valiente y orgullosa que luchó por sus derechos y los de su familia perdura en la historia Targaryen. Su muerte simbolizó la tragedia de una mujer que, a pesar de su fuerza y legitimidad, fue negada su derecho al poder.");
    }


    // Método para agregar dragones iniciales
    private void insertInitialDragons(SQLiteDatabase db) {
        addDragon(db, "Syrax", R.drawable.syrax,
                "Syrax es el dragón personal de Rhaenyra Targaryen, llamado así en honor a una diosa valyria. Este dragón de escamas doradas fue domesticado desde una edad temprana, lo cual lo hizo menos salvaje y más controlable que otros dragones. A lo largo de su vida, Syrax ha sido un símbolo de poder y autoridad de Rhaenyra. Durante la Danza de los Dragones, se mantuvo en Rocadragón la mayor parte del tiempo, pero fue desplegado en varias ocasiones importantes. Aunque su carácter era menos agresivo, mostraba gran ferocidad cuando estaba junto a su jinete en combate. En uno de los momentos más críticos de la guerra, Syrax defendió Desembarco del Rey junto a su jinete. Lamentablemente, Syrax encontró un trágico final en la guerra, sacrificándose en una de las batallas más feroces, dejando a Rhaenyra devastada y marcando el ocaso de su lucha por el Trono de Hierro.",
                "Negro");

        addDragon(db, "Caraxes", R.drawable.caraxes,
                "Caraxes, también conocido como el 'Wyrm Sangriento', era uno de los dragones más temidos de su tiempo, conocido por su carácter feroz y su peculiar apariencia alargada. Con sus escamas rojas y una envergadura imponente, Caraxes era una bestia de combate, montado por Daemon Targaryen, el príncipe rebelde. Caraxes fue originalmente montado por Aemon Targaryen, pero tras su muerte, Daemon se convirtió en su jinete y estableció un vínculo poderoso con él. Durante la Danza de los Dragones, Caraxes luchó en numerosas batallas al lado de Daemon, y su reputación como un dragón brutal y agresivo creció. Su participación en la guerra incluyó enfrentamientos contra otros dragones, y en el clímax de la guerra, Caraxes se enfrentó a Vhagar en una lucha épica sobre Harrenhal. Ambos dragones y sus jinetes perecieron en la batalla, dejando un legado de destrucción y violencia que definió la guerra.",
                "Negro");

        addDragon(db, "Vermax", R.drawable.vermax,
                "Vermax fue el dragón montado por Jacaerys Velaryon, el hijo mayor de Rhaenyra Targaryen. Aunque relativamente joven, Vermax poseía una lealtad inquebrantable hacia su jinete y demostró gran destreza en el vuelo. Durante la Danza de los Dragones, Jacaerys y Vermax realizaron importantes misiones diplomáticas, volando hasta el Norte para asegurar alianzas con casas como los Stark de Invernalia. Vermax también participó en batallas claves, demostrando gran valentía en combate a pesar de su menor tamaño en comparación con otros dragones mayores. En su última misión, Vermax y Jacaerys se enfrentaron a un enemigo formidable, lo cual resultó en su trágica muerte. Su sacrificio marcó un momento doloroso en la guerra, y su lealtad a su jinete sigue siendo recordada como un símbolo de la fidelidad de la casa Targaryen.",
                "Negro");

        addDragon(db, "Moondancer", R.drawable.moondancer,
                "Moondancer era un dragón joven, pequeño y extremadamente ágil, montado por Baela Targaryen. Sus escamas de un tono verde pálido brillaban a la luz de la luna, de ahí su nombre. Aunque Moondancer no era tan grande como otros dragones, poseía una velocidad y maniobrabilidad impresionantes, lo cual lo convertía en un oponente difícil de alcanzar en combate. Durante la Danza de los Dragones, Baela y Moondancer lucharon ferozmente en defensa de su familia y de la causa de los 'Negros'. Uno de los momentos más destacados fue la batalla contra Sunfyre, el dragón de Aegon II. A pesar de su pequeño tamaño, Moondancer mostró gran valentía y resistencia, enfrentando al más experimentado Sunfyre en una feroz batalla. Aunque Moondancer pereció en combate, su valentía y lealtad a Baela dejaron una huella imborrable en la historia de la guerra.",
                "Negro");

        addDragon(db, "Meleys", R.drawable.meleys,
                "Meleys, conocida como la 'Reina Roja', era uno de los dragones más antiguos y poderosos de su tiempo. Con escamas de un rojo vibrante y una velocidad asombrosa, Meleys fue montada por Rhaenys Targaryen, quien también era conocida como la Reina que Nunca Fue. A pesar de su edad, Meleys era rápida, fuerte y temida en el campo de batalla. Durante la Danza de los Dragones, Meleys y Rhaenys participaron en varias batallas, y juntas defendieron con valentía la causa de Rhaenyra. En uno de los enfrentamientos más épicos de la guerra, Meleys se enfrentó a Vhagar y Sunfyre, los dragones de Aemond y Aegon II, en una batalla que costó la vida tanto a ella como a su jinete. La caída de Meleys marcó un momento decisivo en la guerra, y su pérdida fue sentida profundamente por los partidarios de Rhaenyra. La 'Reina Roja' dejó un legado de fuerza, valentía y lealtad que perdurará en la memoria de la casa Targaryen.",
                "Negro");
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
