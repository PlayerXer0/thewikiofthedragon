package com.example.thewikiofthedragon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "dragonDatabase.db";
    private static final int DATABASE_VERSION = 5; // Incrementa la versión para activar onUpgrade
    private FirebaseFirestore firestore;

    // Tabla de personajes
    private static final String TABLE_CHARACTERS = "characters";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_IMAGE = "imageResId";
    private static final String COLUMN_BIOGRAPHY = "biography";
    private static final String COLUMN_FACTION = "faction";

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
        firestore = FirebaseFirestore.getInstance(); // Inicializa Firestore
    }

    public void syncCharacterToFirebase(Character character) {
        Map<String, Object> characterData = new HashMap<>();
        characterData.put("name", character.getName());
        characterData.put("imageResId", character.getImageResId());
        characterData.put("biography", character.getBiography());

        firestore.collection("characters").document(character.getName())
                .set(characterData)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseSync", "Personaje sincronizado en Firebase"))
                .addOnFailureListener(e -> Log.e("FirebaseSync", "Error al sincronizar personaje", e));
    }

    public void addCharacter(Character character) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, character.getName());
        values.put(COLUMN_IMAGE, character.getImageResId());
        values.put(COLUMN_BIOGRAPHY, character.getBiography());

        long id = db.insert(TABLE_CHARACTERS, null, values);
        if (id == -1) {
            Log.e("DatabaseInsert", "Error al insertar el personaje: " + character.getName());
        } else {
            Log.d("DatabaseInsert", "Personaje insertado: " + character.getName());
        }
        db.close();
    }

    public void syncUserToFirebase(String nombre, String email, String contrasena, String bando, String casa) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("nombre", nombre);
        userData.put("email", email);
        userData.put("contrasena", contrasena);
        userData.put("bando_favorito", bando);
        userData.put("casa_favorita", casa);

        firestore.collection("users").document(nombre)
                .set(userData)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseSync", "Usuario sincronizado en Firebase"))
                .addOnFailureListener(e -> Log.e("FirebaseSync", "Error al sincronizar usuario", e));
    }

    //personajes bando verde
    public List<Character> getAllGreenCharacters() {
        List<Character> characterList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS + " WHERE faction = ?", new String[]{"Verde"});

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
            Log.d("DatabaseDebug", "No se encontraron personajes del Bando Verde en la base de datos.");
        }
        cursor.close();
        return characterList;
    }

    //personajes bando negro
    public List<Character> getAllBlackCharacters() {
        List<Character> characterList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CHARACTERS + " WHERE " + COLUMN_FACTION + " = ?", new String[]{"Negro"});

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
            Log.d("DatabaseDebug", "No se encontraron personajes del Bando Negro en la base de datos.");
        }
        cursor.close();
        return characterList;
    }



    // Método para obtener todos los dragones del Bando Verde
    public List<Dragon> getAllGreenDragons() {
        List<Dragon> dragonList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DRAGONS + " WHERE " + COLUMN_DRAGON_FACTION + " = ?", new String[]{"Verde"});

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
            Log.d("DatabaseDebug", "No se encontraron dragones del Bando Verde en la base de datos.");
        }
        cursor.close();
        return dragonList;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla de personajes con la columna 'faction'
        String CREATE_TABLE_CHARACTERS = "CREATE TABLE " + TABLE_CHARACTERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT NOT NULL," +
                COLUMN_IMAGE + " INTEGER NOT NULL," +
                COLUMN_BIOGRAPHY + " TEXT NOT NULL," +
                COLUMN_FACTION + " TEXT NOT NULL)"; // Agregar columna 'faction' aquí
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
        insertInitialGreenCharacters(db);
        insertInitialDragons(db);
        insertInitialGreenDragons(db);

        Log.d("DatabaseHelper", "Tablas y datos iniciales creados.");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            // Eliminar las tablas y recrearlas con la nueva estructura
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHARACTERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRAGONS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db); // Volver a llamar a onCreate para recrear las tablas
            Log.d("DatabaseHelper", "Tablas actualizadas y datos iniciales reinicializados.");
        }
    }


    //BANDO NEGRO
    private void insertInitialCharacters(SQLiteDatabase db) {
        addCharacter(db, "Rhaenyra Targaryen", R.drawable.rhae,
                "Rhaenyra Targaryen fue la primogénita del rey Viserys I y su única hija de su primer matrimonio. Desde joven, Rhaenyra fue educada como la heredera al Trono de Hierro, rompiendo con la tradición que favorecía a los varones. Conocida como la 'Alegría del Reino' en su juventud, fue amada por la corte y adorada por su padre, quien luchó para asegurar su derecho como su sucesora. Rhaenyra era ambiciosa, decidida y ferozmente protectora de su derecho al trono. Sin embargo, tras la muerte de su padre, su medio hermano Aegon II usurpó el trono, desencadenando la brutal guerra civil conocida como la Danza de los Dragones. Rhaenyra montaba a su dragón, Syrax, y lideró a sus seguidores, conocidos como los 'Negros', en un intento por recuperar su trono. A lo largo de la guerra, mostró una mezcla de valor y crueldad, perdiendo seres queridos y enfrentándose a traiciones que la marcaron profundamente. Su trágico final, siendo devorada por el dragón Sunfyre de Aegon II, simbolizó la caída de una figura poderosa, ambiciosa y profundamente trágica en la historia de los Targaryen.",
                "Negro");

        addCharacter(db, "Daemon Targaryen", R.drawable.daemon_targaryen,
                "Daemon Targaryen, hermano menor de Viserys I y esposo de Rhaenyra, fue una figura compleja y ambiciosa. Conocido por su carácter impetuoso y su habilidad con la espada, Daemon fue un hombre que vivió bajo sus propias reglas. Fue Comandante de la Guardia de la Ciudad y fundó la organización de los Capas Doradas en Desembarco del Rey, ganándose tanto enemigos como seguidores leales. Era temido por su imprevisibilidad y su deseo de poder, y nunca abandonó su ambición de sentarse en el Trono de Hierro. Daemon montaba a Caraxes, un dragón feroz que complementaba su propia naturaleza combativa. Durante la Danza de los Dragones, Daemon fue una figura clave en la causa de los 'Negros', liderando campañas y enfrentándose a sus enemigos con brutalidad. Su enfrentamiento final contra Aemond Targaryen, donde ambos perecieron junto a sus dragones en Harrenhal, es recordado como uno de los momentos más legendarios de la guerra. Su vida y su muerte definieron la naturaleza conflictiva y ambiciosa de los Targaryen.",
                "Negro");

        addCharacter(db, "Jacaerys Velaryon", R.drawable.jaca,
                "Jacaerys Velaryon fue el hijo mayor de Rhaenyra Targaryen y Laenor Velaryon, aunque algunos rumores en la corte ponían en duda su paternidad. Fiel a su madre y a su causa, Jacaerys, conocido como Jace, fue un líder joven y prometedor. Montaba a Vermax, su dragón, con el cual participó en importantes misiones diplomáticas para asegurar alianzas con casas poderosas, como los Stark en el Norte. Jacaerys mostró valentía y determinación en la guerra, y era un símbolo de esperanza para los seguidores de Rhaenyra. Sin embargo, su vida fue truncada durante la guerra en una misión peligrosa. Su muerte fue una de las muchas tragedias que marcaron la Danza de los Dragones, recordándolo como un hijo leal y un líder joven que no alcanzó a ver la paz por la que luchaba.",
                "Negro");

        addCharacter(db, "Lucerys Velaryon", R.drawable.luce,
                "Lucerys Velaryon, segundo hijo de Rhaenyra Targaryen, fue un joven valiente pero también vulnerable. Montaba a Arrax, su dragón, y fue enviado por su madre en una misión diplomática para asegurar la lealtad de los Baratheon en Bastión de Tormentas. Sin embargo, su misión terminó trágicamente cuando fue interceptado por Aemond Targaryen y su dragón, Vhagar. En un combate desigual, Lucerys y Arrax fueron derribados y asesinados en una tormenta. Su muerte marcó el inicio de una serie de venganzas y represalias que escalaron la violencia de la Danza de los Dragones, dejando a Rhaenyra devastada y llenando de odio a su facción. La muerte de Lucerys fue un catalizador que alimentó el conflicto y el rencor en ambos bandos.",
                "Negro");

        addCharacter(db, "Joffrey Velaryon", R.drawable.joff,
                "Joffrey Velaryon fue el hijo menor de Rhaenyra Targaryen y hermano de Jacaerys y Lucerys. Aunque era el menor de los hermanos, Joffrey mostró una determinación feroz para proteger a su madre y a su familia. Montaba al dragón Tyraxes y, aunque no era un combatiente experimentado, estaba decidido a ayudar en la lucha de los 'Negros'. En uno de los episodios más trágicos de la Danza de los Dragones, Joffrey intentó volar hacia Rocadragón para ayudar a su madre, pero su inexperiencia y la fuerza de los vientos resultaron fatales. Cayó desde su dragón en una tormenta, dejando a Rhaenyra una vez más devastada por la pérdida de otro hijo. La muerte de Joffrey es recordada como un acto de valentía y tragedia, resaltando el costo de la guerra en la familia Targaryen.",
                "Negro");

        addCharacter(db, "Baela Targaryen", R.drawable.baela,
                "Baela Targaryen, hija de Daemon Targaryen y Lady Laena Velaryon, era conocida por su carácter valiente y decidido. Montaba a Moondancer, un dragón pequeño pero rápido y ágil. Baela fue una firme defensora de Rhaenyra durante la Danza de los Dragones y mostró gran coraje al enfrentarse al dragón Sunfyre, montado por Aegon II, en una feroz batalla en la que Moondancer perdió la vida. Baela resultó herida, pero sobrevivió a la guerra, mostrando la misma fortaleza que su padre. Su valentía y determinación hicieron de ella una figura respetada y una heroína en la causa de los 'Negros'. Baela continuó siendo una presencia fuerte en la historia Targaryen después de la guerra, y su legado es recordado con respeto.",
                "Negro");

        addCharacter(db, "Rhaena Targaryen", R.drawable.rhaena,
                "Rhaena Targaryen, hermana gemela de Baela, era una persona tranquila y leal, aunque no tenía un dragón al comienzo de la Danza de los Dragones. A lo largo de la guerra, Rhaena mostró su apoyo inquebrantable a su familia y eventualmente adquirió a Morning, un dragón joven que simbolizaba su crecimiento y fortaleza en tiempos difíciles. Aunque Rhaena no fue una combatiente activa en la guerra, su presencia fue una fuente de consuelo y apoyo para su familia. Al igual que su hermana, sobrevivió a la guerra y continuó desempeñando un papel importante en la dinastía Targaryen. Rhaena es recordada por su lealtad, su temple y su amor por su familia en medio de tiempos de gran conflicto.",
                "Negro");

        addCharacter(db, "Rhaenys Targaryen", R.drawable.rhaenys,
                "Rhaenys Targaryen, conocida como la 'Reina que Nunca Fue', era la esposa de Lord Corlys Velaryon y una de las primeras mujeres en reclamar su derecho al Trono de Hierro. A pesar de su reclamo legítimo, fue pasada por alto en favor de Viserys I debido a su género. Montaba a Meleys, la 'Reina Roja', un dragón tan imponente como ella misma. Durante la Danza de los Dragones, Rhaenys luchó junto a Rhaenyra y sus hijos, defendiendo la causa de los 'Negros'. En una de las batallas más épicas de la guerra, Rhaenys y Meleys se enfrentaron a Vhagar y Sunfyre en una batalla que selló su destino. Rhaenys murió en combate, pero su legado como una mujer valiente y orgullosa que luchó por sus derechos y los de su familia perdura en la historia Targaryen. Su muerte simbolizó la tragedia de una mujer que, a pesar de su fuerza y legitimidad, fue negada su derecho al poder.",
                "Negro");
    }

    //BANDO VERDE
    private void insertInitialGreenCharacters(SQLiteDatabase db) {
        addCharacter(db, "Aegon II Targaryen", R.drawable.aegon_targaryen,
                "Aegon II Targaryen, hijo de Viserys I y Alicent Hightower, fue coronado como rey por el bando verde en desafío a su hermana mayor, Rhaenyra. Desde joven, Aegon fue criado en la corte con un sentido de derecho y ambición. Aunque en un inicio no mostró gran interés en la política, su madre y su abuelo, Otto Hightower, lo prepararon para disputar el trono. Su reinado fue violento y marcado por el conflicto, y su dragón Sunfyre se convirtió en un símbolo de su poder. Aegon se enfrentó a numerosos desafíos durante la guerra y sufrió graves heridas. La brutalidad de su reinado y su odio hacia su hermana marcaron profundamente a los Siete Reinos.",
                "Verde");

        addCharacter(db, "Aemond Targaryen", R.drawable.aemond,
                "Aemond Targaryen, apodado 'Aemond el Tuerto', era el hermano menor de Aegon II y uno de los personajes más temidos del bando verde. Después de perder un ojo en una pelea con Lucerys Velaryon, desarrolló un odio profundo hacia los hijos de Rhaenyra. Aemond montaba a Vhagar, uno de los dragones más grandes y antiguos de Westeros, y se convirtió en un guerrero formidable. Su personalidad fría y vengativa, junto con su destreza en combate, lo hicieron una figura clave en la guerra. Aemond finalmente encontró su fin en un combate épico contra Daemon Targaryen en Harrenhal.",
                "Verde");

        addCharacter(db, "Helaena Targaryen", R.drawable.helaena,
                "Helaena Targaryen, hermana y esposa de Aegon II, es conocida por su naturaleza amable y pacífica. A diferencia de sus hermanos, Helaena evitaba la violencia y estaba más interesada en sus hijos y en su dragón, Dreamfyre. Se decía que tenía un don para las visiones proféticas, que a menudo la hacían prever eventos trágicos en la guerra. La muerte de sus hijos y los horrores de la guerra la afectaron profundamente, llevándola a un estado de dolor y desesperanza. Su vida y su muerte simbolizaron la inocencia sacrificada en la lucha por el poder.",
                "Verde");

        addCharacter(db, "Alicent Hightower", R.drawable.alicent,
                "Alicent Hightower, madre de Aegon II, fue una figura central en el bando verde. Como esposa de Viserys I, dedicó su vida a proteger a sus hijos y asegurar su posición en la corte. Alicent era conocida por su inteligencia, ambición y lealtad hacia su familia. Fue ella quien, junto a su padre Otto Hightower, orquestó la coronación de Aegon II, desencadenando la Danza de los Dragones. Su vida estuvo marcada por la tensión y el conflicto con Rhaenyra, y aunque amaba a sus hijos profundamente, las consecuencias de sus decisiones la atormentaron hasta el final.",
                "Verde");

        addCharacter(db, "Criston Cole", R.drawable.criston_cole,
                "Criston Cole, conocido como 'El Hacedor de Reyes', fue el Lord Comandante de la Guardia Real y un firme partidario de Aegon II. Originalmente cercano a Rhaenyra, su relación se deterioró, y su lealtad se volcó hacia el bando verde. Fue uno de los arquitectos de la coronación de Aegon y se convirtió en el líder militar de los verdes durante la guerra. Cole era un guerrero formidable y llevó a cabo acciones despiadadas en nombre de su rey. Su papel en la Danza de los Dragones lo convirtió en una figura legendaria, aunque también en una de las más controversiales de su tiempo.",
                "Verde");

        addCharacter(db, "Otto Hightower", R.drawable.otto_hightower,
                "Otto Hightower, padre de Alicent y Mano del Rey, fue una figura clave en la política de Westeros y en la creación del bando verde. Conocido por su astucia y ambición, Otto fue uno de los principales impulsores de la coronación de Aegon II y trabajó incansablemente para asegurar el poder de su familia. Como Mano del Rey, su influencia fue inmensa, pero su relación con Viserys I y Rhaenyra fue tensa. Otto manipuló los eventos de la corte para favorecer a su familia, aunque sus decisiones tuvieron consecuencias trágicas y devastadoras para los Siete Reinos.",
                "Verde");

        addCharacter(db, "Larys Strong", R.drawable.larys_strong,
                "Larys Strong, conocido como el 'Maestro de Susurros' del bando verde, fue un intrigante astuto y manipulador. Su papel en la corte era ambiguo y misterioso, y trabajaba en las sombras para recopilar información y eliminar a los enemigos de los verdes. Aunque su lealtad a veces era cuestionada, su habilidad para manipular a los nobles y sus conocimientos lo convirtieron en un aliado valioso para Alicent y Aegon II. Larys era temido y respetado en igual medida, y su participación en la guerra dejó una marca indeleble en la historia.",
                "Verde");

        addCharacter(db, "Tyland Lannister", R.drawable.tyland_lannister,
                "Tyland Lannister, hermano gemelo de Jason Lannister, fue el Tesoro Real durante la Danza de los Dragones y un aliado estratégico del bando verde. Con su aguda mente para las finanzas y la diplomacia, Tyland aseguró los recursos necesarios para financiar el esfuerzo de guerra de Aegon II. A pesar de ser capturado y torturado por el bando negro en un momento de la guerra, su lealtad a los verdes no vaciló. La habilidad de Tyland para asegurar la estabilidad financiera y las alianzas le dieron al bando verde una ventaja crucial en los tiempos de conflicto.",
                "Verde");
    }


    //DRAGONES BANDO NEGRO
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

    //DRAGONES BANDO VERDE
    private void insertInitialGreenDragons(SQLiteDatabase db) {
        addDragon(db, "Vhagar", R.drawable.vhagar,
                "Vhagar es uno de los dragones más grandes y antiguos de Westeros. Fue montado por la reina Visenya Targaryen durante la Conquista de Aegon y, más tarde, por Aemond Targaryen en la Danza de los Dragones. Con una envergadura masiva y una ferocidad inigualable, Vhagar es el dragón más temido de su tiempo. Durante la guerra, Vhagar protagonizó algunas de las batallas más épicas, enfrentándose a los dragones del bando negro. La enorme fuerza y resistencia de Vhagar la convierten en un símbolo de la devastación y poder de los Targaryen.",
                "Verde");

        addDragon(db, "Sunfyre", R.drawable.sunfyre,
                "Sunfyre, conocido como el Dragón Dorado, es montado por Aegon II Targaryen. Es famoso por el color dorado de sus escamas, que brilla como el oro bajo el sol. Sunfyre es un dragón hermoso y poderoso, aunque más joven y menos experimentado que Vhagar. Durante la Danza de los Dragones, Sunfyre acompaña a Aegon II en numerosas batallas. Aunque es herido gravemente en una pelea con Moondancer, demuestra una gran lealtad y resistencia, regresando para luchar junto a su jinete en múltiples ocasiones.",
                "Verde");

        addDragon(db, "Dreamfyre", R.drawable.dreamfyre,
                "Dreamfyre es un dragón de gran belleza montado por la princesa Helaena Targaryen. De un color azul pálido con reflejos plateados, es una criatura grácil y de temperamento tranquilo. Aunque Dreamfyre no es utilizado para la guerra tanto como otros dragones, su conexión con Helaena lo convierte en un símbolo de la inocencia perdida durante la Danza de los Dragones. Se cree que Dreamfyre es uno de los dragones más viejos aún vivos, habiendo pertenecido anteriormente a la princesa Rhaena Targaryen.",
                "Verde");

        addDragon(db, "Tessarion", R.drawable.tessarion,
                "Tessarion, también conocido como el Dragón de Bronce, es montado por el príncipe Daeron Targaryen, el hermano menor de Aegon II. Sus escamas tienen un brillo de bronce, y su fuego es de un tono azul intenso. Tessarion es un dragón joven, pero demuestra gran valentía y habilidad en el combate. Durante la Danza de los Dragones, Tessarion lucha al lado de Daeron en numerosas batallas, ganándose el respeto de ambos bandos. Su lealtad y ferocidad en combate hacen de Tessarion un dragón temible en la guerra civil.",
                "Verde");
    }



    // Método para agregar un personaje con su biografía
    private void addCharacter(SQLiteDatabase db, String name, int imageResId, String biography, String faction) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_IMAGE, imageResId);
        values.put(COLUMN_BIOGRAPHY, biography);
        values.put(COLUMN_FACTION, faction);
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
        // Sincronizar con Firebase
        syncUserToFirebase(nombre, email, contrasena, bando, casa);
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

    public void validarLoginFirebase(String nickname, String password, OnLoginListener listener) {
        firestore.collection("users")
                .whereEqualTo("nombre", nickname)
                .whereEqualTo("contrasena", password)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure("Usuario o contraseña incorrectos");
                    }
                })
                .addOnFailureListener(e -> listener.onFailure("Error en la conexión a Firebase"));
    }

    public interface OnLoginListener {
        void onSuccess();
        void onFailure(String message);
    }


    // Método para eliminar un usuario por su nickname
    public boolean eliminarUsuarioPorNickname(String nickname) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete(TABLE_USERS, USER_NAME + " = ?", new String[]{nickname.toLowerCase().trim()});
        db.close();
        return resultado > 0;
    }
}
