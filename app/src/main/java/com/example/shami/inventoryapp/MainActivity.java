package com.example.shami.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shami.inventoryapp.data.InventoryContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String Log_Tag="[DM]Shami "+MainActivity.class.getSimpleName();

    Productadapter productadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                startActivity(intent);
            }
        });

        productadapter=new Productadapter(this,null);
        ListView list=(ListView)findViewById(R.id.listview);
        View empty = getLayoutInflater().inflate(R.layout.emptyview, null, false);
        addContentView(empty, new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, AppBarLayout.LayoutParams.MATCH_PARENT));

        list.setEmptyView(empty);
        list.setAdapter(productadapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,DetailActivity.class);
                Uri CurrentUri= ContentUris.withAppendedId(InventoryContract.Product.Content_Uri,id);
                intent.setData(CurrentUri);
                startActivity(intent);
            }
        });



        /*
        InventoryDB dbhelper=new InventoryDB(this);
        SQLiteDatabase db =dbhelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from "+ InventoryContract.Product.Table_name,null);
        Toast.makeText(getApplicationContext(),"The number of rows are "+cursor.getCount(),Toast.LENGTH_SHORT).show();

*/

        getSupportLoaderManager().initLoader(0,null,this);
        insertSupplier();
    }

    void insertSupplier()
    {


        getContentResolver().delete(InventoryContract.Supplier.Content_Uri,null,null);


        ContentValues contentValues1=new ContentValues();
        contentValues1.put(InventoryContract.Supplier.suppiler_name,"MadMax");
        contentValues1.put(InventoryContract.Supplier.supplier_phone,"03455650843");
        contentValues1.put(InventoryContract.Supplier.supplier_email,"madmax@madmax.com");
        getContentResolver().insert(InventoryContract.Supplier.Content_Uri,contentValues1);


        ContentValues contentValues=new ContentValues();
        contentValues.put(InventoryContract.Supplier.suppiler_name,"Batman");
        contentValues.put(InventoryContract.Supplier.supplier_phone,"03455650843");
        contentValues.put(InventoryContract.Supplier.supplier_email,"batman@batman.com");
        getContentResolver().insert(InventoryContract.Supplier.Content_Uri,contentValues);


        ContentValues Values=new ContentValues();
        Values.put(InventoryContract.Supplier.suppiler_name,"CookingDude");
        Values.put(InventoryContract.Supplier.supplier_phone,"03455650843");
        Values.put(InventoryContract.Supplier.supplier_email,"cooking@Cooking.com");
        getContentResolver().insert(InventoryContract.Supplier.Content_Uri,Values);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menumain,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_order:
                return true;

            case R.id.action_deleteall:
                deleteAll();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void deleteAll()
    {
       int rowsDeleted= getContentResolver().delete(InventoryContract.Product.Content_Uri,null,null);
       Toast.makeText(getApplicationContext(),"The number of rows deleted "+rowsDeleted,Toast.LENGTH_SHORT).show();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection=
                {
                        InventoryContract.Product.p_id,
                        InventoryContract.Product.P_Title,
                        InventoryContract.Product.P_Price,
                        InventoryContract.Product.P_Quantity,
                        InventoryContract.Product.P_suppiler,
                        InventoryContract.Product.P_pictures
                };
        return new CursorLoader(this, InventoryContract.Product.Content_Uri,projection,null,null,null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    productadapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        productadapter.swapCursor(null);
    }
}
