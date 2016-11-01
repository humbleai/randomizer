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

    // Database Version
    private static final int DATABASE_VERSION = R.integer.db_version;
    // Database Name
    private static final String DATABASE_NAME = "SetsDB";


    // SetList table name
    private static final String TABLE_SETLISTS = "SetList";

    // SetList Table Columns names
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

        // SQL statement to create SetList table
        String CREATE_SETLIST_TABLE = "CREATE TABLE SetList ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "icon INTEGER, "+
                "title TEXT, "+
                "description TEXT, " +
                "prime TEXT, " +
                "settype BOOLEAN)";


        // create SetLists table
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


        // SQL statement to create item table
        String CREATE_ITEM_TABLE = "CREATE TABLE SetItem ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "parentSetId INTEGER, "+
                "icon INTEGER, "+
                "title TEXT, "+
                "color TEXT, " +
                "settype TEXT, " +
                "excluded INTEGER )";

        // create books table
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


        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_ICON, setlist.getIcon()); // get icon
        values.put(KEY_TITLE, setlist.getTitle()); // get title
        values.put(KEY_DESCRIPTION, setlist.getDescription()); // get desc
        values.put(KEY_PRIMARY, setlist.getPrime()); // get title
        values.put(KEY_SETTYPE, setlist.getSetType()); // get desc
        // 3. insert
        Long inserted = db.insert(TABLE_SETLISTS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
      //  db.close();
        return inserted;
    }


    public SetList getSetList(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_SETLISTS, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build setlist object
        SetList setlist = new SetList();
        assert cursor != null;
        setlist.setId(Integer.parseInt(cursor.getString(0)));
        setlist.setIcon(Integer.parseInt(cursor.getString(1)));
        setlist.setTitle(cursor.getString(2));
        setlist.setDescription(cursor.getString(3));
        setlist.setPrime(cursor.getString(4));
        setlist.setSetType(cursor.getString(5));

        cursor.close();
        // 5. return setlist
        return setlist;
    }


    public List<SetList> getAllSetLists() {
        List<SetList> setlists = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_SETLISTS;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build setlist and add it to list
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

                // Add setlist to setlists
                setlists.add(setlist);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // return setlists
        return setlists;
    }


    public int updateSetList(SetList setlist) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("icon", setlist.getIcon()); // get icon
        values.put("title", setlist.getTitle()); // get title
        values.put("description", setlist.getDescription()); // get description
        values.put("prime", setlist.getPrime());
        values.put("settype", setlist.getSetType());

        // 3. updating row
        int i = db.update(TABLE_SETLISTS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(setlist.getId()) }); //selection args


        // 4. close
     //   db.close();

        return i;

    }


    public void deleteSetList(int setId) {

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. delete
        db.delete(TABLE_SETLISTS, //table name
                KEY_ID + " = ?",  // selections
                new String[]{String.valueOf(setId)}); //selections args

        String query = "DELETE FROM SetItem WHERE parentSetId = " + setId;
        db.execSQL(query);

        // 3. close
      //  db.close();


    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

}
