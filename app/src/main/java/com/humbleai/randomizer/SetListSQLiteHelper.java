package com.humbleai.randomizer;


import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;


class SetListSQLiteHelper extends SQLiteOpenHelper {

    private final Context context;


    private static final int DATABASE_VERSION = R.integer.db_version;

    private static final String DATABASE_NAME = "SetsDB";



    private static final String TABLE_SETLISTS = "SetList";


    private static final String KEY_ID = "id";
    private static final String KEY_ICON = "icon";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRIMARY = "prime";
    private static final String KEY_SETTYPE = "settype";

    private static final String[] COLUMNS = {KEY_ID, KEY_ICON, KEY_TITLE, KEY_DESCRIPTION, KEY_PRIMARY, KEY_SETTYPE};


    public SetListSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Locale.setDefault(Locale.getDefault());
                this.context =context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String CREATE_SETLIST_TABLE = "CREATE TABLE SetList ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "icon INTEGER, "+
                "title TEXT, "+
                "description TEXT, " +
                "prime TEXT, " +
                "settype BOOLEAN)";



        db.execSQL(CREATE_SETLIST_TABLE);

        Resources res = context.getResources();

        TypedArray sets_icons = res.obtainTypedArray(R.array.sets_icons);
        TypedArray sets_titles = res.obtainTypedArray(R.array.sets_titles);
        TypedArray sets_descs = res.obtainTypedArray(R.array.sets_descs);
        TypedArray sets_primaries = res.obtainTypedArray(R.array.sets_primaries);
        TypedArray sets_set_types = res.obtainTypedArray(R.array.sets_set_types);

        for(int i=0; i<sets_titles.length(); i++){
            String INSERT_SETLIST_ENTRIES = "INSERT INTO SetList (id, icon, title, description, prime, settype) " +
                    "VALUES (" + (i + 1) + ", " + sets_icons.getResourceId(i, 0) + ", '" + sets_titles.getString(i) + "', '" + sets_descs.getString(i) + "', ' " + sets_primaries.getString(i) + "', '" + sets_set_types.getString(i) +"')";

            db.execSQL(INSERT_SETLIST_ENTRIES);
        }

        sets_icons.recycle();
        sets_descs.recycle();
        sets_titles.recycle();
        sets_primaries.recycle();
        sets_set_types.recycle();



        String CREATE_ITEM_TABLE = "CREATE TABLE SetItem ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "parentSetId INTEGER, "+
                "icon INTEGER, "+
                "title TEXT, "+
                "color TEXT, " +
                "settype TEXT, " +
                "excluded INTEGER )";


        db.execSQL(CREATE_ITEM_TABLE);



        // Coins ////////////////////////////

        TypedArray coins_icons = res.obtainTypedArray(R.array.coins_icons);
        TypedArray coins_titles = res.obtainTypedArray(R.array.coins_titles);

        for(int i=0; i< coins_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 2 + ", " + coins_icons.getResourceId(i, 0) + ", '" + coins_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";

            db.execSQL(query);
        }

        coins_icons.recycle();
        coins_titles.recycle();





        // Dice ////////////////////////////

        TypedArray dice_icons = res.obtainTypedArray(R.array.dice_icons);
        TypedArray dice_titles = res.obtainTypedArray(R.array.dice_titles);

        for(int i=0; i< dice_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 3 + ", " + dice_icons.getResourceId(i, 0) + ", '" + dice_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";

            db.execSQL(query);
        }

        dice_icons.recycle();
        dice_titles.recycle();

        // Card ////////////////////////////

        TypedArray cards_icons = res.obtainTypedArray(R.array.cards_icons);
        TypedArray cards_titles = res.obtainTypedArray(R.array.cards_titles);

        for(int i=0; i< cards_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 4 + ", " + cards_icons.getResourceId(i, 0) + ", '" + cards_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";
            db.execSQL(query);
        }

        cards_icons.recycle();
        cards_titles.recycle();


        // Letter ////////////////////////////

        TypedArray letters_icons = res.obtainTypedArray(R.array.letters_icons);
        TypedArray letters_titles = res.obtainTypedArray(R.array.letters_titles);

        for(int i=0; i< letters_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 5 + ", " + letters_icons.getResourceId(i, 0) + ", '" + letters_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";
            db.execSQL(query);
        }

        letters_icons.recycle();
        letters_titles.recycle();




        // Country ////////////////////////////

        TypedArray countries_icons = res.obtainTypedArray(R.array.countries_icons);
        TypedArray countries_titles = res.obtainTypedArray(R.array.countries_titles);

        for(int i=0; i< countries_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 6 + ", " + countries_icons.getResourceId(i, 0) + ", '" + countries_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";

            db.execSQL(query);
        }

        countries_icons.recycle();
        countries_titles.recycle();


        // Color ////////////////////////////

        TypedArray colors_titles = res.obtainTypedArray(R.array.colors_titles);
        TypedArray colors_icons = res.obtainTypedArray(R.array.colors_icons);
        TypedArray colors_colors = res.obtainTypedArray(R.array.colors_colors);

        for(int i=0; i< colors_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 7 + ", " + colors_icons.getResourceId(i, 0)+ ", '" + colors_titles.getString(i) + "\n" + colors_colors.getString(i) + "', '" + colors_colors.getString(i) +"', 'stock', 0)";

            db.execSQL(query);
        }

        colors_titles.recycle();
        colors_icons.recycle();
        colors_colors.recycle();


        // Day of Week ////////////////////////////

        TypedArray days_titles = res.obtainTypedArray(R.array.days_titles);
        TypedArray days_icons = res.obtainTypedArray(R.array.days_icons);

        for(int i=0; i< days_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 8 + ", " + days_icons.getResourceId(i, 0) + ", '" + days_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";
            db.execSQL(query);
        }

        days_titles.recycle();
        days_icons.recycle();

        // Month ////////////////////////////

        TypedArray months_titles = res.obtainTypedArray(R.array.months_titles);
        TypedArray months_icons = res.obtainTypedArray(R.array.months_icons);

        for(int i=0; i< months_titles.length(); i++){
            String query = "INSERT INTO SetItem (parentSetId, icon, title, color, settype, excluded) " +
                    "VALUES (" + 9 + ", " + months_icons.getResourceId(i, 0) + ", '" + months_titles.getString(i) + "', '#FAFAFA', 'stock', 0)";
            db.execSQL(query);
        }

        months_titles.recycle();
        months_icons.recycle();



        ////////////////////////////


    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older SetLists table if existed
        db.execSQL("DROP TABLE IF EXISTS SetList");

        // create fresh SetLists table
        this.onCreate(db);
    }

    public Long addSetList(SetList setlist){



        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_ICON, setlist.getIcon());
        values.put(KEY_TITLE, setlist.getTitle());
        values.put(KEY_DESCRIPTION, setlist.getDescription());
        values.put(KEY_PRIMARY, setlist.getPrime());
        values.put(KEY_SETTYPE, setlist.getSetType());

        Long inserted = db.insert(TABLE_SETLISTS,
                null,
                values);


      //  db.close();
        return inserted;
    }


    public SetList getSetList(int id){


        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor =
                db.query(TABLE_SETLISTS,
                        COLUMNS,
                        " id = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();


        SetList setlist = new SetList();
        assert cursor != null;
        setlist.setId(Integer.parseInt(cursor.getString(0)));
        setlist.setIcon(Integer.parseInt(cursor.getString(1)));
        setlist.setTitle(cursor.getString(2));
        setlist.setDescription(cursor.getString(3));
        setlist.setPrime(cursor.getString(4));
        setlist.setSetType(cursor.getString(5));

        cursor.close();

        return setlist;
    }


    public List<SetList> getAllSetLists() {
        List<SetList> setlists = new LinkedList<>();


        String query = "SELECT  * FROM " + TABLE_SETLISTS;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        SetList setlist;
        if (cursor.moveToFirst()) {
            do {
                setlist = new SetList();
                setlist.setId(Integer.parseInt(cursor.getString(0)));
                setlist.setIcon(Integer.parseInt(cursor.getString(1)));
                setlist.setTitle(cursor.getString(2));
                setlist.setDescription(cursor.getString(3));
                setlist.setPrime(cursor.getString(4));
                setlist.setSetType(cursor.getString(5));


                setlists.add(setlist);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return setlists;
    }


    public int updateSetList(SetList setlist) {


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("icon", setlist.getIcon());
        values.put("title", setlist.getTitle());
        values.put("description", setlist.getDescription());
        values.put("prime", setlist.getPrime());
        values.put("settype", setlist.getSetType());


        int i = db.update(TABLE_SETLISTS,
                values,
                KEY_ID+" = ?",
                new String[] { String.valueOf(setlist.getId()) });



     //   db.close();

        return i;

    }


    public void deleteSetList(int setId) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_SETLISTS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(setId)});

        String query = "DELETE FROM SetItem WHERE parentSetId = " + setId;
        db.execSQL(query);


      //  db.close();


    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
