package com.example.maadela;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="register.db";
    public static final String TABLE_NAME="register_table";
    public static final String COL_1="Name";
    public static final String COL_2="Co_Number";
    public static final String COL_3="Password";
    public static final String COL_4="Co_Password";


    public DatabaseHelper(Context context) {
        super(context,DATABASE_NAME,null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create  table "+TABLE_NAME+"(Name TEXT PRIMARY KEY,Co_Number TEXT,NIC TEXT,EMAIL TEXT,Password TEXT,Co_Password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

   public boolean insertData(String name,String pn,String nic,String email,String password,String co_password){
       SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
       ContentValues contentValues=new ContentValues();
       contentValues.put(COL_1,name);
       contentValues.put(COL_2,pn);
       contentValues.put(COL_3,password);
       contentValues.put(COL_4,co_password);
       long result=sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
       if(result==-1)
           return false;
       else
           return  true;



    }
//    public Cursor getAllData(){
//        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
//        Cursor res=sqLiteDatabase.rawQuery("select * from "+TABLE_NAME,null );
//        return res;
//    }
public boolean retrieveData(String name,String pw){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery("Select * From register_table where Name=? and Password=? ",new String[]{name,pw});
        if(cursor.getCount()>0)
            return false;
            else
                return true;
        }
}

