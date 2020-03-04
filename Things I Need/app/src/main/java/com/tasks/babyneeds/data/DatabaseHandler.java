package com.tasks.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tasks.babyneeds.R;
import com.tasks.babyneeds.model.Item;
import com.tasks.babyneeds.util.Util;

import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(Context context) {
        super(context,Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String create="CREATE TABLE "+Util.TABLE_NAME+"("+Util.KEY_ID+" INTEGER PRIMARY KEY,"+Util.KEY_ITEM+
                " TEXT,"+Util.KEY_QTY+" INTEGER,"+Util.KEY_COLOR+" TEXT,"+Util.KEY_SIZE+" INTEGER);";
        db.execSQL(create);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.valueOf(R.string.Drop_at);
        db.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        //Create a table again
        onCreate(db);
    }

    public void addItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Util.KEY_ITEM,item.getItem());
        contentValues.put(Util.KEY_QTY,item.getQty());
        contentValues.put(Util.KEY_COLOR,item.getColor());
        contentValues.put(Util.KEY_SIZE,item.getSize());
        //contentValues.put(Util.TABLE_NAME,java.lang.System.currentTimeMillis());// this will give us the timestamp to save the date and the time

//        inserting into database
        db.insert(Util.TABLE_NAME,null,contentValues);
        db.close();
    }

    public Item getItem(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Util.TABLE_NAME,new String[]{Util.KEY_ID,Util.KEY_ITEM,Util.KEY_QTY,Util.KEY_COLOR,Util.KEY_SIZE},Util.KEY_ID+"=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        Item item=new Item();
        item.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Util.KEY_ID))));
        item.setItem(cursor.getString(1));
        item.setQty(cursor.getInt(2));
        item.setColor(cursor.getString(3));
        item.setSize(cursor.getInt(4));

//        converting timestamp into something readable
//        DateFormat dateFormat=DateFormat.getDateInstance();
//        String formatteddate=dateFormat.format(new Date(cursor.getLong(5)));
//        item.setDate(formatteddate);

        return item;
    }

    public List<Item> getAllItems(){
        List<Item> itemList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String query="SELECT * FROM "+Util.TABLE_NAME;
        Cursor cursor=db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Item item=new Item();
                item.setId(cursor.getInt(0));
                item.setItem(cursor.getString(1));
                item.setQty(cursor.getInt(2));
                item.setColor(cursor.getString(3));
                item.setSize(cursor.getInt(4));
                itemList.add(item);

            }while (cursor.moveToNext());
        }
        return itemList;

    }


    public int updateItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();


        ContentValues contentValues=new ContentValues();
        contentValues.put(Util.KEY_ITEM,item.getItem());
        contentValues.put(Util.KEY_QTY,item.getQty());
        contentValues.put(Util.KEY_COLOR,item.getColor());
        contentValues.put(Util.KEY_SIZE,item.getSize());

        return db.update(Util.TABLE_NAME,contentValues,Util.KEY_ID+"=?",new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Util.TABLE_NAME,Util.KEY_ID+"=?",new String[]{String.valueOf(item.getId())});
        db.close();
    }
    public int getCount(){
        String query="SELECT * FROM "+ Util.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(query,null);
        return cursor.getCount();
    }


}
