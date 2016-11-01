package com.humbleai.randomizer;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

class SetItemSQLiteHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = R.integer.db_version;

    private static final String DATABASE_NAME = "SetsDB";


    private static final String TABLE_SETITEMS = "SetItem";


    private static final String KEY_ID = "id";
    private static final String KEY_PARENTSETID = "parentSetId";
    private static final String KEY_ICON = "icon";
    private static final String KEY_TITLE = "title";
    private static final String KEY_COLOR = "color";
    private static final String KEY_SETTYPE = "settype";
    private static final String KEY_EXCLUDED = "excluded";

    private static final String[] COLUMNS = {KEY_ID, KEY_PARENTSETID, KEY_ICON, KEY_TITLE, KEY_COLOR, KEY_SETTYPE, KEY_EXCLUDED};

    public SetItemSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS SetItem");


        this.onCreate(db);
    }



    public Long addSetItem(SetItem setitem){


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put(KEY_PARENTSETID, setitem.getParentSetId());
        values.put(KEY_ICON, setitem.getIcon());
        values.put(KEY_TITLE, setitem.getTitle());
        values.put(KEY_SETTYPE, setitem.getSetType());
        values.put(KEY_EXCLUDED, setitem.getExcluded());

        Long inserted =db.insert(TABLE_SETITEMS, // table
                null,
                values);


      //  db.close();

        return inserted;
    }


    public SetItem getSetItem(int id){


        SQLiteDatabase db = this.getReadableDatabase();


        Cursor cursor =
                db.query(TABLE_SETITEMS,
                        COLUMNS,
                        " id = ?",
                        new String[] { String.valueOf(id) },
                        null,
                        null,
                        null,
                        null);


        if (cursor != null)
            cursor.moveToFirst();


        SetItem setitem = new SetItem();
        assert cursor != null;
        setitem.setId(Integer.parseInt(cursor.getString(0)));
        setitem.setParentSetId(Integer.parseInt(cursor.getString(1)));
        setitem.setIcon(Integer.parseInt(cursor.getString(2)));
        setitem.setTitle(cursor.getString(3));
        setitem.setSetType(cursor.getString(5));
        //setitem.setItemViewType(cursor.getInt(6));
        setitem.setExcluded(cursor.getInt(6));

        cursor.close();

        return setitem;
    }


    public SetItem getRandomSetItem(int setIdsi, int count){


        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_SETITEMS + " WHERE " + KEY_PARENTSETID + " = " + setIdsi + " AND " + KEY_EXCLUDED + " = 0 LIMIT 1 OFFSET " + String.valueOf(count) + ";";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null)
            cursor.moveToFirst();


        SetItem setitem = new SetItem();
        assert cursor != null;
        setitem.setId(Integer.parseInt(cursor.getString(0)));
        setitem.setParentSetId(Integer.parseInt(cursor.getString(1)));
        setitem.setIcon(Integer.parseInt(cursor.getString(2)));
        setitem.setTitle(cursor.getString(3));
        setitem.setSetType(cursor.getString(5));
        //setitem.setItemViewType(cursor.getInt(6));
        setitem.setExcluded(cursor.getInt(6));

        cursor.close();

        return setitem;
    }

    public Long getCount(int setIdsi, boolean includedOnly){
        SQLiteDatabase db = this.getReadableDatabase();

        String wherePart =  KEY_PARENTSETID + " = " + setIdsi;

        if (includedOnly) wherePart = wherePart + " AND " + KEY_EXCLUDED + " = 0";

        return DatabaseUtils.queryNumEntries(db, TABLE_SETITEMS, wherePart);
    }

    public List<SetItem> getAllSetItems(int setIdsi, int lastItemId, boolean full) {
        List<SetItem> setitems = new LinkedList<>();


        String query = "SELECT  * FROM " + TABLE_SETITEMS + " WHERE " + KEY_PARENTSETID + " = " + setIdsi + " AND " + KEY_ID + " > " + lastItemId;



        if (!full) query = query + " LIMIT 100";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);


        SetItem setitem;
        if (cursor.moveToFirst()) {
            do {
                setitem = new SetItem();
                setitem.setId(Integer.parseInt(cursor.getString(0)));
                setitem.setParentSetId(Integer.parseInt(cursor.getString(1)));
                setitem.setIcon(Integer.parseInt(cursor.getString(2)));
                setitem.setTitle(cursor.getString(3));
                setitem.setSetType(cursor.getString(5));
                //setitem.setItemViewType(cursor.getInt(6));
                setitem.setExcluded(cursor.getInt(6));


                setitems.add(setitem);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return setitems;
    }


     public int updateSetItem(SetItem setitem) {


        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("parentSetId", setitem.getParentSetId());
         values.put("icon", setitem.getIcon());
         values.put("title", setitem.getTitle());
         values.put("settype", setitem.getSetType());
        // values.put("itemviewtype", setitem.getItemViewType());
         values.put("excluded", setitem.getExcluded());


        int i = db.update(TABLE_SETITEMS,
                values,
                KEY_ID+" = ?",
                new String[] { String.valueOf(setitem.getId()) });


       // db.close();

        return i;

    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
    public void deleteSetItem(SetItem setitem) {


        SQLiteDatabase db = this.getWritableDatabase();


        db.delete(TABLE_SETITEMS,
                KEY_ID + " = ?",
                new String[]{String.valueOf(setitem.getId())});


     //   db.close();


    }


}
