package com.example.shami.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shami.inventoryapp.data.InventoryContract;
import com.example.shami.inventoryapp.data.SupplierAdapter;

import java.io.ByteArrayOutputStream;


public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String Log_Tag="[DM]Shami "+DetailActivity.class.getSimpleName();

    EditText P_Name;
    EditText P_Quantity;
    EditText P_Price;
    ImageView P_Picture;
    Spinner supplierspiner;
    SupplierAdapter supplierAdapter;
    Uri CurrentUri=null;
    SimpleCursorAdapter sAdapter;

    private static final int Suppiler_loader_id=0;
    private static final int Product_loader_id=1;


    private boolean mPetHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };
    int oursuppiler_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent getIntent=getIntent();


        CurrentUri=getIntent.getData();

        if(CurrentUri!=null)
        {
            setTitle("Edit a Product");
            getSupportLoaderManager().initLoader(Product_loader_id,null,this);
            getSupportLoaderManager().initLoader(Suppiler_loader_id,null,this);
        }
        else
        {

            getSupportLoaderManager().initLoader(Suppiler_loader_id,null,this);
            setTitle("Add a new Product");
        }



        P_Name=(EditText)findViewById(R.id.nameEdittext);
        P_Quantity=(EditText)findViewById(R.id.quantityEdittext);
        P_Price=(EditText)findViewById(R.id.priceEdittext);
        P_Picture=(ImageView) findViewById(R.id.productpic);
        supplierspiner=(Spinner)findViewById(R.id.spinner);
        sAdapter= new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, null, new String[] {InventoryContract.Supplier.suppiler_name}, new int[] {android.R.id.text1}, 0);
        sAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        supplierspiner.setAdapter(sAdapter);

        supplierspiner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                oursuppiler_id=position;

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        P_Name.setOnTouchListener(mTouchListener);
        P_Quantity.setOnTouchListener(mTouchListener);
        P_Price.setOnTouchListener(mTouchListener);
        P_Picture.setOnTouchListener(mTouchListener);
        supplierspiner.setOnTouchListener(mTouchListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menudetail,menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (CurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.action_save:
              if(!IsEmptyField())
              {
                  SaveProduct();
                  finish();
              }
              else
              {
                  Toast.makeText(getApplicationContext(),"One or More fields are empty",Toast.LENGTH_SHORT).show();
              }
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.home:
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(DetailActivity.this);
                            }
                        };

                showUnsavedChangesDialog(discardButtonClickListener);
                return true;

        }


        return super.onOptionsItemSelected(item);
    }


    public void DeleteProduct()
    {
     int rowDeleted=   getContentResolver().delete(CurrentUri,null,null);
     Toast.makeText(getApplicationContext(),"The Number of Rows deleted "+rowDeleted,Toast.LENGTH_SHORT).show();
    }

    public void SaveProduct()
    {


        byte[] img=ImagetoByte();
        String title=P_Name.getText().toString().trim();
        int quantity=Integer.parseInt(P_Quantity.getText().toString().trim());
        int price=Integer.parseInt(P_Price.getText().toString().trim());
        ContentValues contentValues=new ContentValues();
        contentValues.put(InventoryContract.Product.P_Title,title);
        contentValues.put(InventoryContract.Product.P_Price,price);
        contentValues.put(InventoryContract.Product.P_Quantity,quantity);
        contentValues.put(InventoryContract.Product.P_suppiler,oursuppiler_id);
        contentValues.put(InventoryContract.Product.P_pictures,img);

        if(CurrentUri!=null)
        {
            getContentResolver().update(CurrentUri,contentValues,null,null);
        }
        else{
              getContentResolver().insert(InventoryContract.Product.Content_Uri,contentValues);
            }
        }
    public boolean IsEmptyField()
        {
            if(TextUtils.isEmpty(P_Name.getText())||TextUtils.isEmpty(P_Quantity.getText())||TextUtils.isEmpty(P_Price.getText()))
            {
                return true;
            }
            return false;
        }

    public byte[] ImagetoByte()
    {
        Bitmap b = BitmapFactory.decodeResource(getResources(),
                R.drawable.mainpic);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] img = bos.toByteArray();
      return img;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        switch (id)
        {
            case Product_loader_id:
                String[] Product_projecton=
                        {
                                InventoryContract.Product.p_id,
                                InventoryContract.Product.P_Title,
                                InventoryContract.Product.P_suppiler,
                                InventoryContract.Product.P_Quantity,
                                InventoryContract.Product.P_Price,
                                InventoryContract.Product.P_pictures
                        };
                return  new CursorLoader(this,CurrentUri,Product_projecton,null,null,null);

            case Suppiler_loader_id:
                String[] Suppiler_projection=
                        {
                                InventoryContract.Supplier.suppiler_id,
                                InventoryContract.Supplier.suppiler_name,
                                InventoryContract.Supplier.supplier_phone,
                                InventoryContract.Supplier.supplier_email
                        };
                return new CursorLoader(this,InventoryContract.Supplier.Content_Uri,Suppiler_projection,null,null,null);


            default:
                throw new IllegalArgumentException("Invalid Loader ID");
        }




    }

    @Override
    public void onBackPressed() {
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };

        showUnsavedChangesDialog(discardButtonClickListener);
    }
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Changes");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are u sure u want to delete it");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DeleteProduct();
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }






    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        switch (loader.getId())
        {
            case Product_loader_id:

                if(CurrentUri!=null)
                {
                    if(data.moveToFirst()) {
                        byte[] image = data.getBlob(data.getColumnIndex(InventoryContract.Product.P_pictures));
                        P_Name.setText(data.getString(data.getColumnIndex(InventoryContract.Product.P_Title)));
                        P_Quantity.setText(Integer.toString(data.getInt(data.getColumnIndex(InventoryContract.Product.P_Quantity))));
                        P_Price.setText(Integer.toString(data.getInt(data.getColumnIndex(InventoryContract.Product.P_Price))));
                        P_Picture.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
                        supplierspiner.setSelection(data.getInt(data.getColumnIndex(InventoryContract.Product.P_suppiler)));

                    }

                }

                break;
            case Suppiler_loader_id:
                sAdapter.swapCursor(data);

                break;
        }
       // sAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (loader.getId())
        {
            case Product_loader_id:

                break;
            case Suppiler_loader_id:
                sAdapter.swapCursor(null);

                break;
        }
     //   sAdapter.swapCursor(null);
    }
}
