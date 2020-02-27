package com.example.mediastoreupdatetest;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Long imgId = getNewestImageFromMediaStore();
        Log.v("TestApp", "Newest image ID is " + imgId);
        updateImage(imgId, "Some text");
        Log.v("TestApp", "Done");
    }

    private boolean checkRuntimePermissions(int permissionCode, String permissionString) {
        // Android 6 (API 23) introduces run-time permissions, which may change (be revoked)
        int permissionCheck = ContextCompat.checkSelfPermission(this, permissionString);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            Log.v("TestApp", "Permission " + permissionString + " (" + permissionCode + ") not granted");

            ActivityCompat.requestPermissions(this,
                    new String[]{permissionString},
                    permissionCode);
            return false;
        } else {
            Log.v("TestApp", "Permission " + permissionString + " (" + permissionCode + ") granted");
            return true;
        }
    }

    private void updateImage(Long imgId, String text) {
        if (!checkRuntimePermissions(MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            return;

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DESCRIPTION, text);

        String sImageId = "" + imgId;
        int res = getContentResolver().update(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values,
            MediaStore.Images.Media._ID + "= ?", new String[]{sImageId});

        if (res == 0) {
            Log.v("TestApp", "Update failed (res = " + res + ")");
        } else {
            Log.v("TestApp", "Update succeeded (res = " + res + ")");
        }
    }

    private Long getNewestImageFromMediaStore() {
        if (!checkRuntimePermissions(MY_PERMISSIONS_READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE))
            return null;

        Uri baseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor c_images = getContentResolver().query(baseUri,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DESCRIPTION
                },
                null, null,
                MediaStore.Images.Media._ID + " DESC"
        );
        if (c_images != null) {
            c_images.moveToFirst();
            return c_images.getLong(0);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }





    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1;
    private static final int MY_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.v("TestApp", "MainActivity.onRequestPermissionsResult() MY_PERMISSIONS_READ_EXTERNAL_STORAGE NOT granted!");
                }
                return;
            }
        }
    }
}
