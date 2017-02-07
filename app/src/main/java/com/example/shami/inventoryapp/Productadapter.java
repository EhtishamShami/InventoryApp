package com.example.shami.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shami.inventoryapp.data.InventoryContract;

/**
 * Created by Shami on 2/6/2017.
 */

public class Productadapter extends CursorAdapter {

    public Productadapter(Context context,Cursor cursor)
    {
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.inventory_list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name=(TextView)view.findViewById(R.id.nametextview);
        TextView quantity=(TextView)view.findViewById(R.id.quantitytextview);
        TextView price=(TextView)view.findViewById(R.id.pricetextview);

        String P_name=cursor.getString(cursor.getColumnIndex(InventoryContract.Product.P_Title));
        int P_quantity=cursor.getInt(cursor.getColumnIndex(InventoryContract.Product.P_Quantity));
        int P_price=cursor.getInt(cursor.getColumnIndex(InventoryContract.Product.P_Price));

        name.setText(P_name);
        quantity.setText(Integer.toString(P_quantity));
        price.setText(Integer.toString(P_price));
    }
}
