package com.example.shami.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Shami on 2/6/2017.
 */

public final class InventoryContract {
    InventoryContract(){};

    public static final String CONTENT_AUTHORITY="com.example.shami.inventoryapp";

    public static final Uri Base_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final String Path_Product="Products";

    public static final String Path_Supplier="Supplier";

public static final class Product implements BaseColumns
{
    public static final Uri Content_Uri=Uri.withAppendedPath(Base_CONTENT_URI,Path_Product);

    public static final String Table_name="products";
    public static final String p_id=BaseColumns._ID;
    public static final String P_Title="p_name";
    public static final String P_Quantity="p_quantity";
    public static final String P_Price="p_price";
    public static final String P_suppiler="p_suppilerId";
    public static final String P_pictures="p_picture";

}

public static final class Supplier implements BaseColumns
{


    public static final Uri Content_Uri=Uri.withAppendedPath(Base_CONTENT_URI,Path_Supplier);

    public static final String Table_name="supplier";
    public static final String suppiler_id=BaseColumns._ID;
    public static final String suppiler_name="s_name";
    public static final String supplier_phone="s_phone";
    public static final String supplier_email="s_email";


}


}
