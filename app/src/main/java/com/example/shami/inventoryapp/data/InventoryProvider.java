package com.example.shami.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


/**
 * Created by Shami on 2/6/2017.
 */

public class InventoryProvider extends ContentProvider {

    InventoryDB inventoryDBHelper;
    private final static String Log_Tag="[DM]Shami "+InventoryProvider.class.getSimpleName();

    @Override
    public boolean onCreate() {
        inventoryDBHelper=new InventoryDB(getContext());
        return true;
    }

    private static final int Products=100;
    private static final int Product_id=101;
    private static final int Suppliers=102;
    private static final int Supplier_id=103;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.Path_Product, Products);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.Path_Product+"/#",Product_id);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.Path_Supplier,Suppliers);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY,InventoryContract.Path_Supplier+"/#",Supplier_id);
    }


    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionargs, String sortOrder) {
        SQLiteDatabase db = inventoryDBHelper.getReadableDatabase();
        Cursor cursor = null;
        int match=sUriMatcher.match(uri);

        switch(match)
        {
            case Products:
                cursor=db.query(InventoryContract.Product.Table_name,projection,selection,selectionargs,null,null,sortOrder);
                break;
            case Product_id:
                selection=InventoryContract.Product.p_id+"=?";
                selectionargs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(InventoryContract.Product.Table_name,projection,selection,selectionargs,null,null,sortOrder);
                break;
            case Suppliers:
                cursor=db.query(InventoryContract.Supplier.Table_name,projection,selection,selectionargs,null,null,sortOrder);
                break;
            case Supplier_id:
                selection=InventoryContract.Supplier.suppiler_id+"=?";
                selectionargs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(InventoryContract.Supplier.Table_name,projection,selection,selectionargs,null,null,sortOrder);
                break;
            default:
                throw  new IllegalArgumentException("Wrong Argument operation cant be performed");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }








    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        int match=sUriMatcher.match(uri);

        switch (match)
        {
            case Products:
                uri=insertProduct(uri,contentValues);
                break;
            case Suppliers:
                uri=insertSupplier(uri,contentValues);
                break;
            default:
                throw new IllegalArgumentException("The Insertation cannot be performed");
        }

        return uri;

    }

    private Uri insertProduct(Uri uri,ContentValues contentValues)
    {
        SQLiteDatabase db=inventoryDBHelper.getWritableDatabase();
        long id=db.insert(InventoryContract.Product.Table_name,null,contentValues);
        Log.v(Log_Tag,"The new row is edited with id "+id);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }


    private Uri insertSupplier(Uri uri,ContentValues contentValues)
    {
        SQLiteDatabase db=inventoryDBHelper.getWritableDatabase();
        long id=db.insert(InventoryContract.Supplier.Table_name,null,contentValues);
        Log.v(Log_Tag,"The new row is edited with id "+id);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] args) {

        SQLiteDatabase db=inventoryDBHelper.getReadableDatabase();
        int match=sUriMatcher.match(uri);
        int rowsDeleted=0;
        switch(match)
        {
            case Products:
                rowsDeleted=db.delete(InventoryContract.Product.Table_name,selection,args); break;
            case Product_id:
                selection= InventoryContract.Product.p_id+"=?";
                args=new String []{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(InventoryContract.Product.Table_name,selection,args); break;
            case Suppliers:
                rowsDeleted=db.delete(InventoryContract.Supplier.Table_name,selection,args);
                break;
            case Supplier_id:
                selection= InventoryContract.Supplier.suppiler_id+"=?";
                args=new String []{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=db.delete(InventoryContract.Supplier.Table_name,selection,args);
                break;
            default:
                throw new IllegalArgumentException("The product or Supplier cannot be deleted");
        }
        if(rowsDeleted>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int match=sUriMatcher.match(uri);
        int rowsUpdated=0;

        switch (match)
        {
            case Products:
                rowsUpdated=UpdateProduct(contentValues,selection,selectionArgs);
                break;
            case Product_id:
                selection=InventoryContract.Product.p_id+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated=UpdateProduct(contentValues,selection,selectionArgs);
                break;
            case Suppliers:
                rowsUpdated=updateSupplier(contentValues,selection,selectionArgs);
                break;
            case Supplier_id:
                selection=InventoryContract.Supplier.suppiler_id+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsUpdated=updateSupplier(contentValues,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("The Operation Update cant be performed");
        }
        if(rowsUpdated>0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return rowsUpdated;
    }

    private int updateSupplier(ContentValues contentValues,String selection,String[] args)
    {
        int rowsUpdated=0;
        SQLiteDatabase db=inventoryDBHelper.getReadableDatabase();

        rowsUpdated=db.update(InventoryContract.Supplier.Table_name,contentValues,selection,args);
        Log.v(Log_Tag,"The updated number of rows are "+rowsUpdated);
        return rowsUpdated;
    }
    private int UpdateProduct(ContentValues contentValues,String selection,String[] args)
    {
        int rowsUpdated=0;
        SQLiteDatabase db=inventoryDBHelper.getReadableDatabase();
        rowsUpdated=db.update(InventoryContract.Product.Table_name,contentValues,selection,args);
        Log.v(Log_Tag,"The updated number of rows are "+rowsUpdated);
        return rowsUpdated;
    }

}
