package com.example.shami.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Shami on 2/6/2017.
 */

public class InventoryDB extends SQLiteOpenHelper {

    private static final String LOG_TAG="[DM]Shami "+InventoryDB.class.getSimpleName();
    private static final String DataBaseName="Inventory.db";
    private static final int version=3;

    public InventoryDB(Context context)
    {
        super(context,DataBaseName,null,version);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String SQL_CREATE_Products_TABLE="CREATE TABLE "+InventoryContract.Product.Table_name+"("
                +InventoryContract.Product.p_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +InventoryContract.Product.P_Title+ " TEXT NOT NULL, "
                +InventoryContract.Product.P_Price+" INTEGER NOT NULL DEFAULT 0,"
                +InventoryContract.Product.P_Quantity+" INTEGER NOT NULL DEFAULT 0,"
                +InventoryContract.Product.P_suppiler+" INTEGER NOT NULL,"
                +InventoryContract.Product.P_pictures+" BOLB"
                +");";
        sqLiteDatabase.execSQL(SQL_CREATE_Products_TABLE);

        String SQL_CREATE_Suppiler_TABLE="CREATE TABLE "+InventoryContract.Supplier.Table_name+"("
                +InventoryContract.Supplier.suppiler_id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +InventoryContract.Supplier.suppiler_name+ " TEXT NOT NULL, "
                +InventoryContract.Supplier.supplier_phone+" INTEGER NOT NULL, "
                +InventoryContract.Supplier.supplier_email+" TEXT "
                +");";

        sqLiteDatabase.execSQL(SQL_CREATE_Suppiler_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE [IF EXISTS] "+InventoryContract.Supplier.Table_name);

        sqLiteDatabase.execSQL("DROP TABLE [IF EXISTS] "+InventoryContract.Product.Table_name);
        onCreate(sqLiteDatabase);
    }
}
