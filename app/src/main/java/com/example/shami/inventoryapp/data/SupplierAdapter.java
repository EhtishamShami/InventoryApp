package com.example.shami.inventoryapp.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.shami.inventoryapp.R;

/**
 * Created by Shami on 2/7/2017.
 */

public class SupplierAdapter extends CursorAdapter {


    public SupplierAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.drop_down_layout,viewGroup);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView=(TextView)view.findViewById(R.id.dropdownitem);
        textView.setText(cursor.getString(cursor.getColumnIndex(InventoryContract.Supplier.suppiler_name)));
    }
}
